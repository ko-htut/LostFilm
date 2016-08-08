package com.alexandr.lostfilm.database;

/**
 * Created by alexandr on 02/08/16.
 */
public class FavSerials {
    String season;
    String name;
    String pic_link;
    String descr;
    String date;

    public FavSerials(String season, String name, String pic_link, String descr, String date) {
        this.season = season;
        this.name = name;
        this.pic_link = pic_link;
        this.descr = descr;
        this.date = date;
    }

    public String getSeason() {
        return season;
    }

    public String getName() {
        return name;
    }

    public String getPic_link() {
        return pic_link;
    }

    public String getDescr() {
        return descr;
    }

    public String getDate() {
        return date;
    }


}
