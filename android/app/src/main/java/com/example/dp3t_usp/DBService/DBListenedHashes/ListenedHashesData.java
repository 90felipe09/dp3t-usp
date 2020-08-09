package com.example.dp3t_usp.DBService.DBListenedHashes;

import com.example.dp3t_usp.DBService.DBData;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ListenedHashesData extends DBData  {
    public static HashMap<String,Class> fieldsTypes = new HashMap<>();
    public HashMap<String,String> values = new HashMap<>();

    public ListenedHashesData(String hash){
        fieldsTypes.put(ListenedHashesContract.COLUMN_LISTENED_HASH, String.class);
        fieldsTypes.put(ListenedHashesContract.COLUMN_DATE, Date.class);
        setValues(hash);
    }

    public ListenedHashesData(String hash, String date){
        fieldsTypes.put(ListenedHashesContract.COLUMN_LISTENED_HASH, String.class);
        fieldsTypes.put(ListenedHashesContract.COLUMN_DATE, Date.class);
        setValues(hash, date);
    }

    public void setValues(String hash){
        HashMap<String, String> newValues = new HashMap<>();
        newValues.put(ListenedHashesContract.COLUMN_LISTENED_HASH, hash);
        newValues.put(ListenedHashesContract.COLUMN_DATE, Calendar.getInstance().getTime().toString());
        this.values = newValues;
    }
    public void setValues(String hash, String date){
        HashMap<String, String> newValues = new HashMap<>();
        newValues.put(ListenedHashesContract.COLUMN_LISTENED_HASH, hash);
        newValues.put(ListenedHashesContract.COLUMN_DATE, date);
        this.values = newValues;
    }

}
