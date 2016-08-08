package com.alexandr.lostfilm.database;

/**
 * Created by alexandr on 02/08/16.
 */
public class AllSerials {

    private String link;
    private String ruName;
    private String engName;

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
