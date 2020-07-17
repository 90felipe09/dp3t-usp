package com.example.dp3t_usp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LastExposureCheckHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LastExposureCheck.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + LastExposureCheckContract.LastExposureCheckEntry.TABLE_NAME + " ("
            + LastExposureCheckContract.LastExposureCheckEntry._ID + " INTEGER PRIMARY KEY,"
            + LastExposureCheckContract.LastExposureCheckEntry.LAST_CHECK + " TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + LastExposureCheckContract.LastExposureCheckEntry.TABLE_NAME;

    public LastExposureCheckHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }


}