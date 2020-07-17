package com.example.dp3t_usp;

import android.provider.BaseColumns;

public class InfectedHashesContract {
    private InfectedHashesContract(){}

    // Define o conteúdo da tabela
    public static class InfectedHashEntry implements BaseColumns {
        public static final String TABLE_NAME = "Infected_Hashes";
        public static final String COLUMN_INFECTED_HASH = "Hash";
        public static final String COLUMN_DATE = "Check Date";
    }
}
