package com.alexandr.lostfilm.database;

/**
 * Created by alexandr on 02/08/16.
 *

 * POJO object, that describes all information about serial
 */
public class AllSerials {

    private String link;
    private String ruName;
    private String engName;
    private String img_big;
    private String img_small;
    private String episode;
    private String date;

    public AllSerials(String link, String ruName, String engName,
                      String img_big, String img_small, String episode, String date) {
        this.link = link;
        this.ruName = ruName;
        this.engName = engName;
        this.img_big = img_big;
        this.img_small = img_small;
        this.episode = episode;
        this.date = date;
    }

    public String getImg_big() {
        return img_big;
    }

    public void setImg_big(String img_big) {
        this.img_big = img_big;
    }

    public String getImg_small() {
        return img_small;
    }

    public void setImg_small(String img_small) {
        this.img_small = img_small;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // leave this constructor for back compability, later will be deleted
    public AllSerials(String link, String ruName, String engName) {
        this.link = link;
        this.ruName = ruName;
        this.engName = engName;

    }



    public String getLink() {
        return link;
    }

    public String getRuName() {
        return ruName;
    }

    public String getEngName() {
        return engName;
    }
}
