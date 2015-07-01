package com.master.plotty;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Simple class to set and retrieve data from Plotly.
 *
 * @author Daniel Pardo Ligorred
 */
public class PlotlySender {

    public static String sendData(int temp, int hum) throws Exception {

        String httpsURL = "https://plot.ly/clientresp";

        String query = "un=" + URLEncoder.encode("your_user_account", "UTF-8");
        query += "&key=" + URLEncoder.encode("your_password","UTF-8");
        query += "&origin=" + URLEncoder.encode("plot","UTF-8");
        query += "&platform=" + URLEncoder.encode("java","UTF-8");

        query += "&args=" + URLEncoder.encode(
                "[" +
                        "{\"y\": [" + hum + "], \"name\": \"Humedad\"}, {\"y\": [" + temp + "], \"name\": \"Temperatura\"}" +
                        "]", "UTF-8");

        query += "&kwargs=" +
                URLEncoder.encode(
                        "{\"filename\": \"Riego\", \"fileopt\": \"extend\", " +
                        "\"style\": {\"type\": \"scatter\"}, " +
                        "\"world_readable\": true}", "UTF-8");

        URL myurl = new URL(httpsURL);
        HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-length", String.valueOf(query.length()));
        con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
        con.setDoOutput(true);
        con.setDoInput(true);

        DataOutputStream output = new DataOutputStream(con.getOutputStream());

        output.writeBytes(query);

        output.close();

        DataInputStream input = new DataInputStream( con.getInputStream() );

        StringBuilder resp = new StringBuilder();

        for( int c = input.read(); c != -1; c = input.read() ) {

            resp.append((char) c);
        }

        input.close();

        System.out.println("\nResp:");
        System.out.print(resp.toString());

        System.out.println("\nResp Code:" + con.getResponseCode());
        System.out.println("\nResp Message:" + con.getResponseMessage());

        String aux = resp.toString().substring(resp.toString().indexOf("https"));

        return aux.substring(0, aux.toString().indexOf("\""));
    }

}