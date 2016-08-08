package com.alexandr.lostfilm.inet.parse;

import android.os.Environment;
import android.util.Log;

import com.alexandr.lostfilm.database.AllSerials;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexandr on 22/05/16.
 */
public class WebParser {
    public static final String NEW = "http://www.lostfilm.tv/browse.php";
    public static final String ALL = "http://www.lostfilm.tv/serials.php";
    public static final String baseURL = "http://lostfilm.tv";

    public void parseNewSeries() {
        HTTPUrl download = new HTTPUrl();

        String parseString = download.getCodeByUrlToString(NEW);
        if (parseString!=null) {
            String source = parseString.toString();
            source = source.substring(source.indexOf("<div class=\"content_body\">"),source.length());
            source = source.substring(0,source.indexOf("d_pages_link"));
            source = source.replace("<div class=\"content_body\">","<br clear=both> <br clear=both>");

            Pattern pattern = Pattern.compile
                    ("(?imsd)<br clear=both>\\s*<br clear=both>.*?<!--\\s*([0-9]{2}\\.[0-9]{2}).*?000000\">(.*?)</span>.*?<img src=\"([^\"]+)\".*?<b>(.*?)</b>.*?<b>(.*?)</b>");
            Matcher matcher = pattern.matcher(source);

            ArrayList<FavSerials> result = new ArrayList<>();
            while (matcher.find())
            {

            }
            /**
            $seasons = $matches[1]; // Сезоны
            $titles = $matches[2]; // Названия
            $images = $matches[3]; // Пути к картинкам
            $descriptions = $matches[4]; // Описания
            $dates = $matches[5]; // Даты
             */
           // String out = matcher.group(1)+" "+matcher.group(2)+" "+matcher.group(3)+" "+matcher.group(4)+" "+matcher.group(5);
           // Log.i("shit",out);

        } else {
            Log.i("PHP fuck", "it is null");
        }
        //return result;
    }

    public ArrayList<AllSerials> parseAllSerials() {

        ArrayList<AllSerials> result= new ArrayList<>();
        HTTPUrl download = new HTTPUrl();

        String parseString = download.getCodeByUrlToString(ALL);
        if (parseString!=null) {
            String source = parseString.toString();
            source = source.substring(source.indexOf("<!-- ### Полный список сериалов -->"),source.length());
            source = source.substring(0,source.indexOf("<br />"));
            writeToFile(source);

            Pattern pattern = Pattern.compile
                    ("(?imsd)<a href=\"([^\"]+).*?\" class=\"bb_a\">(.*?)<br><span>(.*?)</span>");
            Matcher matcher = pattern.matcher(source);

            while (matcher.find())
            {
                String link = matcher.group(1);
                String ru = matcher.group(2);
                String en = matcher.group(3);
                result.add(new AllSerials(link,ru,en));
                try
                {
                    Log.i("debugDetail",ru);
                    parseAllDetail(new URL(baseURL+link));
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }



        } else {
            Log.i("PHP fuck", "it is null");
        }
        return result;
    }



    private void writeToFile (String s)
    {
        try {
            Log.i("ebat",s);
            System.out.println(s);
            FileWriter fw = new FileWriter(new File(Environment.getExternalStorageDirectory()+"/ebat.txt"));
            fw.write(s);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseAllDetail(URL link)
    {
        try
        {
            Document doc = Jsoup.parse(link,1000);
            Element element = doc.select("div.mid").first();

            Element pic = element.select("img").first();
            String pic_link = pic.attr("src");
            Log.i("debugDetail",pic_link);

            Pattern pattern = Pattern.compile
                    ("(?imsd)Статус: (.*?)Сайт");
            Matcher matcher = pattern.matcher(element.text());
            matcher.find();
            String status = matcher.group(1);
            Log.i("debugDetail"," status: "+status);

            Element lastEpisode = element.select("span.micro").first();
            //Log.i("debugDetail"," micro: "+lastEpisode.text());
            String[] info = lastEpisode.text().split(",");
            Log.i("debugDetail"," date: "+info[0]);
            Log.i("debugDetail"," episode: "+info[1]);
            Log.i("debugDetail"," ");

        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
