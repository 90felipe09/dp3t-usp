package com.example.dp3t_usp.BLEService;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;

import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;

import com.example.dp3t_usp.DBService.DBEmittedHashes.EmittedHashesData;
import com.example.dp3t_usp.DBService.DBEmittedHashes.EmittedHashesService;
import com.example.dp3t_usp.DBService.DBListenedHashes.ListenedHashesData;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


public class BLEAdvertiserHandler{
    // Bluetooth related resources
    private BluetoothLeAdvertiser advertiser;
    private AdvertiseSettings advertiseSettings;

    private AdvertiseData data;
    
    private BluetoothAdapter bluetoothAdapter;

    private EmittedHashesService emittedHashesService;

    // Public domain
    public BLEAdvertiserHandler(ParcelUuid uuid, String data, Context context){
        this.emittedHashesService = new EmittedHashesService(context);
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        configAdvertiser(uuid);
        configData(data, uuid);


}

    public void startAdvertising(){
        this.advertiser.startAdvertising(advertiseSettings, data, advertisingCallback);
    }

    public void stopAdvertising(){
        this.advertiser.stopAdvertising(advertisingCallback);
    }

    //Private domain

    private void configAdvertiser(ParcelUuid uuid){
        this.advertiser = this.bluetoothAdapter.getBluetoothLeAdvertiser();
        this.advertiseSettings = new AdvertiseSettings.Builder()
                        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
                        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW)
                        .setConnectable(false)
                        .build();

    }

    private AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess (AdvertiseSettings settingsInEffect){
            super.onStartSuccess(settingsInEffect);
            Log.e("AdvertiseCallback","It worked");
        }

        @Override
        public void onStartFailure(int errorCode){
            Log.e("AdvertiseCallback","It DID NOT worked" + errorCode);
            super.onStartFailure(errorCode);
        }
    };

    
    public void configData(String dataToSend, ParcelUuid pUuid){
        this.data = new AdvertiseData.Builder()
                        .setIncludeDeviceName(false)
                        .addServiceData(pUuid, dataToSend.getBytes(Charset.forName("UTF-8")))
                        .build();
        EmittedHashesData emittedHash = new EmittedHashesData(dataToSend, Calendar.getInstance().getTime().toString());
        Log.e("BLEAdvert:emittedHash:", emittedHash.values.toString());
        this.emittedHashesService.insertData(emittedHash);
        debugDB();
        stopAdvertising();
        startAdvertising();

    }

    void debugDB(){

        Log.e("Log Emitted Hashes DB", "========STARTING DEBUG=======");
        ArrayList<EmittedHashesData> storedHashes = emittedHashesService.getData();

        Iterator<EmittedHashesData> iterator = storedHashes.iterator();
        while(iterator.hasNext()){
            Log.e("Log Emitted Hashes DB", iterator.next().values.toString());
        }
        Log.e("Log Emitted Hashes DB", "========END DEBUG=======");
        emittedHashesService.deleteAllData();
    }

}