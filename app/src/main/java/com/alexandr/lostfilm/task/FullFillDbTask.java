package com.alexandr.lostfilm.task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.alexandr.lostfilm.MainActivity;
import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.inet.WebParser;

/**
 * Created by alexandr on 29/08/16.
 */
public class FullFillDbTask extends AsyncTask<MainActivity, Void, Void> {
    MainActivity mActivity;

    @Override
    protected Void doInBackground(MainActivity... params) {
        mActivity=params[0];
        DB db = new DB(mActivity);
        db.openWritable();
        db.formatDB();
        db.close();
        WebParser wb = new WebParser(mActivity);
        wb.parseReallyAllSerials();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(mActivity,"ready",Toast.LENGTH_LONG).show();
    }
}
