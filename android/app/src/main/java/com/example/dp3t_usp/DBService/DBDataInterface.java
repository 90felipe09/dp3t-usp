package com.example.dp3t_usp.DBService;

import java.util.ArrayList;
import java.util.HashMap;

public interface DBDataInterface {
    Object getField(String field);
    ArrayList<Object> getFields(ArrayList<String> field);

    void setFields(HashMap newValues);
    void setField(String field, String newValue);
}