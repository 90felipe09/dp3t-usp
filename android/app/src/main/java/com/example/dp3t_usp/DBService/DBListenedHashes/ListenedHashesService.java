package com.example.dp3t_usp.DBService.DBListenedHashes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.dp3t_usp.DBService.DBServiceInterface;

import java.util.ArrayList;

public class ListenedHashesService implements DBServiceInterface<ListenedHashesData> {
    private ListenedHashesHelper listenedHashesHelper;

    public ListenedHashesService(Context context){
        this.listenedHashesHelper = new ListenedHashesHelper(context);
    }

    @Override
    public void insertData(ListenedHashesData dbData) {
        SQLiteDatabase listenedHashesDB = this.listenedHashesHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListenedHashesContract.COLUMN_LISTENED_HASH, dbData.getField(ListenedHashesContract.COLUMN_LISTENED_HASH).toString());
        values.put(ListenedHashesContract.COLUMN_DATE, dbData.getField(ListenedHashesContract.COLUMN_DATE).toString());

        listenedHashesDB.insert(ListenedHashesContract.TABLE_NAME, null, values);
    }

    @Override
    public void insertData(ArrayList<ListenedHashesData> dataList) {
        SQLiteDatabase listenedHashesDB = this.listenedHashesHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        ListenedHashesData data;
        while(dataList.iterator().hasNext()){
            data = dataList.iterator().next();
            values.put(ListenedHashesContract.COLUMN_LISTENED_HASH, data.getField(ListenedHashesContract.COLUMN_LISTENED_HASH).toString());
            values.put(ListenedHashesContract.COLUMN_DATE, data.getField(ListenedHashesContract.COLUMN_DATE).toString());

            listenedHashesDB.insert(ListenedHashesContract.TABLE_NAME, null, values);
        }
    }

    @Override
    public ArrayList<ListenedHashesData> getData() {
        SQLiteDatabase listenedHashesDB = this.listenedHashesHelper.getReadableDatabase();
        ArrayList<ListenedHashesData> listenedHashesData = new ArrayList<>();
        Cursor cursor = listenedHashesDB.rawQuery(ListenedHashesContract.SQL_GET_ENTRY,null);
        cursor.moveToFirst();
        int hashColumn = cursor.getColumnIndex(ListenedHashesContract.COLUMN_LISTENED_HASH);
        int dateColumn = cursor.getColumnIndex(ListenedHashesContract.COLUMN_DATE);
        Log.e("dbListenedHashesContent", "Entries number" + cursor.getCount());
        while(!cursor.isAfterLast()){
            Log.e("dbListenedHashesContent", "Entry " + cursor.getPosition() + ": " + cursor.getString(hashColumn) + " from " + cursor.getString(dateColumn));
            listenedHashesData.add(new ListenedHashesData(cursor.getString(hashColumn), cursor.getString(dateColumn)));
            cursor.moveToNext();
        }
        return listenedHashesData;
    }

    @Override
    public ArrayList<ListenedHashesData> getData(ArrayList<String> fields) {
        return null;
    }

    @Override
    public ListenedHashesData getData(String field, Object value) {
        SQLiteDatabase listenedHashesDB = this.listenedHashesHelper.getReadableDatabase();
        Cursor cursor = listenedHashesDB.rawQuery(ListenedHashesContract.getEntry(field, value.toString()), null);
        cursor.moveToFirst();
        int hashColumn = cursor.getColumnIndex(ListenedHashesContract.COLUMN_LISTENED_HASH);
        int dateColumn = cursor.getColumnIndex(ListenedHashesContract.COLUMN_DATE);
        ListenedHashesData listenedHashesData = new ListenedHashesData(cursor.getString(hashColumn), cursor.getString(dateColumn));
        return listenedHashesData;
    }

    @Override
    public void deleteData(String id) {
        SQLiteDatabase listenedHashesDB = this.listenedHashesHelper.getWritableDatabase();
        listenedHashesDB.delete(ListenedHashesContract.TABLE_NAME, ListenedHashesContract.COLUMN_LISTENED_HASH + "=" + id,null);
    }

    @Override
    public void deleteData(ArrayList<String> ids) {
        SQLiteDatabase listenedHashesDB = this.listenedHashesHelper.getWritableDatabase();
        while(ids.iterator().hasNext()){
            listenedHashesDB.delete(ListenedHashesContract.TABLE_NAME, ListenedHashesContract.COLUMN_LISTENED_HASH + "=" + ids.iterator().next(),null);
        }
    }

    @Override
    public void deleteAllData(){
        SQLiteDatabase listenedHashesDB = this.listenedHashesHelper.getWritableDatabase();
        listenedHashesDB.rawQuery(ListenedHashesContract.SQL_EMPTY_TABLE,null);
    }

    @Override
    public boolean isInDb(String hash){
        SQLiteDatabase db = listenedHashesHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ListenedHashesContract.SQL_GET_ENTRY,null);
        cursor.moveToFirst();
        int hashColumn = cursor.getColumnIndex(ListenedHashesContract.COLUMN_LISTENED_HASH);
        Log.e("dbListenedHashesContent", "Entries number" + cursor.getCount());
        while(!cursor.isAfterLast()){
            if(hash == cursor.getString(hashColumn)){return true;}
            cursor.moveToNext();
        }
        return false;
    };
}
