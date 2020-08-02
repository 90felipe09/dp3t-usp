package com.example.dp3t_usp;

import androidx.annotation.NonNull;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dp3t_usp.APIService.APIService;
import com.example.dp3t_usp.APIService.FirebaseAPIService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int TIME_BETWEEN_SCANS = 1000;

    private Button shareWithFogButton;
    private Switch broadcastSwitch;

    //Safety status
    private UserStatus status;
    private ImageView statusImage;
    private TextView statusLabel;
    // Bluetooth related resources
    private boolean isAdvertising;
    private boolean advertisingStandby;

    // Api
    private APIService apiService;
    private ArrayList<String> hashes;



    class onGetHashesSuccessCallbackImpl implements APIService.onGetHashesSuccessCallback {
        @Override
        public void callback(ArrayList<String> newHashes) {
            hashes = newHashes;
            Log.i("callback", ""+newHashes);
        }
    }



    // Background service
    private Intent backgroundServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.apiService = new FirebaseAPIService();

        initializeView();

        updateUserStatus(UserStatus.outdated);

        checkPortability();


    }


    private boolean checkPortability(){
        if(BluetoothAdapter.getDefaultAdapter() == null){
            Toast.makeText(this, "Bluetooth não habilitado.", Toast.LENGTH_SHORT).show();

            broadcastSwitch.setEnabled(false);
            shareWithFogButton.setEnabled(false);
            return false;
        }
        if(!BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported()){
            Toast.makeText(this, "Broadcast não suportado.", Toast.LENGTH_SHORT).show();

            broadcastSwitch.setEnabled(false);
            shareWithFogButton.setEnabled(false);

            return false;
        }
        else{
            this.backgroundServiceIntent = new Intent(this, BLEService.class);
            return true;
        }
    }

    private void initializeView(){
        broadcastSwitch = findViewById(R.id.switch_broadcast);
        shareWithFogButton = findViewById(R.id.btn_share_with_fog);
        statusImage = findViewById(R.id.img_exposition_status);
        statusLabel = findViewById(R.id.label_exposition_status);




        broadcastSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //setExposition(isChecked);
            }
        });

        shareWithFogButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO IMPL : add share with fog function, and update Status
                apiService.getInfectedHashes(new onGetHashesSuccessCallbackImpl());
            }
        });
    }

    private void setExposition(boolean setAdvertising){
        isAdvertising = setAdvertising;
        if(isAdvertising){
            advertisingStandby = true;

            try {
                startService(backgroundServiceIntent);
            }
            catch(Exception e){
                Log.e("startService", e.getMessage());
            }
        }
        else{
            stopService(backgroundServiceIntent);
        }
        Log.e("isAdvertising", "isAdvertising: " + isAdvertising);
    }


    private enum UserStatus {
        safe,
        outdated,
        exposed
    }

    private void updateUserStatus(UserStatus status){
        switch (status){
            case safe:
                setSafeStatus();
            case outdated:
                setOutdatedStatus();
            case exposed:
                setExposedStatus();
        }
    }

    private void setSafeStatus() {
        //todo fe
        status = UserStatus.safe;
    }
    private void setOutdatedStatus() {
        //todo fe
        status = UserStatus.outdated;
    }
    private void setExposedStatus(){
        //todo fe
        status = UserStatus.exposed;
    }
}
