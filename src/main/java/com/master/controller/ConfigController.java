package com.master.controller;

import com.master.service.DataStorageService;
import com.master.service.I2CService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Daniel Pardo Ligorred
 */
@Controller
@RequestMapping("/config")
public class ConfigController {

    private SimpleDateFormat timeDateFormat;

    @Autowired
    private DataStorageService dataStorageService;
    @Autowired
    private I2CService i2CService;

    public ConfigController() {

        this.timeDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    }

    @RequestMapping(value = "/fixedIrrigate", method = RequestMethod.POST)
    public String fixedIrrigate(
            ModelMap model,
            @RequestParam(required = false) String fertilizer, @RequestParam int percentage,
            @RequestParam String datetime){

        Date irrigateDate = null;

        try {

            irrigateDate = this.timeDateFormat.parse(datetime);
        } catch (ParseException e) {

            e.printStackTrace();
            return "redirect:/";
        }

        if(new Date().before(irrigateDate)){

            boolean isFertilized = (fertilizer != null) ? true : false;

            this.i2CService.addFixedIrrigate(irrigateDate, isFertilized, percentage);
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/scheduledIrrigate", method = RequestMethod.POST)
    public String scheduledIrrigate(
            ModelMap model,
            @RequestParam(required = false) String fertilizer, @RequestParam int percentage,
            @RequestParam String time, @RequestParam String scheduleDay){

        if(time != null && (scheduleDay != null)){

            boolean isFertilized = (fertilizer != null) ? true : false;

            this.i2CService.addScheduledIrrigate(time, scheduleDay, isFertilized, percentage);
        }

        return "redirect:/";
    }

}