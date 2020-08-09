package com.example.dp3t_usp.CheckupService;

import android.content.Context;
import android.text.style.TtsSpan;
import android.util.Log;

import com.example.dp3t_usp.DBService.DBLastCheck.LastCheckContract;
import com.example.dp3t_usp.DBService.DBLastCheck.LastCheckData;
import com.example.dp3t_usp.DBService.DBLastCheck.LastCheckService;
import com.example.dp3t_usp.DateService.DateService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SyncedService {

    public static boolean checkSync(Context context){
        LastCheckService lastCheckService;
        lastCheckService = new LastCheckService(context);
        ArrayList<LastCheckData> lastCheckData = lastCheckService.getData();
        Date actualTime = Calendar.getInstance().getTime();


        Log.e("actualTime", actualTime.toString());

        if(lastCheckData == null){
            return false;
        }

        if(lastCheckData.get(0).getField(LastCheckContract.COLUMN_DATE) == null){
            return false;
        }
        Date lastTime = DateService.parseString(lastCheckData.get(0).getField(LastCheckContract.COLUMN_DATE));

        Log.e("lastCheckData", lastCheckData.get(0).getField(LastCheckContract.COLUMN_DATE));
        return true;
    }

}
