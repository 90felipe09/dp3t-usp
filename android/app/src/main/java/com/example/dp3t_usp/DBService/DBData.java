package com.example.dp3t_usp.DBService;

import java.util.ArrayList;
import java.util.HashMap;

public class DBData implements DBDataInterface {
    public static HashMap<String,Class> fieldsTypes = new HashMap<>();
    public HashMap<String,Object> values = new HashMap<>();

    @Override
    public Object getField(String field) {
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
        this.values = newValues;
    }

    @Override
    public void setField(String field, Object newValue) {
        this.values.remove(field);
        this.values.put(field, newValue);
    }

}
