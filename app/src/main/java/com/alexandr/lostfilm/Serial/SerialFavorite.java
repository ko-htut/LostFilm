package com.alexandr.lostfilm.Serial;

/**
 * Created by alexandr on 22/05/16.
 */
public class SerialFavorite {
    int id;
    int image;
    String name;
    String date;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public SerialFavorite(int id, int image, String name, String date) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.date = date;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
