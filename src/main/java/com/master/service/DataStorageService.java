package com.master.service;

import java.util.List;

/**
 * @author Daniel Pardo Ligorred
 */
public interface DataStorageService {

    /**
     * Set new data into storage.
     *
     * @param key
     * @param value
     * @param log
     */
    void saveValue(String key, String value, boolean log);

    /**
     * Get string value from storage.
     *
     * @param key
     * @return
     */
    String getStringValue(String key);

    /**
     * Get integer value from stogare.
     *
     * @param key
     * @return
     */
    int getIntValue(String key);

    /**
     * Get boolean value from storage.
     *
     * @param key
     * @return
     */
    boolean getBooleanValue(String key);

    /**
     * Register new entry into log storage.
     *
     * @param logLine
     */
    void registerLog(String logLine);

    /**
     * Get all entries from log storage.
     * @return
     */
    List<String> getLog();

}