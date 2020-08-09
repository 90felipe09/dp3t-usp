package com.example.dp3t_usp.DBService.DBLastCheck;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// Um helper é uma classe que gerencia um banco de dados. Ele cria, atualiza e destrói tabelas.
public class LastCheckHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "LastCheck.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + LastCheckContract.TABLE_NAME + " ("
            + LastCheckContract._ID + " INTEGER PRIMARY KEY,"
            + LastCheckContract.COLUMN_DATE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + LastCheckContract.TABLE_NAME;

    public LastCheckHelper(Context context){
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
