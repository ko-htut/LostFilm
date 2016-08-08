package com.alexandr.lostfilm.database.task;


import android.content.Context;

import android.os.AsyncTask;

import com.alexandr.lostfilm.database.AllSerials;
import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.inet.parse.WebParser;

import java.util.ArrayList;

/**
 * Created by alexandr on 02/08/16.
 */
public class DbTaskAddAllSerials extends AsyncTask<Context, Void, Void> {
    @Override
    protected Void doInBackground(Context... params) {

        //DBHelper dbHelper = new DBHelper(params[0]);
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        DB db = new DB(params[0]);
        db.open();

        WebParser wb = new WebParser();
        ArrayList<AllSerials> listAllSerials =wb.parseAllSerials();


        for (int i=0; i<listAllSerials.size();i++)
        {
            AllSerials serial = listAllSerials.get(i);
            db.addToAll(serial.getLink(),serial.getRuName(),serial.getEngName());
        }
        db.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
