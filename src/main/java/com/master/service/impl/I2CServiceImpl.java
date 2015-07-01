package com.master.service.impl;

import com.master.plotty.PlotlySender;
import com.master.service.DataStorageService;
import com.master.service.I2CService;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * Comunication service between Arduino and Rapsberry with I2C.
 *
 * @author Daniel Pardo Ligorred
 */
@Service("i2CService")
public class I2CServiceImpl implements I2CService {

    private SimpleDateFormat timeDateFormat;

    @Autowired
    private DataStorageService dataStorageService;

    @Autowired
    @Qualifier(value = "scheduler")
    private ThreadPoolTaskScheduler scheduler;

    private ScheduledFuture<?> scedulefuture;

    private I2CBus bus;
    private I2CDevice device;

    public I2CServiceImpl() throws IOException {

        this.bus = I2CFactory.getInstance(I2CBus.BUS_1);
        this.device = bus.getDevice(0x04);
        this.timeDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    }

    @Override
    public int[] retrieveInfo() {

        byte[] data = new byte[9];
        int[] dataVals = new int[3];

        try {

            device.read(data, 0, data.length);

            byte status = data[0];
            byte[] temp = new byte[]{data[1], data[2], data[3], data[4]};
            byte[] hum = new byte[]{data[5], data[6], data[7], data[8]};

            dataVals[0] = (int) status;
            dataVals[1] = ByteBuffer.wrap(temp).getInt();
            dataVals[2] = ByteBuffer.wrap(hum).getInt();

            this.dataStorageService.saveValue("plottyUrl", PlotlySender.sendData(dataVals[1], dataVals[2]), false);
            this.dataStorageService.saveValue("plottyLast", this.timeDateFormat.format(new Date()), false);
        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return dataVals;
    }

    private void sendWork(boolean fertilizer, int percentage){

        ByteBuffer byteBuffer = ByteBuffer.wrap(
                new byte[]{
                        (byte) ((fertilizer) ? 1 : 0),
                        (byte) percentage,
                });

        try {

            this.device.write(byteBuffer.array(), 0, byteBuffer.array().length);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void addFixedIrrigate(Date date, boolean fertilizer, int percentage){

        this.scheduler.schedule(new Runnable() {

            @Override
            public void run() {

                I2CServiceImpl.this.sendWork(fertilizer, percentage);
            }

        }, date);

        this.dataStorageService.registerLog("Riego fijo programado a fecha: " + date.toString());
    }

    @Override
    public void addScheduledIrrigate(String time, String scheduleDay, boolean fertilizer, int percentage) {

        if(this.scedulefuture != null){

            this.scedulefuture.cancel(false);
        }

        String[] times = time.split(":");

        this.scedulefuture =
                this.scheduler.schedule(new Runnable() {

                    @Override
                    public void run() {

                        I2CServiceImpl.this.sendWork(fertilizer, percentage);
                    }

                }, new CronTrigger("0 " + times[1] + " " + times[0] + " ? * " + scheduleDay)); //SEC MIN HOUR MONTHDAY YEAR DAYS

        this.dataStorageService.registerLog("Riego programado a las " + time + " los días: " + scheduleDay);
    }

}