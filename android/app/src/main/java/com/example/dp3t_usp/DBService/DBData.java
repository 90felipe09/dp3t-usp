package com.example.dp3t_usp.DBService;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DBData implements DBDataInterface {
    public static HashMap<String,Class> fieldsTypes = new HashMap<>();
    private HashMap<String,String> values = new HashMap<>();

    @Override
    public String getField(String field) {
        Log.e("getField", field);
        Log.e("getField", this.values.toString());
        Log.e("getField", this.values.get(field));
        return values.get(field);
    }

    @Override
    public ArrayList<Object> getFields(ArrayList<String> fields) {
        ArrayList elements = new ArrayList();
        String currentField;
        for(int i = 0; i < values.size(); i++){
            currentField = fields.get(i);
            if(fields.contains(fieldsTypes.get(currentField).getName())){
                elements.add(values.get(currentField));
            }
        }
        return elements;
    }

    @Override
    public void setFields(HashMap newValues) {
        Log.e("setField", newValues.toString());
        Log.e("setField", values.toString());
        this.values = newValues;
        Log.e("setField", values.toString());
    }

    @Override
    public void setField(String field, String newValue) {
        this.values.remove(field);
        this.values.put(field, newValue);
    }

}
