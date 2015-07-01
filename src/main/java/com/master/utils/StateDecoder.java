package com.master.utils;

/**
 * @author Daniel Pardo Ligorred
 */
public class StateDecoder {

    public static String getStatusPhrase(int status){

        switch (status){
            case 1:
                return "Rellenando tanque de mezcla";
            case 2:
                return "Mezclando";
            case 3:
                return "Regando";
            default:
                return "En Espera";
        }
    }

}