package com.example.dp3t_usp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;

import android.os.ParcelUuid;

import java.nio.charset.Charset;


public class BLEAdvertiserHandler{
    // Bluetooth related resources
    private BluetoothLeAdvertiser advertiser;
    private AdvertiseSettings advertiseSettings;

    private AdvertiseData data;
    
    private BluetoothAdapter bluetoothAdapter;

    // Public domain
    public BLEAdvertiserHandler(ParcelUuid uuid, String data){
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
                        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                        .setConnectable(false)
                        .build();

    }

    private AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess (AdvertiseSettings settingsInEffect){
            super.onStartSuccess(settingsInEffect);
        }

        @Override
        public void onStartFailure(int errorCode){
            super.onStartFailure(errorCode);
        }
    };

    
    public void configData(String dataToSend, ParcelUuid pUuid){
        data = new AdvertiseData.Builder()
                        .setIncludeDeviceName(false)
                        .addServiceData(pUuid, dataToSend.getBytes(Charset.forName("UTF-8")))
                        .build();
    }

}