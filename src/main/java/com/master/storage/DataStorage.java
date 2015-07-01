package com.master.storage;

import com.master.exception.DataException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Pardo Ligorred
 */
public class DataStorage implements Serializable {

    private Map<String, Object> values;

    private SimpleDateFormat dateFormat;

    private Queue<String> log;

    public DataStorage() {

        this.values  = new HashMap<>();
        this.log = new PriorityQueue<>(30);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd 'T' HH:mm:ss");

        this.log.add(String.format("[<span class=\"date\">%s</span>] System Up!!", this.dateFormat.format(new Date())));
    }

    public void setValue(String key, Object value, boolean log){

        if(log) {

            this.log.offer(
                    String.format("[<span class=\"date\">%s</span>] <span class=\"value\">%s: %s</span>",
                            this.dateFormat.format(new Date()), key, value.toString()));
        }

        this.values.put(key, value);
    }

    public String getStringValue(String key) throws DataException{

        if(this.values.containsKey(key)){

            return this.values.get(key).toString();
        }

        throw new DataException(key + " doesn´t exist.");
    }

    public int getIntValue(String key) throws DataException{

        if(this.values.containsKey(key)){

            return Integer.parseInt(this.values.get(key).toString());
        }

        throw new DataException(key + " doesn´t exist.");
    }

    public boolean getBooleanValue(String key) throws DataException{

        if(this.values.containsKey(key)){

            return Integer.parseInt(this.values.get(key).toString()) == 1;
        }

        throw new DataException(key + " doesn´t exist.");
    }

    public void registerLog(String logLine){

        this.log.offer(
                String.format("[<span class=\"date\">%s</span>] <span class=\"value\">%s</span>",
                        this.dateFormat.format(new Date()), logLine));
    }

    public List<String> getLog(){

        return this.log.parallelStream().collect(Collectors.toList());
    }

}