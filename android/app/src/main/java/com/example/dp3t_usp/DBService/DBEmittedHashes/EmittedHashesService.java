package com.example.dp3t_usp.DBService.DBEmittedHashes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dp3t_usp.DBService.DBServiceInterface;

import java.util.ArrayList;

public class EmittedHashesService implements DBServiceInterface<EmittedHashesData> {
    private EmittedHashesHelper emittedHashesHelper;

    public EmittedHashesService(Context context){
        this.emittedHashesHelper = new EmittedHashesHelper(context);
    }

    @Override
    public void insertData(EmittedHashesData dbData) {
        SQLiteDatabase listenedHashesDB = this.emittedHashesHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EmittedHashesContract.COLUMN_EMITTED_HASH, dbData.getField(EmittedHashesContract.COLUMN_EMITTED_HASH).toString());
        values.put(EmittedHashesContract.COLUMN_DATE, dbData.getField(EmittedHashesContract.COLUMN_DATE).toString());

        listenedHashesDB.insert(EmittedHashesContract.TABLE_NAME, null, values);
    }

    @Override
    public void insertData(ArrayList<EmittedHashesData> dataList) {
        SQLiteDatabase listenedHashesDB = this.emittedHashesHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        EmittedHashesData data;
        while(dataList.iterator().hasNext()){
            data = dataList.iterator().next();
            values.put(EmittedHashesContract.COLUMN_EMITTED_HASH, data.getField(EmittedHashesContract.COLUMN_EMITTED_HASH).toString());
            values.put(EmittedHashesContract.COLUMN_DATE, data.getField(EmittedHashesContract.COLUMN_DATE).toString());

            listenedHashesDB.insert(EmittedHashesContract.TABLE_NAME, null, values);
        }
    }

    @Override
    public ArrayList<EmittedHashesData> getData() {
        SQLiteDatabase listenedHashesDB = this.emittedHashesHelper.getReadableDatabase();
        ArrayList<EmittedHashesData> listenedHashesData = new ArrayList<>();
        Cursor cursor = listenedHashesDB.rawQuery(EmittedHashesContract.SQL_GET_ENTRY,null);
        cursor.moveToFirst();
        int hashColumn = cursor.getColumnIndex(EmittedHashesContract.COLUMN_EMITTED_HASH);
        int dateColumn = cursor.getColumnIndex(EmittedHashesContract.COLUMN_DATE);
        Log.e("dbEmittedHashesContent", "Entries number" + cursor.getCount());
        while(!cursor.isAfterLast()){
            Log.e("dbEmittedHashesContent", "Entry " + cursor.getPosition() + ": " + cursor.getString(hashColumn) + " from " + cursor.getString(dateColumn));
            listenedHashesData.add(new EmittedHashesData(cursor.getString(hashColumn), cursor.getString(dateColumn)));
            cursor.moveToNext();
        }
        return listenedHashesData;
    }

    @Override
    public ArrayList<EmittedHashesData> getData(ArrayList<String> fields) {
        return null;
    }

    @Override
    public EmittedHashesData getData(String field, Object value) {
        SQLiteDatabase listenedHashesDB = this.emittedHashesHelper.getReadableDatabase();
        Cursor cursor = listenedHashesDB.rawQuery(EmittedHashesContract.getEntry(field, value.toString()), null);
        cursor.moveToFirst();
        int hashColumn = cursor.getColumnIndex(EmittedHashesContract.COLUMN_EMITTED_HASH);
        int dateColumn = cursor.getColumnIndex(EmittedHashesContract.COLUMN_DATE);
        EmittedHashesData listenedHashesData = new EmittedHashesData(cursor.getString(hashColumn), cursor.getString(dateColumn));
        return listenedHashesData;
    }

    @Override
    public void deleteData(String id) {
        SQLiteDatabase listenedHashesDB = this.emittedHashesHelper.getWritableDatabase();
        listenedHashesDB.delete(EmittedHashesContract.TABLE_NAME, EmittedHashesContract.COLUMN_EMITTED_HASH + "=" + id,null);
    }

    @Override
    public void deleteData(ArrayList<String> ids) {
        SQLiteDatabase listenedHashesDB = this.emittedHashesHelper.getWritableDatabase();
        while(ids.iterator().hasNext()){
            listenedHashesDB.delete(EmittedHashesContract.TABLE_NAME, EmittedHashesContract.COLUMN_EMITTED_HASH + "=" + ids.iterator().next(),null);
        }
    }

    @Override
    public void deleteAllData(){
        SQLiteDatabase listenedHashesDB = this.emittedHashesHelper.getWritableDatabase();
        listenedHashesDB.rawQuery(EmittedHashesContract.SQL_EMPTY_TABLE,null);
    }

    @Override
    public boolean isInDb(String hash){
        SQLiteDatabase db = emittedHashesHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(EmittedHashesContract.SQL_GET_ENTRY,null);
        cursor.moveToFirst();
        int hashColumn = cursor.getColumnIndex(EmittedHashesContract.COLUMN_EMITTED_HASH);
        Log.e("dbListenedHashesContent", "Entries number" + cursor.getCount());
        while(!cursor.isAfterLast()){
            if(hash == cursor.getString(hashColumn)){return true;}
            cursor.moveToNext();
        }
        return false;
    };
}
