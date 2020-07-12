package com.example.dp3t_usp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;
import android.util.Log;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class BLEScannerHandler {
    private BluetoothAdapter bluetoothAdapter;

    private BluetoothLeScanner bluetoothScanner;
    private List<ScanFilter> filters;
    private ScanFilter filter;
    private ScanSettings scanSettings;


    // Public domain
    public BLEScannerHandler(ParcelUuid pUuid){
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        configScanner();
        setFilters(pUuid);
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

    private void setFilters(ParcelUuid pUuid){
        filter = new ScanFilter.Builder()
                .setServiceUuid(pUuid)
                .build();
        filters = new ArrayList<ScanFilter>();
        filters.add(filter);
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
    };
}