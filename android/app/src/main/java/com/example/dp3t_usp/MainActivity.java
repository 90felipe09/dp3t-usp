package com.example.dp3t_usp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int TIME_BETWEEN_SCANS = 1000;

    private TextView broadcastLabel;
    private TextView expositionLabel;

    private TextView debugLabel;

    private Button toggleExpositionBtn;
    private Button shareHashTableBtn;

    // Bluetooth related resources
    private BluetoothLeAdvertiser advertiser;
    private boolean isAdvertising;
    private boolean advertisingStandby;
    private AdvertiseSettings advertiseSettings;
    private ParcelUuid pUuid;
    private AdvertiseData data;

    private BluetoothLeScanner bluetoothScanner;
    private Handler handler;
    private Runnable runnable;
    private List<ScanFilter> filters;
    private ScanFilter filter;
    private ScanSettings scanSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();

        if (checkPortability()) {
            initializeBLE();
        }
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.btn_broadcast){
            toggleExposition();
        }
        else if(v.getId() == R.id.btn_share_with_fog){
            // Add behaviour on click
        }
    }

    private boolean checkPortability(){
        if(BluetoothAdapter.getDefaultAdapter() == null){
            Toast.makeText(this, "Bluetooth não habilitado.", Toast.LENGTH_SHORT).show();

            toggleExpositionBtn.setEnabled(false);
            shareHashTableBtn.setEnabled(false);
            return false;
        }
        if(!BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported()){
            Toast.makeText(this, "Broadcast não suportado.", Toast.LENGTH_SHORT).show();

            toggleExpositionBtn.setEnabled(false);
            shareHashTableBtn.setEnabled(false);

            return false;
        }
        else{return true;}
    }

    private void initializeView(){
        broadcastLabel = (TextView) findViewById(R.id.label_broadcast_status);
        expositionLabel = (TextView) findViewById(R.id.label_exposition_status);
        debugLabel = (TextView) findViewById(R.id.label_debug);

        toggleExpositionBtn = (Button) findViewById(R.id.btn_broadcast);
        shareHashTableBtn = (Button) findViewById(R.id.btn_share_with_fog);

        toggleExpositionBtn.setOnClickListener(this);
        shareHashTableBtn.setOnClickListener(this);
    }

    private void initializeBLE(){
        advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();
        advertiseSettings = new AdvertiseSettings.Builder()
                        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
                        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                        .setConnectable(false)
                        .build();
        pUuid = new ParcelUuid(UUID.fromString(getString(R.string.ble_uuid_dp3t)));

        data = new AdvertiseData.Builder()
                        .setIncludeDeviceName(false)
                        .addServiceData(pUuid, "Info".getBytes(Charset.forName("UTF-8")))
                        .build();

        Log.e("data", "data: " + data);

        isAdvertising = false;
        advertisingStandby = false;

        bluetoothScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        filter = new ScanFilter.Builder()
                        .setServiceUuid(new ParcelUuid(
                            UUID
                            .fromString(getString(R.string.ble_uuid_dp3t))))
                            .build();
        filters = new ArrayList<ScanFilter>();
        filters.add(filter);

        scanSettings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();

        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                if(isAdvertising){
                    if(advertisingStandby){
                        Log.e("check", "enter start advertising");
                        try {
                            advertiser.startAdvertising(advertiseSettings, data, advertisingCallback);

                            bluetoothScanner.startScan(filters, scanSettings, scanCallback);
                        }
                        catch(Exception e){
                            Log.e("BLE", "Exception in start scan" + e.getMessage());
                        }
                        advertisingStandby = false;
                    }
                    else{
                        Log.e("check", "enter stop advertising");
                        advertiser.stopAdvertising(advertisingCallback);
                        bluetoothScanner.stopScan(scanCallback);
                        advertisingStandby = true;
                    }
                    handler.postDelayed(runnable, TIME_BETWEEN_SCANS);
                }
            }
        };
    }

    private void toggleExposition(){
        isAdvertising = !isAdvertising;
        if(isAdvertising){
            advertisingStandby = true;
            runnable.run();
        }
        else{
            handler.removeCallbacksAndMessages(runnable);
            advertiser.stopAdvertising(advertisingCallback);
            bluetoothScanner.stopScan(scanCallback);
        }
        Log.e("isAdvertising", "isAdvertising: " + isAdvertising);
    }

    private AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess (AdvertiseSettings settingsInEffect){
            super.onStartSuccess(settingsInEffect);
            isAdvertising = true;
        }

        @Override
        public void onStartFailure(int errorCode){
            Log.e("BLE", "Error on advertise: " + errorCode);
            super.onStartFailure(errorCode);
            isAdvertising = false;
        }
    };

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result){
            Log.e("results scan", "result scan: " + result.toString());
            super.onScanResult(callbackType, result);
            if (result == null || 
                result.getDevice() == null || 
                TextUtils.isEmpty(result.getDevice().getName())){
                    return;
                }
            
            StringBuilder builder = new StringBuilder(result.getDevice().getName());

            builder.append("\n").append(new String(
                result.getScanRecord().getServiceData(
                    result.getScanRecord()
                        .getServiceUuids()
                        .get(0)), Charset.forName("UTF-8")));
            
            debugLabel.setText(builder.toString());
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
