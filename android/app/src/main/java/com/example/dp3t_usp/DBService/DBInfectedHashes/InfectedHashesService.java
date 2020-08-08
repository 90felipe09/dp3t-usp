package com.example.dp3t_usp.DBService.DBInfectedHashes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dp3t_usp.DBService.DBServiceInterface;

import java.util.ArrayList;

public class InfectedHashesService implements DBServiceInterface<InfectedHashesData> {
    private InfectedHashesHelper infectedHashesHelper;

    public InfectedHashesService(Context context){
        this.infectedHashesHelper = new InfectedHashesHelper(context);
    }

    @Override
    public void insertData(InfectedHashesData dbData) {
        SQLiteDatabase infectedHashesDB = this.infectedHashesHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfectedHashesContract.COLUMN_INFECTED_HASH, dbData.getField(InfectedHashesContract.COLUMN_INFECTED_HASH).toString());
        values.put(InfectedHashesContract.COLUMN_DATE, dbData.getField(InfectedHashesContract.COLUMN_DATE).toString());

        infectedHashesDB.insert(InfectedHashesContract.TABLE_NAME, null, values);
    }

    @Override
    public void insertData(ArrayList<InfectedHashesData> dataList) {
        SQLiteDatabase infectedHashesDB = this.infectedHashesHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        InfectedHashesData data;
        while(dataList.iterator().hasNext()){
            data = dataList.iterator().next();
            values.put(InfectedHashesContract.COLUMN_INFECTED_HASH, data.getField(InfectedHashesContract.COLUMN_INFECTED_HASH).toString());
            values.put(InfectedHashesContract.COLUMN_DATE, data.getField(InfectedHashesContract.COLUMN_DATE).toString());

            infectedHashesDB.insert(InfectedHashesContract.TABLE_NAME, null, values);
        }
    }

    @Override
    public ArrayList<InfectedHashesData> getData() {
        SQLiteDatabase infectedHashesDB = this.infectedHashesHelper.getReadableDatabase();
        ArrayList<InfectedHashesData> listenedHashesData = new ArrayList<>();
        Cursor cursor = infectedHashesDB.rawQuery(InfectedHashesContract.SQL_GET_ENTRY,null);
        cursor.moveToFirst();
        int hashColumn = cursor.getColumnIndex(InfectedHashesContract.COLUMN_INFECTED_HASH);
        int dateColumn = cursor.getColumnIndex(InfectedHashesContract.COLUMN_DATE);
        Log.e("dbListenedHashesContent", "Entries number" + cursor.getCount());
        while(!cursor.isAfterLast()){
            Log.e("dbListenedHashesContent", "Entry " + cursor.getPosition() + ": " + cursor.getString(hashColumn) + " from " + cursor.getString(dateColumn));
            listenedHashesData.add(new InfectedHashesData(cursor.getString(hashColumn), cursor.getString(dateColumn)));
            cursor.moveToNext();
        }
        return listenedHashesData;
    }

    @Override
    public ArrayList<InfectedHashesData> getData(ArrayList<String> fields) {
        return null;
    }

    @Override
    public InfectedHashesData getData(String field, Object value) {
        SQLiteDatabase infectedHashesDB = this.infectedHashesHelper.getReadableDatabase();
        Cursor cursor = infectedHashesDB.rawQuery(InfectedHashesContract.getEntry(field, value.toString()), null);
        cursor.moveToFirst();
        int hashColumn = cursor.getColumnIndex(InfectedHashesContract.COLUMN_INFECTED_HASH);
        int dateColumn = cursor.getColumnIndex(InfectedHashesContract.COLUMN_DATE);
        InfectedHashesData listenedHashesData = new InfectedHashesData(cursor.getString(hashColumn), cursor.getString(dateColumn));
        return listenedHashesData;
    }

    @Override
    public void deleteData(String id) {
        SQLiteDatabase infectedHashesDB = this.infectedHashesHelper.getWritableDatabase();
        infectedHashesDB.delete(InfectedHashesContract.TABLE_NAME, InfectedHashesContract.COLUMN_INFECTED_HASH + "=" + id,null);
    }

    @Override
    public void deleteData(ArrayList<String> ids) {
        SQLiteDatabase infectedHashesDB = this.infectedHashesHelper.getWritableDatabase();
        while(ids.iterator().hasNext()){
            infectedHashesDB.delete(InfectedHashesContract.TABLE_NAME, InfectedHashesContract.COLUMN_INFECTED_HASH + "=" + ids.iterator().next(),null);
        }
    }

    @Override
    public void deleteAllData(){
        SQLiteDatabase infectedHashesDB = this.infectedHashesHelper.getWritableDatabase();
        infectedHashesDB.rawQuery(InfectedHashesContract.SQL_EMPTY_TABLE,null);
    }

    @Override
    public boolean isInDb(String hash){
        SQLiteDatabase db = infectedHashesHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(InfectedHashesContract.SQL_GET_ENTRY,null);
        cursor.moveToFirst();
        int hashColumn = cursor.getColumnIndex(InfectedHashesContract.COLUMN_INFECTED_HASH);
        Log.e("dbListenedHashesContent", "Entries number" + cursor.getCount());
        while(!cursor.isAfterLast()){
            if(hash == cursor.getString(hashColumn)){return true;}
            cursor.moveToNext();
        }
        return false;
    };
}
