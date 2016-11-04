package com.alexandr.lostfilm.inet;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.alexandr.lostfilm.database.AllSerials;
import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.notification.CustomNotification;
import com.alexandr.lostfilm.notification.NotificationDisplay;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebParser {
    public static final String NEW = "http://www.lostfilm.tv/browse.php";
    public static final String ALL = "http://www.lostfilm.tv/serials.php";
    public static final String baseURL = "http://lostfilm.tv";
    private Context mCtx;
    private DB mDB;



    public WebParser(Context ctx) {
        this.mCtx = ctx;
        mDB = new DB(ctx);
        mDB.openWritable();
    }


    public void parseSerial(String link,String date,String ruName)
    {
        ConnectivityManager cm =
                (ConnectivityManager)mCtx.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork==null|| activeNetwork.isConnectedOrConnecting()==false){return;}
        boolean isConnected = activeNetwork.isConnectedOrConnecting();
        if(!isConnected)
        {
            return;
        }
        Document doc = null;
        try
        {
            doc = Jsoup.parse(new URL(link), 5000);
        }
        catch (IOException e)
        {
            Log.i("DEBUG_TASK_ALL","msg "+e.getMessage());
           return;
        }

        Element element = doc.select("div.mid").first();
 // parse status
        Pattern pattern = Pattern.compile
                ("(?imsd)Статус: (.*?)Сайт");
        Matcher matcher = pattern.matcher(element.text());
        matcher.find();
        String status = matcher.group(1).trim();
 // parse lastEpisode
        Element lastEpisode = element.select("span.micro").first();
        String[] info = lastEpisode.text().split(",");
        if(info[0].equals(date))
        {return;}
// detail ru eng
        Element lastEpisodeName = element.select("td.t_episode_title").first();
        String descr_ru;
        String descr_eng;
        if (lastEpisodeName.text().contains("(")) {
            descr_ru = lastEpisodeName.text().substring(0, lastEpisodeName.text().indexOf("(")).trim();
            descr_eng = lastEpisodeName.text().substring(lastEpisodeName.text().indexOf("("),
                    lastEpisodeName.text().length());
        } else {
            descr_ru = lastEpisodeName.text().trim();
            descr_eng = "";
        }
        descr_eng = descr_eng.replace("(", "");
        descr_eng = descr_eng.replace(")", "").trim();
        mDB.updateSerial(ruName,info[1],descr_ru,descr_eng,status,info[0]);

    }

    public NotificationDisplay parseFavSerial(String link, String date, String ruName)
    {
        ConnectivityManager cm =
                (ConnectivityManager)mCtx.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if( activeNetwork==null|| activeNetwork.isConnectedOrConnecting()==false){return null;}
        boolean isConnected = activeNetwork.isConnectedOrConnecting();
        if(!isConnected)
        {
            return null;
        }
        Document doc = null;
        try
        {
            Log.i("debugLink",link);
            doc = Jsoup.parse(new URL(link), 5000);
        }
        catch (IOException e)
        {
            Log.i("DEBUG_TASK_FAV","pzdc "+e.getMessage());
            return null;
        }
        try {
            Element element = doc.select("div.mid").first();
            // parse status
            Pattern pattern = Pattern.compile
                    ("(?imsd)Статус: (.*?)Сайт");
            Matcher matcher = pattern.matcher(element.text());
            matcher.find();
            String status = matcher.group(1).trim();
            // parse lastEpisode
            // info[0] = date, info[1]=episode
            Element lastEpisode = element.select("span.micro").first();
            String[] info = lastEpisode.text().split(",");
            if (info[0].equals(date)) {
                return null;
            }
// detail ru eng
            Element lastEpisodeName = element.select("td.t_episode_title").first();

            String descr_ru;
            String descr_eng;
            if (lastEpisodeName.text().contains("(")) {
                descr_ru = lastEpisodeName.text().substring(0, lastEpisodeName.text().indexOf("(")).trim();
                descr_eng = lastEpisodeName.text().substring(lastEpisodeName.text().indexOf("("),
                        lastEpisodeName.text().length());
            } else {
                descr_ru = lastEpisodeName.text().trim();
                descr_eng = "";
            }
            descr_eng = descr_eng.replace("(", "");
            descr_eng = descr_eng.replace(")", "").trim();
            mDB.updateSerial(ruName, info[1], descr_ru, descr_eng, status, info[0]);
            CustomNotification notification = new CustomNotification(mCtx);
            return notification.newNotification(mCtx, ruName, true);
        }
        catch (Exception e)
        {
            Log.i("debugBAD",e.getMessage()+" "+link);
            Log.i("debugBAD",e.getClass().toString()+" ");
        }
        return null;
    }

    public void close()
    {
        mDB.close();
    }

    public ArrayList<AllSerials> parseReallyAllSerials() {

        ArrayList<AllSerials> result = new ArrayList<>();
        HTTPUrl download = new HTTPUrl();

        if(!isConnected()) return result;
        String parseString = download.getCodeByUrlToString(ALL);
        if (parseString != null) {
            String source = parseString.toString();
            source = source.substring(source.indexOf("<!-- ### Полный список сериалов -->"), source.length());
            source = source.substring(0, source.indexOf("<br />"));


            Pattern pattern = Pattern.compile
                    ("(?imsd)<a href=\"([^\"]+).*?\" class=\"bb_a\">(.*?)<br><span>(.*?)</span>");
            Matcher matcher = pattern.matcher(source);


            while (matcher.find()) {
                String link = matcher.group(1);
                String ru = matcher.group(2);
                String en = matcher.group(3);

                try {
                    parseAllDetail(new URL(baseURL + link), ru, en);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
            mDB.close();
            Log.i("debug", " all serials parsed");

        } else {
            Log.i("PHP", "it is null");
        }
        return result;
    }

    public ArrayList<NotificationDisplay> parseAllSerials() {

        ArrayList<NotificationDisplay> result = new ArrayList<>();

        HTTPUrl download = new HTTPUrl();
        if(!isConnected()) return result;
        String parseString = download.getCodeByUrlToString(ALL);
        if (parseString != null) {
            String source = parseString.toString();
            source = source.substring(source.indexOf("<!-- ### Полный список сериалов -->"), source.length());
            source = source.substring(0, source.indexOf("<br />"));


            Pattern pattern = Pattern.compile
                    ("(?imsd)<a href=\"([^\"]+).*?\" class=\"bb_a\">(.*?)<br><span>(.*?)</span>");
            Matcher matcher = pattern.matcher(source);

            DB db = new DB(mCtx);
            db.openReadOnly();
            HashSet<String> ruNames = new HashSet<>();

            Cursor all = db.getReallyAllSerials();
            int ruNameColIndex = all.getColumnIndex(DB.ALL_COLUMN_RU_NAME);
            if (all.moveToFirst()) {
                do {
                    ruNames.add(all.getString(ruNameColIndex));
                } while (all.moveToNext());
            } else {
                Log.i("debugWebParser","db is empty");
            }
            db.close();
            CustomNotification notification = new CustomNotification(mCtx);
            while (matcher.find()) {
                String link = matcher.group(1);
                String ru = matcher.group(2);
                String en = matcher.group(3);
                if (!ruNames.contains(ru) || ruNames.isEmpty()) {
                    try {
                        parseAllDetail(new URL(baseURL + link), ru, en);
                        result.add(notification.newNotification(mCtx, ru, false));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
            mDB.close();
            Log.i("debug", " all serials parsed");

        } else {
            Log.i("PHP", "it is null");
        }
        return result;
    }

    private void parseAllDetail(URL link, String ru, String eng) {
        try {
            if(!isConnected()) return;
            Document doc = Jsoup.parse(link, 5000);
            Element element = doc.select("div.mid").first();

            Pattern pattern = Pattern.compile
                    ("(?imsd)Статус: (.*?)Сайт");
            Matcher matcher = pattern.matcher(element.text());
            matcher.find();
            String status = matcher.group(1).trim();

            Element pic = element.select("img").first();
            String pic_link = baseURL+pic.attr("src");

            Element lastEpisode = element.select("span.micro").first();

            String[] info = lastEpisode.text().split(",");
            // info[0] = date, info[1]=episode

            //parse descr ru,eng
            Element lastEpisodeName = element.select("td.t_episode_title").first();

            String descr_ru;
            String descr_eng;
            if (lastEpisodeName.text().contains("(")) {
                descr_ru = lastEpisodeName.text().substring(0, lastEpisodeName.text().indexOf("(")).trim();
                descr_eng = lastEpisodeName.text().substring(lastEpisodeName.text().indexOf("("),
                        lastEpisodeName.text().length());

            } else {
                descr_ru = lastEpisodeName.text().trim();
                descr_eng = "";
            }
            descr_eng = descr_eng.replace("(", "");
            descr_eng = descr_eng.replace(")", "").trim();


            mDB.addToAll(link.toString(), ru, eng, status.trim(), pic_link,
                    bigPicToSmall(pic_link), info[0], info[1], descr_ru, descr_eng, 0);
            Log.i("debug_webparser",pic_link);

        } catch (IOException e) {
            Log.i("DEBUG_TASK_ALL","msg "+e.getMessage());
            return;
        }
    }

    private String bigPicToSmall(String link) {
        link = link.replace("posters", "icons");
        link = link.replace("poster", "cat");
        return link;
    }

    private boolean isConnected()
    {
        ConnectivityManager cm =
                (ConnectivityManager)mCtx.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.i("debugCm",cm.toString());
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if( activeNetwork==null || activeNetwork.isConnectedOrConnecting()==false)
        {
            return false;
        }
        else
        return true;
    }
}
