package com.example.dp3t_usp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InfectedHashesHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "InfectedHashes.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + InfectedHashesContract.InfectedHashEntry.TABLE_NAME + " ("
            + InfectedHashesContract.InfectedHashEntry._ID + " INTEGER PRIMARY KEY,"
            + InfectedHashesContract.InfectedHashEntry.COLUMN_INFECTED_HASH + " TEXT,"
            + InfectedHashesContract.InfectedHashEntry.COLUMN_DATE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + InfectedHashesContract.InfectedHashEntry.TABLE_NAME;

    public InfectedHashesHelper(Context context){
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