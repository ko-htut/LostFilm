package com.alexandr.lostfilm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;


public class DB {


    private static final String DB_NAME = "LostFilmDB";
    private static final int DB_VERSION = 1;

    private static final String DB_TABLE_SERIALS = "Serials";



    public static final String ALL_COLUMN_ID = "_id";
    public static final String ALL_COLUMN_LINK = "link";
    public static final String ALL_COLUMN_RU_NAME = "ruName";
    public static final String ALL_COLUMN_ENG_NAME = "engName";
    public static final String ALL_COLUMN_STATUS = "status";
    public static final String ALL_COLUMN_BIG_PICTURE = "bigPicture";
    public static final String ALL_COLUMN_SMALL_PICTURE = "smallPicture";
    public static final String ALL_COLUMN_DATE = "date";
    public static final String ALL_COLUMN_LAST_EPISODE = "episode";
    public static final String ALL_COLUMN_IS_FAVORITE = "isFavorite";
    public static final String ALL_COLUMN_DETAIL_RU = "descr_ru";
    public static final String ALL_COLUMN_DETAIL_ENG = "descr_eng";




    private static final String DB_CREATE_SERIALS=
            "create table " + DB_TABLE_SERIALS + "(" +
                    ALL_COLUMN_ID + " integer primary key autoincrement, " +
                    ALL_COLUMN_LINK + " text, " +
                    ALL_COLUMN_RU_NAME + " text UNIQUE ON CONFLICT IGNORE," +
                    ALL_COLUMN_ENG_NAME + " text, " +
                    ALL_COLUMN_STATUS + " text, " +
                    ALL_COLUMN_BIG_PICTURE + " text, " +
                    ALL_COLUMN_SMALL_PICTURE + " text, " +
                    ALL_COLUMN_DATE + " text, " +
                    ALL_COLUMN_LAST_EPISODE + " text," +
                    ALL_COLUMN_DETAIL_ENG + " text," +
                    ALL_COLUMN_DETAIL_RU + " text," +
                    ALL_COLUMN_IS_FAVORITE + " integer " +
                    ");";



    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {

        Log.i("DBDEBUG",mCtx.toString());
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
        return mDB.query(DB_TABLE_SERIALS, null, "isFavorite=0", null, null, null, null);
    }

    public Cursor getFavSerials() {
        System.out.println(mDB.toString());
        return mDB.query(DB_TABLE_SERIALS, null, "isFavorite!=0", null, null, null, null);
    }

    // добавить запись в DB_TABLE SERIALS
    public void addToAll(String link, String ruName , String engName) {
        ContentValues cv = new ContentValues();
        cv.put(ALL_COLUMN_LINK, link);
        cv.put(ALL_COLUMN_RU_NAME, ruName);
        cv.put(ALL_COLUMN_ENG_NAME, engName);
        mDB.insert(DB_TABLE_SERIALS, null, cv);
    }

    public void addToFav(String ruName)
    {
        // подготовим значения для обновления
        ContentValues cv = new ContentValues();
        cv.put(ALL_COLUMN_IS_FAVORITE, 1);
        // обновляем по ruName
        int updCount = mDB.update(DB_TABLE_SERIALS, cv, ALL_COLUMN_RU_NAME+" = ?",
                new String[] { ruName });
        Log.d("debugUpdateDB", "addToFav, updated rows count = " + updCount);
    }

    public void delFromFav(String ruName)
    {
        // подготовим значения для обновления
        ContentValues cv = new ContentValues();
        cv.put(ALL_COLUMN_IS_FAVORITE, 0);
        // обновляем по ruName
        int updCount = mDB.update(DB_TABLE_SERIALS, cv, ALL_COLUMN_RU_NAME+" = ?",
                new String[] { ruName });
        Log.d("debugUpdateDB", "delFromFav, updated rows count = " + updCount);
    }


    // добавить запись в DB_TABLE SERIALS
    public void addToAll(String link, String ruName , String engName, String status,
                         String bigPicture, String smallPicture, String date,
                         String lastEpisode,String descr_ru, String descr_eng, int isFavorite)
    {
        ContentValues cv = new ContentValues();
        cv.put(ALL_COLUMN_LINK, link);
        cv.put(ALL_COLUMN_RU_NAME, ruName);
        cv.put(ALL_COLUMN_ENG_NAME, engName);
        cv.put(ALL_COLUMN_STATUS,status);
        cv.put(ALL_COLUMN_BIG_PICTURE,bigPicture);
        cv.put(ALL_COLUMN_SMALL_PICTURE,smallPicture);
        cv.put(ALL_COLUMN_DATE,date);
        cv.put(ALL_COLUMN_LAST_EPISODE,lastEpisode);
        cv.put(ALL_COLUMN_DETAIL_ENG,descr_eng);
        cv.put(ALL_COLUMN_DETAIL_RU, descr_ru);
        cv.put(ALL_COLUMN_IS_FAVORITE,isFavorite);
        mDB.insert(DB_TABLE_SERIALS, null, cv);
    }

    // удалить запись из DB_TABLE ALL
    public void delFromAll(String  ruName) {
        mDB.delete(DB_TABLE_SERIALS, ALL_COLUMN_RU_NAME + " = " + ruName, null);
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
            //db.execSQL(DB_CREATE_ALL);
            //db.execSQL(DB_CREATE_FAV);
            db.execSQL(DB_CREATE_SERIALS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

}
