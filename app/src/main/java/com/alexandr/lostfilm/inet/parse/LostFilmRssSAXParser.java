package com.alexandr.lostfilm.inet.parse;


import android.os.Environment;

import com.alexandr.lostfilm.inet.HTTPUrl;
import com.alexandr.lostfilm.inet.SerialRSS;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class LostFilmRssSAXParser {
    String RSS = "http://www.lostfilm.tv/rssdd.xml";

    public LostFilmRssSAXParser() {
        try {
            HTTPUrl download = new HTTPUrl();
            //File toParse = download.getFileByUrl(RSS);
            File toParse= new File (Environment.getExternalStorageDirectory()+"/savedFile.xml");
           // File toParse=new File(Environment.getExternalStorageDirectory()+"/converted.xml");
           // FileInputStream fis = new FileInputStream(toConvert);


            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            RSSHandler rsshandler = new RSSHandler();
            saxParser.parse(toParse, rsshandler);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class RSSHandler extends DefaultHandler {
        SerialRSS rssItem;
        String thisElement = "";
        boolean TitleNnChannel = true;
        boolean DescriptionInChannel = true;
        boolean LinkInChannel = true;

        public ArrayList<SerialRSS> getRSSitems() {
            return result;
        }

        ArrayList<SerialRSS> result = new ArrayList<>();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            System.out.println("Element: " + qName);
            // System.out.println("title, description, link "+ TitleNnChannel+" "+DescriptionInChannel+" "+LinkInChannel+"\n\n");
            if (qName.equalsIgnoreCase("item")) {
                rssItem = new SerialRSS();
                thisElement = "";
            }

            if (qName.equalsIgnoreCase("title")) {
                if (TitleNnChannel) {
                    thisElement = "";
                    TitleNnChannel = false;
                } else {
                    thisElement = qName;
                }
            }
            if (qName.equalsIgnoreCase("description")) {
                if (DescriptionInChannel) {
                    thisElement = "";
                    DescriptionInChannel = false;
                } else {
                    thisElement = qName;
                }
            }
            if (qName.equalsIgnoreCase("pubDate")) {
                thisElement = qName;

            }
            if (qName.equalsIgnoreCase("link")) {
                if (LinkInChannel) {
                    thisElement = "";
                    LinkInChannel = false;
                } else {
                    thisElement = qName;
                }

            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equalsIgnoreCase("item")) {
                result.add(rssItem);
                //rssItem = null;
            }
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            String s = new String(ch, start, length);
            if (thisElement.equals("title")) {
                if (TitleNnChannel) {
                    TitleNnChannel = false;
                    return;
                } else {
                    rssItem.setTitle(s);
                }
            }
            if (thisElement.equals("description")) {
                if (DescriptionInChannel) {
                    DescriptionInChannel = false;
                    return;
                } else {
                    rssItem.setDescription(s);
                }
            }
            if (thisElement.equals("pubDate")) {
                rssItem.setPubDate(s);
            }
            if (thisElement.equals("link")) {
                if (LinkInChannel) {
                    LinkInChannel = false;
                    return;
                }
                else
                {
                    if (rssItem==null) {System.out.println("rssItem==null");}
                    if (s==null) {System.out.println("s==null");}
                    rssItem.setLink(s);

                }
            }
        }


        @Override
        public void endDocument() {
            System.out.println("End document. Stop parsing XML..." + "\n");

            for (SerialRSS item : result) {
                System.out.println("----------------");
                System.out.println("title: " + item.getTitle());
                System.out.println("description :" + item.getDescription());
                System.out.println("pubDate: " + item.getPubDate());
                System.out.println("link: " + item.getLink());
            }

        }


    }
}
