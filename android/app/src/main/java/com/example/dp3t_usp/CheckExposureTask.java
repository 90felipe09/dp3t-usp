package com.example.dp3t_usp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

public class CheckExposureTask {

    // Mudar o endpoint de onde a gente vai retirar os hashes
    private final static String endpointCheck = "localhost:1234";
    private Context context;

    public CheckExposureTask(Context context) {
        this.context = context;
    }

    // Função para realizar a operação de checagem
    public static void checkExposure(){

    }

    // Função para pegar hashes infectados do endpoint
    private void getHashes() {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod(RequestPackage.methods.GET.toString());
        requestPackage.setUrl(endpointCheck);

        String lastCheckDate = getLastCheckDate();

        HashMap<String, String> lastCheckParam = new HashMap<String,String>();

        lastCheckParam.put("last_check", lastCheckDate);

        requestPackage.setParams(lastCheckParam);

        RequestMaker requestMaker = new RequestMaker();
        requestMaker.execute(requestPackage);
        // TODO: Até aqui foi executada a requisição. Agora é questão de parsear o retorno
        //  e tornar acessível.
    }

    // Acessa o valor guardado da última checagem e retorna esta data.
    private String getLastCheckDate(){
        LastExposureCheckHelper lastExposureCheckHelper = new LastExposureCheckHelper(context);
        SQLiteDatabase sqLiteDatabase = lastExposureCheckHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(LastExposureCheckContract.SQL_GET_ENTRY,null);
        cursor.moveToFirst();
        int lastCheckCollumnIndex = cursor.getColumnIndex(LastExposureCheckContract.LastExposureCheckEntry.LAST_CHECK);
        return cursor.getString(lastCheckCollumnIndex);
    }
}