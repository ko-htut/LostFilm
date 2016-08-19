package com.alexandr.lostfilm.database;

/**
 * Created by alexandr on 02/08/16.
 */
public class FavSerials {
    String season;
    String name;
    String pic_link;
    String descr_ru;
    String descr_eng;
    String date;


    public FavSerials(String season, String name, String pic_link, String descr_ru, String descr_eng, String date) {
        this.season = season;
        this.name = name;
        this.pic_link = pic_link;
        this.descr_ru = descr_ru;
        this.descr_eng = descr_eng;
        this.date = date;
    }

    public String getSeason() {

        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic_link() {
        return pic_link;
    }

    public void setPic_link(String pic_link) {
        this.pic_link = pic_link;
    }

    public String getDescr_ru() {
        return descr_ru;
    }

    public void setDescr_ru(String descr_ru) {
        this.descr_ru = descr_ru;
    }

    public String getDescr_eng() {
        return descr_eng;
    }

    public void setDescr_eng(String descr_eng) {
        this.descr_eng = descr_eng;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
