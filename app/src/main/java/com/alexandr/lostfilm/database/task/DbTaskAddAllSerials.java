package com.alexandr.lostfilm.database.task;


import android.content.Context;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.fragment.FragmentAll;
import com.alexandr.lostfilm.inet.HTTPUrl;
import com.alexandr.lostfilm.inet.WebParser;
import com.example.alexandr.lostfilm.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbTaskAddAllSerials extends AsyncTask<FragmentAll, Boolean, Void> {
    FragmentAll fm;
    private Context mCtx;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
       // swipeRefreshLayout= (SwipeRefreshLayout) fm.getView().findViewById(R.id.swipe_refresh_layout_all);
       // swipeRefreshLayout.setRefreshing(values[0]);

    }

    @Override
    protected Void doInBackground(FragmentAll... params) {
        fm=params[0];
        mCtx=fm.getContext();
        DB db = new DB(mCtx);
        db.open();
/*
        WebParser wb = new WebParser(mCtx);
        ArrayList<AllSerials> listAllSerials =wb.parseAllSerials();

        for (int i=0; i<listAllSerials.size();i++)
        {
            AllSerials serial = listAllSerials.get(i);
           // db.addToAll(serial.getLink(),serial.getRuName(),serial.getEngName());
        }
        db.close();*/
        HTTPUrl download = new HTTPUrl();

        String parseString = download.getCodeByUrlToString(WebParser.ALL);
        if (parseString!=null) {
            String source = parseString.toString();
            source = source.substring(source.indexOf("<!-- ### Полный список сериалов -->"),source.length());
            source = source.substring(0,source.indexOf("<br />"));

            Pattern pattern = Pattern.compile
                    ("(?imsd)<a href=\"([^\"]+).*?\" class=\"bb_a\">(.*?)<br><span>(.*?)</span>");
            Matcher matcher = pattern.matcher(source);

            while (matcher.find())
            {
                publishProgress(true);
                String link = matcher.group(1);
                String ru = matcher.group(2);
                String en = matcher.group(3);
                db.addToAll(link,ru,en);
                //Log.i("debug",ru);
            }
            Log.i("debug","all seriae parsed");
        } else {
            Log.i("PHP", "it is null");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //fm.refreshListView();
    }
}
