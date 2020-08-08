package com.example.dp3t_usp.DBService.DBListenedHashes;

import com.example.dp3t_usp.DBService.DBData;

import java.util.Date;
import java.util.HashMap;

public class ListenedHashesData extends DBData  {
    public static HashMap<String,Class> fieldsTypes = new HashMap<>();
    public HashMap<String,Object> values = new HashMap<>();

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
        HashMap<String, Object> newValues = new HashMap<>();
        newValues.put(ListenedHashesContract.COLUMN_LISTENED_HASH, hash);
        newValues.put(ListenedHashesContract.COLUMN_DATE, new Date());
        this.values = newValues;
    }
    public void setValues(String hash, String date){
        HashMap<String, Object> newValues = new HashMap<>();
        newValues.put(ListenedHashesContract.COLUMN_LISTENED_HASH, hash);
        newValues.put(ListenedHashesContract.COLUMN_DATE, date);
        this.values = newValues;
    }

}
