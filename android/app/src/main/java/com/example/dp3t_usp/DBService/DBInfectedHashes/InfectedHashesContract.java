package com.example.dp3t_usp.DBService.DBInfectedHashes;

import com.example.dp3t_usp.DBService.DBContract;

// Um contract é uma classe que define as constantes de uma tabela.
public final class InfectedHashesContract extends DBContract {
    public static final String TABLE_NAME = "Infected_Hashes";

    public static final String COLUMN_INFECTED_HASH = "Hash";
    public static final String COLUMN_DATE = "Date";

    public static final String SQL_GET_ENTRY = "SELECT * FROM " + TABLE_NAME;
    public static final String SQL_EMPTY_TABLE = "DELETE FROM " + TABLE_NAME;
    public static String getEntry(String field, String value){
        return "SELECT * FROM " + TABLE_NAME + " WHERE " + field + " = " + value;
    }
}
