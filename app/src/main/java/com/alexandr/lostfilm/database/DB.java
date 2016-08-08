package com.alexandr.lostfilm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alexandr on 04/08/16.
 */
public class DB {

    private static final String DB_NAME = "LostFilmDB";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE_ALL = "SerialAll";
    private static final String DB_TABLE_FAV = "SerialFavorite";


    public static final String ALL_COLUMN_ID = "_id";
    public static final String ALL_COLUMN_LINK = "link";
    public static final String ALL_COLUMN_RU_NAME = "ruName";
    public static final String ALL_COLUMN_ENG_NAME = "engName";

    public static final String FAV_COLUMN_ID = "_id";
    public static final String FAV_COLUMN_S_EP = "s_ep";
    public static final String FAV_COLUMN_RU_NAME = "ruName";
    public static final String FAV_COLUMN_PIC_LINK = "pic_link";
    public static final String FAV_COLUMN_DESCRIPTION = "descr";
    public static final String FAV_COLUMN_DATE = "date";


    private static final String DB_CREATE_FAV =
            "create table " + DB_TABLE_FAV + "(" +
                    FAV_COLUMN_ID + " integer primary key autoincrement, " +
                    FAV_COLUMN_RU_NAME + " text UNIQUE ON CONFLICT IGNORE, " +
                    FAV_COLUMN_S_EP + " text," +
                    FAV_COLUMN_DESCRIPTION + " text," +
                    FAV_COLUMN_PIC_LINK + " text," +
                    FAV_COLUMN_DATE + " text" +
                    ");";

    private static final String DB_CREATE_ALL =
            "create table " + DB_TABLE_ALL + "(" +
                    ALL_COLUMN_ID + " integer primary key autoincrement, " +
                    ALL_COLUMN_LINK + " text, " +
                    ALL_COLUMN_RU_NAME + " text UNIQUE ON CONFLICT IGNORE," +
                    ALL_COLUMN_ENG_NAME + " text " +
                    ");";

    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }


    // получить все данные из таблицы DB_TABLE
    public Cursor getAllSerials() {
        System.out.println(mDB.toString());
        return mDB.query(DB_TABLE_ALL, null, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE ALL
    public void addToAll(String link, String ruName , String engName) {
        ContentValues cv = new ContentValues();
        cv.put(ALL_COLUMN_LINK, link);
        cv.put(ALL_COLUMN_RU_NAME, ruName);
        cv.put(ALL_COLUMN_ENG_NAME, engName);
        mDB.insert(DB_TABLE_ALL, null, cv);
    }

    // удалить запись из DB_TABLE ALL
    public void delFromAll(String  ruName) {
        mDB.delete(DB_TABLE_ALL, ALL_COLUMN_RU_NAME + " = " + ruName, null);
    }

    // добавить запись в DB_TABLE FAV
    public void addToFav(String ruName , String s_ep,String descr, String pic_link, String date) {
        ContentValues cv = new ContentValues();
        cv.put(ALL_COLUMN_RU_NAME, ruName);
        mDB.insert(DB_TABLE_ALL, null, cv);
    }

    // удалить запись из DB_TABLE FAV
    public void delFromFav(String  ruName) {
        mDB.delete(DB_TABLE_FAV, FAV_COLUMN_RU_NAME + " = " + ruName, null);
    }





    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_ALL);
            db.execSQL(DB_CREATE_FAV);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

}