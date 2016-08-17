package com.alexandr.lostfilm.inet;



import java.io.BufferedInputStream;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HTTPUrl {


    public String getCodeByUrlToString(String path) {

        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(path);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(con.getInputStream());
            byte[] buffer = new byte[1024];
            int len = in.read(buffer);
            while (len != -1) {
                result.append(new String(buffer,"Cp1251"));
                len = in.read(buffer);
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
            in.close();
            return result.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


}
