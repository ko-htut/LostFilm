package com.alexandr.lostfilm.inet.parse;

import android.os.Environment;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by alexandr on 01/07/16.
 */
public class JsoupParse {

    public static String SITE_ROOT = "http://www.lostfilm.tv";
    public static String LIST_ALL="http://www.lostfilm.tv/serials.php";
    public static String LIST_RECENT="http://www.lostfilm.tv/browse.php";

    public void some()
    {
        Document doc = null;
        try {
            doc = Jsoup.connect(LIST_RECENT).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements el = doc.getElementsByClass("content_body");
        Document doc_cut = Jsoup.parse(el.toString());

        String content_body = el.toString();
        content_body=content_body.substring(0,content_body.indexOf("<span class=\"d_pages_link_selected\">1</span>"));

        Log.i("content_body_info",content_body);
        System.out.println("doc_text "+doc_cut.text());
        Elements all = doc_cut.getAllElements();
        for (int i=0;i<all.size();i++)
        {
         //   Log.i("elements all ",String.valueOf(i)+" "+all.get(i));
        }
        try {
            FileWriter fw = new FileWriter(new File(Environment.getExternalStorageDirectory()+"/jsoup.txt"));
            FileWriter fw_cut = new FileWriter(new File(Environment.getExternalStorageDirectory()+"/content_body.txt"));
            fw.write(el.toString()); fw_cut.write(content_body);
            fw.flush(); fw_cut.flush();
            fw.close(); fw_cut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("jsoup",el.toString());
    }
}
