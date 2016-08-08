package com.alexandr.lostfilm.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.alexandr.lostfilm.database.DB;


public class AllCursorLoader extends CursorLoader {
    DB db;

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = db.getAllSerials();
        return cursor;
    }



    public AllCursorLoader(Context context, DB db) {
        super(context);
        this.db=db;
        this.db.open();
    }
}
