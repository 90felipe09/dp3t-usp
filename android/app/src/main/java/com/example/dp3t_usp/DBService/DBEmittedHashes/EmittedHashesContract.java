package com.example.dp3t_usp.DBService.DBEmittedHashes;

import com.example.dp3t_usp.DBService.DBContract;

// Um contract é uma classe que define as constantes de uma tabela.
public final class EmittedHashesContract extends DBContract {
    public static final String TABLE_NAME = "Emitted_Hashes";

    public static final String COLUMN_EMITTED_HASH = "Hash";
    public static final String COLUMN_DATE = "Date";

    public static final String SQL_GET_ENTRY = "SELECT * FROM " + TABLE_NAME;
    public static final String SQL_EMPTY_TABLE = "DELETE FROM " + TABLE_NAME;
    public static String getEntry(String field, String value){
        return "SELECT * FROM " + TABLE_NAME + " WHERE " + field + " = " + value;
    }
}
