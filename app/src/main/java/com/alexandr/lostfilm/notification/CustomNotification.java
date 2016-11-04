package com.alexandr.lostfilm.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.alexandr.lostfilm.MainActivity;
import com.alexandr.lostfilm.database.DB;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.example.alexandr.lostfilm.R;

import java.util.Random;

/**
 * Created by alexandr on 05/09/16.
 */
public class CustomNotification {
    Context mCtx;
    Notification nf;
    public CustomNotification(Context ctx)
    {
        mCtx=ctx;
    }


    public NotificationDisplay newNotification(Context context,String ruName,boolean isEpisode)
    {
        mCtx=context;
        NotificationCompat.Builder mBuilder;
        DB db = new DB(mCtx);
        db.openReadOnly();
        Cursor serial=db.getSerial(ruName);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final RemoteViews rv = new RemoteViews(mCtx.getPackageName(), R.layout.notification_layout);
        int colRuName = serial.getColumnIndex(DB.ALL_COLUMN_RU_NAME);
        int colEpisode = serial.getColumnIndex(DB.ALL_COLUMN_LAST_EPISODE);
        int colImg = serial.getColumnIndex(DB.ALL_COLUMN_SMALL_PICTURE);
        if (isEpisode)
        {
            if (serial.moveToFirst()) {
                rv.setImageViewUri(R.id.remoteview_notification_icon, Uri.parse(serial.getString(colImg)));
                rv.setTextViewText(R.id.remoteview_notification_headline, serial.getString(colRuName));
                rv.setTextViewText(R.id.remoteview_notification_short_message, "New episode: "+serial.getString(colEpisode));;
                mBuilder =
                        new NotificationCompat.Builder(mCtx)
                                .setContentTitle(serial.getString(colRuName))
                                .setContentText("New episode: "+serial.getString(colEpisode))
                                .setContent(rv)
                                .setSmallIcon(R.drawable.lostfilm_favico_png)
                                .setSound(alarmSound);
                nf=mBuilder.build();
            }
        }
        else
        {
            if (serial.moveToFirst()) {
                rv.setImageViewUri(R.id.remoteview_notification_icon, Uri.parse(serial.getString(colImg)));
                rv.setTextViewText(R.id.remoteview_notification_headline, serial.getString(colRuName));
                rv.setTextViewText(R.id.remoteview_notification_short_message, "New Serial: "+serial.getString(colRuName));

                Intent intent = new Intent(mCtx, MainActivity.class);
                final PendingIntent pendingIntent = PendingIntent.getActivity(mCtx.getApplicationContext(), 0, intent, 0);

                mBuilder =
                        new NotificationCompat.Builder(mCtx)
                                .setContentTitle("New serial!")
                                .setContentText(serial.getString(colRuName))
                                .setContent(rv)
                                .setSmallIcon(R.drawable.lostfilm_favico_png)
                                .setSound(alarmSound)
                                .setContentIntent(pendingIntent);

                nf=mBuilder.build();
            }

        }
        Random rnd = new Random();
        int id = rnd.nextInt();
        NotificationTarget  notificationTarget
                = new NotificationTarget(mCtx,rv,R.id.remoteview_notification_icon,nf,id);
        db.close();
        return new NotificationDisplay(notificationTarget,nf,id,serial.getString(colImg));
    }


}
