package com.master.service;

import java.util.Date;

/**
 * @author Daniel Pardo Ligorred
 */
public interface I2CService {

    /**
     * Fetch data from Arduino.
     *
     * @return
     */
    int[] retrieveInfo();

    /**
     * Register fixed irrigate.
     *
     * @param date
     * @param fertilizer
     * @param percentage
     */
    void addFixedIrrigate(Date date, boolean fertilizer, int percentage);

    /**
     * Register scheduled irrigate.
     *
     * @param time
     * @param scheduleDay
     * @param fertilizer
     * @param percentage
     */
    void addScheduledIrrigate(String time, String scheduleDay, boolean fertilizer, int percentage);

}