package com.alexandr.lostfilm.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.inet.WebParser;

import java.util.HashSet;

/**
 * Created by alexandr on 01/09/16.
 */
public class AllSerialUpdateTask extends AsyncTask<Context,Void,Void> {
    @Override
    protected void onPreExecute() {
        Log.i("debugAsynkUpdateAll","start");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.i("debugAsynkUpdateAll","stop");
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Context... params) {
        Context mCtx = params[0];
        Log.i("debugAsynk","doInBackgroundAll");
        DB db = new DB(mCtx);
        WebParser webParser = new WebParser(mCtx);
        db.openWritable();
        Cursor c = db.getAllSerials();
        int ruName = c.getColumnIndex(DB.ALL_COLUMN_RU_NAME);
        int date = c.getColumnIndex(DB.ALL_COLUMN_DATE);
        int link = c.getColumnIndex(DB.ALL_COLUMN_LINK);

        if (c.moveToFirst()) {
            do
            {
                webParser.parseSerial(c.getString(link),c.getString(date),c.getString(ruName));

            } while (c.moveToNext());

        }
        webParser.close();
        return null;
    }
}
