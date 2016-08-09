package com.alexandr.lostfilm.database.task;


import android.content.Context;

import android.os.AsyncTask;

import com.alexandr.lostfilm.database.AllSerials;
import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.inet.WebParser;

import java.util.ArrayList;

public class DbTaskAddAllSerials extends AsyncTask<Context, Boolean, Void> {
    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected Void doInBackground(Context... params) {
        DB db = new DB(params[0]);
        db.open();

        WebParser wb = new WebParser(params[0]);
        ArrayList<AllSerials> listAllSerials =wb.parseAllSerials();

        for (int i=0; i<listAllSerials.size();i++)
        {
            AllSerials serial = listAllSerials.get(i);
           // db.addToAll(serial.getLink(),serial.getRuName(),serial.getEngName());
        }
        db.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        super.onPostExecute(aVoid);
    }
}
