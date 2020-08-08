package com.example.dp3t_usp.DBService.DBEmittedHashes;

import com.example.dp3t_usp.DBService.DBData;

import java.util.Date;
import java.util.HashMap;

public class EmittedHashesData extends DBData  {
    public static HashMap<String,Class> fieldsTypes = new HashMap<>();
    public HashMap<String,Object> values = new HashMap<>();

    public EmittedHashesData(String hash){
        fieldsTypes.put(EmittedHashesContract.COLUMN_EMITTED_HASH, String.class);
        fieldsTypes.put(EmittedHashesContract.COLUMN_DATE, Date.class);
        setValues(hash);
    }

    public EmittedHashesData(String hash, String date){
        fieldsTypes.put(EmittedHashesContract.COLUMN_EMITTED_HASH, String.class);
        fieldsTypes.put(EmittedHashesContract.COLUMN_DATE, Date.class);
        setValues(hash, date);
    }

    public void setValues(String hash){
        HashMap<String, Object> newValues = new HashMap<>();
        newValues.put(EmittedHashesContract.COLUMN_EMITTED_HASH, hash);
        newValues.put(EmittedHashesContract.COLUMN_DATE, new Date());
        this.values = newValues;
    }
    public void setValues(String hash, String date){
        HashMap<String, Object> newValues = new HashMap<>();
        newValues.put(EmittedHashesContract.COLUMN_EMITTED_HASH, hash);
        newValues.put(EmittedHashesContract.COLUMN_DATE, date);
        this.values = newValues;
    }

}
