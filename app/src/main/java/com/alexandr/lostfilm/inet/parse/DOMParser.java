package com.alexandr.lostfilm.inet.parse;

import android.util.Log;

import com.alexandr.lostfilm.inet.HTTPUrl;

import  org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by alexandr on 22/05/16.
 */
public class DOMParser {
    public static final String RSS="http://www.lostfilm.tv/rssdd.xml";

    public void parse() throws SAXException {
        HTTPUrl download = new HTTPUrl();
        File toParse  = download.getFileByUrl(RSS);
        if (toParse!=null)
        {
            Log.i("fuck xml  ", String.valueOf(toParse.exists()));
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            dbFactory.setCoalescing(true);
            DocumentBuilder dBuilder = null;
            try
                {
                    dBuilder = dbFactory.newDocumentBuilder();

                }
            catch (ParserConfigurationException e)
                {
                    e.printStackTrace();
                }
            Document doc = null;
            try
                {
                    doc = dBuilder.parse(toParse);
                }
            catch (IOException e)
                {
                    e.printStackTrace();
                }
            doc.getDocumentElement().normalize();

            // получаем список новых серий
            NodeList nList = doc.getElementsByTagName("item");
            Log.i("item","NodeList size: "+nList.getLength());
            for (int i=0;i<nList.getLength();i++)
            {
                // перебор свежих серий
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    //свежая серия
                    Element eElement = (Element) nNode;

                    NodeList name =eElement.getElementsByTagName("title");
                    NodeList description =eElement.getElementsByTagName("description");
                    NodeList pubDate =eElement.getElementsByTagName("pubDate");
                    NodeList link = eElement.getElementsByTagName("link");

                    Log.i("test","item:  "+i+" description:" +description.item(0).getTextContent());
                    Log.i("item","item:  "+i+" name:" +name.item(0).getTextContent().toString());
                    Log.i("item","item:  "+i+" description:" +description.item(0).getTextContent());
                    Log.i("item","item:  "+i+" pubDate:" +pubDate.item(0).getTextContent().toString());
                    Log.i("item","item:  "+i+" link:" +link.item(0).getTextContent().toString());

                    Log.i("item","\n");
                    //   Log.i("item",eElement.getElementsByTagName("title").item(0).getTextContent().toString());
                }
            }
        }

        else
        {
            Log.i("fuck xml  ","it is null");
        }
//        Log.i("XML CODE: ",toParse.toString());
    }

    public static String getCharacterDataFromElement(Element f) {

        NodeList list = f.getChildNodes();
        String data;

        for(int index = 0; index < list.getLength(); index++){
            if(list.item(index) instanceof CharacterData){
                CharacterData child  = (CharacterData) list.item(index);
                data = child.getData();

                if(data != null && data.trim().length() > 0)
                    return child.getData();
            }
        }
        return "";
    }

}
