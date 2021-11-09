package com.example.dp3t_usp.HTTPService;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

// Essa classe monta as requisições no formato para get ou post a partir de um RequestPackage
public class RequestsHelper{
    public static String getData(RequestPackage requestPackage){
        BufferedReader reader = null;
        String uri = requestPackage.getUrl();

        if(requestPackage.getMethod().equals(RequestPackage.methods.GET)){
            uri += "?" + requestPackage.getEncodedParams();
        }

        try{
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestPackage.getMethod());

            if (requestPackage.getMethod().equals(RequestPackage.methods.POST)){
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(requestPackage.getEncodedParams());
                writer.flush();
            }

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }

            return sb.toString();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e){
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

}