package com.example.dp3t_usp.BLEService;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.ParcelUuid;
import android.util.Log;

import com.example.dp3t_usp.DBService.DBListenedHashes.ListenedHashesContract;
import com.example.dp3t_usp.DBService.DBListenedHashes.ListenedHashesData;
import com.example.dp3t_usp.DBService.DBListenedHashes.ListenedHashesHelper;
import com.example.dp3t_usp.DBService.DBListenedHashes.ListenedHashesService;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BLEScannerHandler {
    private BluetoothAdapter bluetoothAdapter;

    private BluetoothLeScanner bluetoothScanner;
    private List<ScanFilter> filters;
    private ScanFilter filter;
    private ScanSettings scanSettings;

    private String preventOwnHash;

    private Context context;

    private ListenedHashesService dbListenedHashes;

    // Public domain
    public BLEScannerHandler(ParcelUuid pUuid, Context context){
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        configScanner();
        setFilters(pUuid);
        this.context = context;
        this.dbListenedHashes = new ListenedHashesService(context);
    }

    public void startScanning(){
        this.bluetoothScanner.startScan(filters, scanSettings, scanCallback);
    }

    public void stopScanning(){
        this.bluetoothScanner.stopScan(scanCallback);
    }

    private void configScanner(){
        this.bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();
        this.scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
    }

    public void setPreventOwnHash(String preventOwnHash){
        this.preventOwnHash = preventOwnHash;
    }

    private void setFilters(ParcelUuid pUuid){
        filter = new ScanFilter.Builder()
                .setServiceUuid(pUuid)
                .build();
        filters = new ArrayList<ScanFilter>();
        filters.add(filter);
    }

    // Add a hash to the ListenedHashes table
    private void writeHashToDB(String hash){
        ListenedHashesData listenedHashesData = new ListenedHashesData(hash);
        dbListenedHashes.insertData(listenedHashesData);
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result){
            super.onScanResult(callbackType, result);

            StringBuilder builder = new StringBuilder("");

            builder.append(new String(
                    result.getScanRecord().getServiceData(
                            result.getScanRecord()
                                    .getServiceUuids()
                                    .get(0)), Charset.forName("UTF-8")));

            if (!dbListenedHashes.isInDb(builder.toString()) && builder.toString() != preventOwnHash){
                writeHashToDB(builder.toString());
            }

            debugDB();

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results){
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode){
            Log.e("BLE", "Error on scanning: " + errorCode);
            super.onScanFailed(errorCode);
        }

        void debugDB(){
            Log.e("Log Listened Hashes DB", "========STARTING DEBUG=======");
            ArrayList<ListenedHashesData> storedHashes = dbListenedHashes.getData();

            while(storedHashes.iterator().hasNext()){
                Log.e("Log Listened Hashes DB", storedHashes.iterator().next().values.toString());
            }
            Log.e("Log Listened Hashes DB", "========END DEBUG=======");
        }
    };

}
