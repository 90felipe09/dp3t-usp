package com.example.dp3t_usp.DBService.DBInfectedHashes;

import com.example.dp3t_usp.DBService.DBData;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class InfectedHashesData extends DBData  {
    public static HashMap<String,Class> fieldsTypes = new HashMap<>();
    public HashMap<String,String> values = new HashMap<>();

    public InfectedHashesData(String hash){
        fieldsTypes.put(InfectedHashesContract.COLUMN_INFECTED_HASH, String.class);
        fieldsTypes.put(InfectedHashesContract.COLUMN_DATE, Date.class);
        setValues(hash);
    }

    public InfectedHashesData(String hash, String date){
        fieldsTypes.put(InfectedHashesContract.COLUMN_INFECTED_HASH, String.class);
        fieldsTypes.put(InfectedHashesContract.COLUMN_DATE, Date.class);
        setValues(hash, date);
    }
    public String getField(String field) {
        return values.get(field);
    }

    public void setValues(String hash){
        HashMap<String, String> newValues = new HashMap<>();
        newValues.put(InfectedHashesContract.COLUMN_INFECTED_HASH, hash);
        newValues.put(InfectedHashesContract.COLUMN_DATE, Calendar.getInstance().getTime().toString());
        this.values = newValues;
    }
    public void setValues(String hash, String date){
        HashMap<String, String> newValues = new HashMap<>();
        newValues.put(InfectedHashesContract.COLUMN_INFECTED_HASH, hash);
        newValues.put(InfectedHashesContract.COLUMN_DATE, date);
        this.values = newValues;
    }

}
