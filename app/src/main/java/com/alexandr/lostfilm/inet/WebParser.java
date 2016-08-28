package com.alexandr.lostfilm.inet;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.alexandr.lostfilm.database.AllSerials;
import com.alexandr.lostfilm.database.DB;
import com.alexandr.lostfilm.database.FavSerials;
import com.alexandr.lostfilm.inet.HTTPUrl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebParser {
    public static final String NEW = "http://www.lostfilm.tv/browse.php";
    public static final String ALL = "http://www.lostfilm.tv/serials.php";
    public static final String baseURL = "http://lostfilm.tv";
    private Context mCtx;
    private DB mDB;


    //public WebParser() {   }

    public WebParser(Context ctx) {
        this.mCtx = ctx;
        mDB = new DB(ctx);
        mDB.openWritable();
    }

    public void parseNewSeries() {
        HTTPUrl download = new HTTPUrl();

        String parseString = download.getCodeByUrlToString(NEW);
        if (parseString != null) {
            String source = parseString.toString();
            source = source.substring(source.indexOf("<div class=\"content_body\">"), source.length());
            source = source.substring(0, source.indexOf("d_pages_link"));
            source = source.replace("<div class=\"content_body\">", "<br clear=both> <br clear=both>");

            Pattern pattern = Pattern.compile
                    ("(?imsd)<br clear=both>\\s*<br clear=both>.*?<!--\\s*([0-9]{2}\\.[0-9]{2}).*?000000\">(.*?)</span>.*?<img src=\"([^\"]+)\".*?<b>(.*?)</b>.*?<b>(.*?)</b>");
            Matcher matcher = pattern.matcher(source);

            ArrayList<FavSerials> result = new ArrayList<>();
            while (matcher.find()) {

            }
            /**
             $seasons = $matches[1]; // Сезоны
             $titles = $matches[2]; // Названия
             $images = $matches[3]; // Пути к картинкам
             $descriptions = $matches[4]; // Описания
             $dates = $matches[5]; // Даты
             */
            // String out = matcher.group(1)+" "+matcher.group(2)+" "+matcher.group(3)+" "+matcher.group(4)+" "+matcher.group(5);


        } else {
            Log.i("PHP", "it is null");
        }
        //return result;
    }

    public ArrayList<AllSerials> parseAllSerials() {

        ArrayList<AllSerials> result = new ArrayList<>();
        HTTPUrl download = new HTTPUrl();

        String parseString = download.getCodeByUrlToString(ALL);
        if (parseString != null) {
            String source = parseString.toString();
            source = source.substring(source.indexOf("<!-- ### Полный список сериалов -->"), source.length());
            source = source.substring(0, source.indexOf("<br />"));


            Pattern pattern = Pattern.compile
                    ("(?imsd)<a href=\"([^\"]+).*?\" class=\"bb_a\">(.*?)<br><span>(.*?)</span>");
            Matcher matcher = pattern.matcher(source);

          //  DB db = new DB(mCtx);
          //  db.open();
            while (matcher.find()) {
                String link = matcher.group(1);
                String ru = matcher.group(2);
                String en = matcher.group(3);
                //  db.addToAll(link,ru,en);
                //  Log.i("debug",ru);
                //result.add(new AllSerials(link,ru,en));
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

    public ArrayList<AllSerials> parseReallyAllSerials() {

        ArrayList<AllSerials> result = new ArrayList<>();
        HTTPUrl download = new HTTPUrl();

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
            HashSet<String> ruNames = new HashSet<String>();

            Cursor all = db.getAllSerials();
            int ruNameColIndex = all.getColumnIndex(DB.ALL_COLUMN_RU_NAME);
            if (all.moveToFirst()) {
                do {
                    ruNames.add(all.getString(ruNameColIndex));
                } while (all.moveToNext());
            } else {
                //something goes wrong
            }
            db.close();

            while (matcher.find()) {
                String link = matcher.group(1);
                String ru = matcher.group(2);
                String en = matcher.group(3);
                if (!ruNames.contains(ru)|| ruNames.isEmpty()) {
                    try {

                        parseAllDetail(new URL(baseURL + link), ru, en);
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
            Document doc = Jsoup.parse(link, 1000);
            Element element = doc.select("div.mid").first();

            Pattern pattern = Pattern.compile
                    ("(?imsd)Статус: (.*?)Сайт");
            Matcher matcher = pattern.matcher(element.text());
            matcher.find();
            String status = matcher.group(1).trim();
            //if (!status.equals("закончен")) {  here
            //Log.i("debugStatus",status);
            Element pic = element.select("img").first();
            String pic_link = pic.attr("src");
            //Log.i("debugDetail",pic_link);

            // Log.i("debugDetail"," status: "+status);
            Element lastEpisode = element.select("span.micro").first();
            //Log.i("debugDetail"," micro: "+lastEpisode.text());
            String[] info = lastEpisode.text().split(",");
            //Log.i("debugDetail"," date: "+info[0]);
            //Log.i("debugDetail"," episode: "+info[1]);
            //Log.i("debugDetail"," ");
            Element lastEpisodeName = element.select("td.t_episode_title").first();
            //Log.i("debugDetailTest",lastEpisodeName.text());
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

            //Log.i("debugDetail", descr_ru);
            // Log.i("debugDetail", descr_eng);

            mDB.addToAll(link.toString(), ru, eng, status.trim(), pic_link,
                    bigPicToSmall(pic_link), info[0], info[1], descr_ru, descr_eng, 0);

            //} and here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String bigPicToSmall(String link) {
        link = link.replace("posters", "icons");
        link = link.replace("poster", "cat");
        return link;
    }

/*
    public void convert(File f) {
        FileInputStream fis;
        System.out.println(f.getAbsolutePath());
        byte[] bFile = new byte[(int) f.length()];
        try {
            //convert file into array of bytes
            fis = new FileInputStream(f);
            fis.read(bFile);
            fis.close();

            String testCharset = new String(bFile, "Cp1251");
            File newFile = new File(f.getAbsolutePath() + "newFile.txt");
            System.out.println(newFile.getAbsolutePath());
            FileWriter fw = new FileWriter(newFile);
            fw.write(testCharset);
            fw.flush();
            fw.close();
            readFile(newFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }

    private String readFile(File f) {
        FileReader fr = null;
        try {
            fr = new FileReader(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(fr.toString());
        return null;
    }*/

}
