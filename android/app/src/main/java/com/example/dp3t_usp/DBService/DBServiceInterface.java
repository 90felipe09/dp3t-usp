package com.example.dp3t_usp.DBService;

import java.util.ArrayList;
import java.util.HashMap;

public interface DBServiceInterface<DBData> {
    // Methods to insert data
    void insertData(DBData data);

    void insertData(ArrayList<DBData> dataList);

    // Methods to get db Data
    ArrayList<DBData> getData();

    ArrayList<DBData> getData(ArrayList<String> fields);

    DBData getData(String field, Object value);

    // Methods to delete data
    void deleteData(String id);

    void deleteData(ArrayList<String> id);

    void deleteAllData();

    // Exists in db
    boolean isInDb(String hash);

}
