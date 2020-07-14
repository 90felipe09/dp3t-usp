package com.example.dp3t_usp;

import android.provider.BaseColumns;

// Um contract é uma classe que define as constantes de uma tabela.
public final class ListenedHashesContract {
    private ListenedHashesContract(){}

    // Define o conteúdo da tabela
    public static class ListenedHashEntry implements BaseColumns{
        public static final String TABLE_NAME = "Listened_Hashes";
        public static final String COLUMN_LISTENED_HASH = "Hash";
        public static final String COLUMN_DATE = "Date";
    }
}
