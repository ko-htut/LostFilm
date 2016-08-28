package com.alexandr.lostfilm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DB {


    private static final String DB_NAME = "lostfilm_db";
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


    private static final String DB_CREATE_SERIALS =
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

    public void openReadOnly() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getReadableDatabase();
    }

    public void openWritable() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper != null) mDBHelper.close();
        if (mDB != null)        mDB.close();
    }


    // получить все данные из таблицы DB_TABLE
    public Cursor getAllSerials() {
        System.out.println(mDB.toString());
        return mDB.query(DB_TABLE_SERIALS, null, "isFavorite=0", null, null, null, null);
    }


    // should be deleted from here
    public Cursor getReallyAllSerials() {
//        System.out.println(mDB.toString());
        return mDB.query(DB_TABLE_SERIALS, null, null, null, null, null, null);
    }
    //till here

    public Cursor getFavSerials() {
        System.out.println(mDB.toString());
        return mDB.query(DB_TABLE_SERIALS, null, "isFavorite!=0", null, null, null, null);
    }



    public void addToFav(String ruName) {
        // подготовим значения для обновления
        ContentValues cv = new ContentValues();
        cv.put(ALL_COLUMN_IS_FAVORITE, 1);
        // обновляем по ruName
        int updCount = mDB.update(DB_TABLE_SERIALS, cv, ALL_COLUMN_RU_NAME + " = ?",
                new String[]{ruName});
        Log.d("debugUpdateDB", "addToFav, updated rows count = " + updCount);
    }

    public void delFromFav(String ruName) {
        // подготовим значения для обновления
        ContentValues cv = new ContentValues();
        cv.put(ALL_COLUMN_IS_FAVORITE, 0);
        // обновляем по ruName
        int updCount = mDB.update(DB_TABLE_SERIALS, cv, ALL_COLUMN_RU_NAME + " = ?",
                new String[]{ruName});
        Log.d("debugUpdateDB", "delFromFav, updated rows count = " + updCount);
    }


    // добавить запись в DB_TABLE SERIALS
    public void addToAll(String link, String ruName, String engName, String status,
                         String bigPicture, String smallPicture, String date,
                         String lastEpisode, String descr_ru, String descr_eng, int isFavorite) {
        ContentValues cv = new ContentValues();
        cv.put(ALL_COLUMN_LINK, link);
        cv.put(ALL_COLUMN_RU_NAME, ruName);
        cv.put(ALL_COLUMN_ENG_NAME, engName);
        cv.put(ALL_COLUMN_STATUS, status);
        cv.put(ALL_COLUMN_BIG_PICTURE, bigPicture);
        cv.put(ALL_COLUMN_SMALL_PICTURE, smallPicture);
        cv.put(ALL_COLUMN_DATE, date);
        cv.put(ALL_COLUMN_LAST_EPISODE, lastEpisode);
        cv.put(ALL_COLUMN_DETAIL_ENG, descr_eng);
        cv.put(ALL_COLUMN_DETAIL_RU, descr_ru);
        cv.put(ALL_COLUMN_IS_FAVORITE, isFavorite);
        mDB.insert(DB_TABLE_SERIALS, null, cv);
    }

    // удалить запись из DB_TABLE ALL
    public void delFromAll(String ruName) {
        mDB.delete(DB_TABLE_SERIALS, ALL_COLUMN_RU_NAME + " = " + ruName, null);
    }

    public void createDB()
    {
        DBHelper dbHelp = new DBHelper(mCtx,DB_NAME,null,DB_VERSION);
        try
        {
            dbHelp.createDataBase();
            dbHelp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        private String DB_PATH;
        private Context ctx;


        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
            if (android.os.Build.VERSION.SDK_INT >= 4.2) {
                DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
            } else {
                DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
            }
            ctx=context;
        }

        public void createDataBase() throws IOException {

            // If database not exists copy it from the assets

            boolean mDataBaseExist = checkDataBase();
            Log.i("debug_checkDB",String.valueOf(mDataBaseExist));
            if (!mDataBaseExist) {
                this.getReadableDatabase();
            //    this.close();

                try {
                    // Copy the database from assests
                    copyDataBase();
                    Log.i("DataBaseHelper", "createDatabase database created");
                } catch (IOException mIOException) {
                    throw new Error("Error Copying DataBase");
                }
            }
        }

        private boolean checkDataBase() {
            File dbFile = new File(DB_PATH + DB_NAME);
            Log.i("debug_db",dbFile.getAbsolutePath());
            Log.i("debug_db",String.valueOf(dbFile.exists()));

            return dbFile.exists();
        }

        private void copyDataBase() throws IOException {
            Log.i("debug_db","copy database called");
            InputStream mInput = ctx.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            Log.i("debug_db outFile",outFileName);
            //OutputStream mOutput = new FileOutputStream(outFileName);

            /*byte[] mBuffer = new byte[64];
            int mLength;
            while ((mLength = mInput.read(mBuffer)) > 0) {
                mOutput.write(mBuffer, 0, mLength);
            }*/
            FileOutputStream mOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[64];
            int len = mInput.read(buffer);
            while (len != -1) {
                mOutput.write(buffer);
                len = mInput.read(buffer);
                if (Thread.interrupted()) {
                    try
                    {
                        throw new InterruptedException();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            mOutput.flush();
            mOutput.close();
            mInput.close();
            Log.i("debug db","copied");
        }






        // создаем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }



    }
}


