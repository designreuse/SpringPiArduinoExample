package com.master.service.impl;

import com.master.service.DataStorageService;
import com.master.storage.DataStorage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Daniel Pardo Ligorred
 */
@Service("dataStorageService")
public class DataStorageServiceImpl implements DataStorageService {

    private DataStorage dataStorage;

    public DataStorageServiceImpl() {

        this.dataStorage = new DataStorage();
    }

    @Override
    public void saveValue(String key, String value, boolean log) {

        this.dataStorage.setValue(key, value, log);
    }

    @Override
    public String getStringValue(String key) {

        try {

            return this.dataStorage.getStringValue(key);
        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }

        return "";
    }

    @Override
    public int getIntValue(String key) {

        try {

            return this.dataStorage.getIntValue(key);
        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }

        return 0;
    }

    @Override
    public boolean getBooleanValue(String key) {

        try {

            return this.dataStorage.getBooleanValue(key);
        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }

        return false;
    }

    @Override
    public void registerLog(String logLine) {

        this.dataStorage.registerLog(logLine);
    }

    @Override
    public List<String> getLog() {

        return this.dataStorage.getLog();
    }

}