package com.example.dp3t_usp.DBService.DBLastCheck;

import com.example.dp3t_usp.DBService.DBData;

import java.util.Date;
import java.util.HashMap;

public class LastCheckData extends DBData  {
    public static HashMap<String,Class> fieldsTypes = new HashMap<>();
    public HashMap<String,Object> values = new HashMap<>();

    public LastCheckData(String date){
        fieldsTypes.put(LastCheckContract.COLUMN_DATE, Date.class);
        setValues(date);
    }

    public void setValues(String date){
        HashMap<String, Object> newValues = new HashMap<>();
        newValues.put(LastCheckContract.COLUMN_DATE, date);
        this.values = newValues;
    }

}
