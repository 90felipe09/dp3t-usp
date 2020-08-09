package com.example.dp3t_usp.DBService.DBLastCheck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dp3t_usp.DBService.DBInfectedHashes.InfectedHashesContract;
import com.example.dp3t_usp.DBService.DBInfectedHashes.InfectedHashesData;
import com.example.dp3t_usp.DBService.DBServiceInterface;

import java.util.ArrayList;

public class LastCheckService implements DBServiceInterface<LastCheckData> {
    private LastCheckHelper lastCheckHelper;

    public LastCheckService(Context context){
        this.lastCheckHelper = new LastCheckHelper(context);
    }

    @Override
    public void insertData(LastCheckData dbData) {
        SQLiteDatabase lastCheckDB = this.lastCheckHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LastCheckContract.COLUMN_DATE, dbData.getField(LastCheckContract.COLUMN_DATE));
        this.deleteAllData();
        lastCheckDB.insert(LastCheckContract.TABLE_NAME, null, values);
    }

    @Override
    public void insertData(ArrayList<LastCheckData> dataList) {
        return;
    }


    @Override
    public ArrayList<LastCheckData> getData() {
        SQLiteDatabase lastCheckDB = this.lastCheckHelper.getReadableDatabase();
        ArrayList<LastCheckData> lastCheckData = new ArrayList<>();
        Cursor cursor = lastCheckDB.rawQuery(LastCheckContract.SQL_GET_ENTRY,null);
        cursor.moveToFirst();
        if(cursor.getCount() == 0){
            return null;
        }
        int dateColumn = cursor.getColumnIndex(LastCheckContract.COLUMN_DATE);
        Log.e("dbLastCheckContent", "Entries number" + cursor.getCount());
        Log.e("dbLastCheckContent", "Entry " + cursor.getPosition() + ": from " + cursor.getString(dateColumn));
        lastCheckData.add(new LastCheckData(cursor.getString(dateColumn)));
        cursor.moveToNext();

        return lastCheckData;
    }

    @Override
    public ArrayList<LastCheckData> getData(ArrayList<String> fields) {
        return null;
    }

    @Override
    public LastCheckData getData(String field, Object value) {
        return null;
    }

    @Override
    public void deleteData(String id) {
        return;
    }

    @Override
    public void deleteData(ArrayList<String> ids) {
        return;
    }

    @Override
    public void deleteAllData(){
        SQLiteDatabase infectedHashesDB = this.lastCheckHelper.getWritableDatabase();
        infectedHashesDB.rawQuery(LastCheckContract.SQL_EMPTY_TABLE,null);
    }

    @Override
    public boolean isInDb(String date){
        return false;
    };
}
