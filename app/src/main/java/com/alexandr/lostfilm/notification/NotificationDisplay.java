package com.alexandr.lostfilm.notification;

import android.app.Notification;

import com.bumptech.glide.request.target.NotificationTarget;

/**
 * Created by alexandr on 13/09/16.
 */
public class NotificationDisplay {
    NotificationTarget target;
    Notification notif;
    int id;

    public NotificationDisplay(NotificationTarget target, Notification notif, int id, String img) {
        this.target = target;
        this.notif = notif;
        this.id = id;
        this.img = img;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String img;

    public NotificationDisplay(NotificationTarget target, Notification notif, String img) {
        this.target = target;
        this.notif = notif;
        this.img = img;
    }

    public NotificationTarget getTarget() {
        return target;
    }

    public void setTarget(NotificationTarget target) {
        this.target = target;
    }

    public Notification getNotif() {
        return notif;
    }

    public void setNotif(Notification notif) {
        this.notif = notif;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
