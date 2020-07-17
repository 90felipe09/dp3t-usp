package com.example.dp3t_usp;

import android.provider.BaseColumns;

public class LastExposureCheckContract {
    private LastExposureCheckContract(){}

    // Define o conteúdo da tabela
    public static class LastExposureCheckEntry implements BaseColumns {
        public static final String TABLE_NAME = "Last_Exposure_Check";
        public static final String LAST_CHECK = "Last_Check";
    }

    public static final String SQL_GET_ENTRY = "SELECT * FROM TABLE " + LastExposureCheckContract.LastExposureCheckEntry.TABLE_NAME;
}