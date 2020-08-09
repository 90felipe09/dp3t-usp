package com.example.dp3t_usp.DBService.DBEmittedHashes;

import android.util.Log;

import com.example.dp3t_usp.DBService.DBData;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class EmittedHashesData extends DBData  {
    public static HashMap<String,Class> fieldsTypes = new HashMap<>();
    public HashMap<String,String> values = new HashMap<>();

    public EmittedHashesData(String hash){
        fieldsTypes.put(EmittedHashesContract.COLUMN_EMITTED_HASH, String.class);
        fieldsTypes.put(EmittedHashesContract.COLUMN_DATE, Date.class);
        setValues(hash);
    }

    public String getField(String field) {
        return values.get(field);
    }

    public EmittedHashesData(String hash, String date){
        fieldsTypes.put(EmittedHashesContract.COLUMN_EMITTED_HASH, String.class);
        fieldsTypes.put(EmittedHashesContract.COLUMN_DATE, Date.class);
        setValues(hash, date);
    }

    public void setValues(String hash){
        HashMap<String, String> newValues = new HashMap<>();
        newValues.put(EmittedHashesContract.COLUMN_EMITTED_HASH, hash);
        newValues.put(EmittedHashesContract.COLUMN_DATE, Calendar.getInstance().getTime().toString());
        this.values = newValues;
    }
    public void setValues(String hash, String date){
        HashMap<String, String> newValues = new HashMap<>();
        newValues.put(EmittedHashesContract.COLUMN_EMITTED_HASH, hash);
        newValues.put(EmittedHashesContract.COLUMN_DATE, date);
        this.values = newValues;
    }

}
