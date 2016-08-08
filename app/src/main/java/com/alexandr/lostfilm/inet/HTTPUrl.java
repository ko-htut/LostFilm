package com.alexandr.lostfilm.inet;

import android.os.Environment;

import android.util.Log;


import java.io.BufferedInputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alexandr Novak on 22/05/16.
 */
public class HTTPUrl {



    public static final String SAVED_FILE =Environment.getExternalStorageDirectory()+"/savedFile.xml";
    public static final String DEBUG_FILE=Environment.getExternalStorageDirectory()+"/debug.txt";

/*
    public File getCodeByUrl(String path) {
        File debugFile = new File(DEBUG_FILE);
        try {
            StringBuilder result = new StringBuilder();
            String result1="";
            URL url = new URL(path);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(con.getInputStream());
            File savedFile = new File(DEBUG_FILE);
            FileOutputStream out = new FileOutputStream(savedFile);


            byte[] buffer = new byte[1024];
            int len = in.read(buffer);
            while (len != -1) {
                result.append(buffer);

                result1=result1+new String(buffer,"cp1251");

                out.write(buffer, 0, len);
                len = in.read(buffer);
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
            out.flush();
            in.close();
            out.close();
            return savedFile;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }*/

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


    public File getFileByUrl(String path)
    {
        try
        {
            File savedFile = new File(SAVED_FILE);
            savedFile.delete();
            savedFile.createNewFile();
            Log.i("FILE", "savedFile: "+savedFile.getAbsolutePath());
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(con.getInputStream());
            FileOutputStream out = new FileOutputStream(savedFile);

            byte[] buffer = new byte[1024];
            int len = in.read(buffer);
            while (len != -1) {
                out.write(buffer, 0, len);
                len = in.read(buffer);
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
            out.flush();
            in.close();
            out.close();
            return savedFile;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
