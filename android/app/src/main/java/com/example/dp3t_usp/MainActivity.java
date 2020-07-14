package com.example.dp3t_usp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int TIME_BETWEEN_SCANS = 1000;

    private TextView broadcastLabel;
    private TextView expositionLabel;

    private TextView debugLabel;

    private Button toggleExpositionBtn;
    private Button shareHashTableBtn;

    // Bluetooth related resources
    private boolean isAdvertising;
    private boolean advertisingStandby;

    private Handler handler;
    private Runnable runnable;

    private BLEAdvertiserHandler advertiser;
    private BLEScannerHandler scanner;
    private ParcelUuid pUuid;

    // Background service
    private Intent backgroundServiceIntent;
    
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
        this.pUuid = new ParcelUuid(UUID.fromString(getString(R.string.ble_uuid_dp3t)));
        this.advertiser = new BLEAdvertiserHandler(this.pUuid,"");
        this.scanner = new BLEScannerHandler(this.pUuid);
        this.backgroundServiceIntent = new Intent(this, BLEService.class);
        this.handler = new Handler();

        this.runnable = new Runnable() {
            @Override
            public void run() {
                if(isAdvertising){
                    if(advertisingStandby){
                        Log.e("check", "enter start advertising");
                        try {
                            advertiser.startAdvertising();
                            scanner.startScanning();
                        }
                        catch(Exception e){
                            Log.e("BLE", "Exception in start scan" + e.getMessage());
                        }
                        advertisingStandby = false;
                    }
                    else{
                        Log.e("check", "enter stop advertising");
                        advertiser.stopAdvertising();
                        scanner.stopScanning();
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

            startService(backgroundServiceIntent);

            runnable.run();
        }
        else{
            stopService(backgroundServiceIntent);
            handler.removeCallbacksAndMessages(runnable);
            advertiser.stopAdvertising();
            scanner.stopScanning();
        }
        Log.e("isAdvertising", "isAdvertising: " + isAdvertising);
    }
}
