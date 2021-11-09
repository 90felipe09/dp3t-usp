package com.example.dp3t_usp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.dp3t_usp.APIService.RemoteAPIService;
import com.example.dp3t_usp.BLEService.BLEService;
import com.example.dp3t_usp.CheckupService.CheckupService;
import com.example.dp3t_usp.CheckupService.SyncedService;
import com.example.dp3t_usp.DBService.DBEmittedHashes.EmittedHashesContract;
import com.example.dp3t_usp.DBService.DBEmittedHashes.EmittedHashesData;
import com.example.dp3t_usp.DBService.DBEmittedHashes.EmittedHashesService;
import com.example.dp3t_usp.DBService.DBInfectedHashes.InfectedHashesData;
import com.example.dp3t_usp.DBService.DBInfectedHashes.InfectedHashesService;
import com.example.dp3t_usp.DBService.DBLastCheck.LastCheckData;
import com.example.dp3t_usp.DBService.DBLastCheck.LastCheckService;
import com.example.dp3t_usp.DBService.DBListenedHashes.ListenedHashesData;
import com.example.dp3t_usp.DBService.DBListenedHashes.ListenedHashesService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.view.View;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int TIME_BETWEEN_SCANS = 1000;

    private Button shareWithFogButton;
    private Switch broadcastSwitch;

    //Safety status
    private UserStatus status;
    private ImageView statusImage;
    private TextView statusLabel;
    private TextView exposedRecommendation;
    // Bluetooth related resources
    private boolean isAdvertising;
    private boolean advertisingStandby;

    // Api
    private APIService apiService;
//    private ArrayList<String> hashes;

    InfectedHashesService infectedHashesService;
    LastCheckService lastCheckService;
    CheckupService checkupService;
    EmittedHashesService emittedHashesService;
    ListenedHashesService listenedHashesService;

    class onGetHashesSuccessCallbackImpl implements APIService.onGetHashesSuccessCallback {
        @Override
        public void callback(ArrayList<String> newHashes) {
            ArrayList<String> hashes = newHashes;

            Iterator<String> iterator = hashes.iterator();

            String currentHash;
            InfectedHashesData infectedHash;

            while(iterator.hasNext()){
                currentHash = iterator.next();

                if(!infectedHashesService.isInDb(currentHash)){
                    infectedHash = new InfectedHashesData(currentHash);
                    infectedHashesService.insertData(infectedHash);
                }

            }

            Log.i("callback", ""+newHashes);
            debugDB();

            LastCheckData lastCheckData = new LastCheckData(Calendar.getInstance().getTime().toString());
            lastCheckService.insertData(lastCheckData);

            if(checkupService.wasExposed()){
                updateUserStatus(UserStatus.exposed);
            }
            else{
                updateUserStatus(UserStatus.safe);
            }
        }
    }

    private void debugDB(){
        Log.e("Log Infected Hashes DB", "========STARTING DEBUG=======");
        ArrayList<InfectedHashesData> storedHashes = infectedHashesService.getData();
        Iterator<InfectedHashesData> iterator = storedHashes.iterator();
        while(iterator.hasNext()){
            Log.e("Log Infected Hashes DB", iterator.next().values.toString());
        }
        Log.e("Log Infected Hashes DB", "========END DEBUG=======");

        Log.e("Log Emitted Hashes DB", "========STARTING DEBUG=======");
        ArrayList<EmittedHashesData> emmitedHashes = emittedHashesService.getData();
        Iterator<EmittedHashesData> iteratorEmitted = emmitedHashes.iterator();
        while(iteratorEmitted.hasNext()){
            Log.e("Log Emitted Hashes DB", iteratorEmitted.next().values.toString());
        }
        Log.e("Log Emitted Hashes DB", "========END DEBUG=======");

        Log.e("Log Listened Hashes DB", "========STARTING DEBUG=======");
        ArrayList<ListenedHashesData> listenedHashes = listenedHashesService.getData();
        Iterator<ListenedHashesData> iteratorListened = listenedHashes.iterator();
        while(iteratorListened.hasNext()){
            Log.e("Log Listened Hashes DB", iteratorListened.next().values.toString());
        }
        Log.e("Log Emitted Hashes DB", "========END DEBUG=======");
    }

    class onPostHashesSuccessCallbackImpl implements APIService.onPostHashesSuccessCallback {
        @Override
        public void callback() {
            Log.e("PostHashes", "Hashes posted successfully, callback called");
        }

        @Override
        public void onSuccess(Object o) {
            Log.e("PostHashes", "Hashes posted successfully");
        }
    }

    class onPostHashesFailureCallbackImpl implements APIService.onPostHashesFailureCallback{
        @Override
        public void callback() {
            Log.e("PostHashes", "Hashes post has failed, callback called");
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            Log.e("PostHashes", "Hashes post has failed");
        }
    }



    // Background service
    private Intent backgroundServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String remoteURL = getString(R.string.cloud_url);
        this.apiService = new RemoteAPIService(remoteURL, this);
        infectedHashesService = new InfectedHashesService(this);
        checkupService = new CheckupService(this);
        emittedHashesService = new EmittedHashesService(this);
        lastCheckService = new LastCheckService(this);
        listenedHashesService = new ListenedHashesService(this);

        initializeView();

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
        exposedRecommendation = findViewById(R.id.exposed_recomendation);

        debugDB();

        if(!SyncedService.checkSync(this)){
            updateUserStatus(UserStatus.outdated);
            Log.i("initializeView()", "Is calling getInfectedHashes");
            apiService.getInfectedHashes(new onGetHashesSuccessCallbackImpl());
        }
        else{
            Log.e("Is already Synced", "Is synced");
            if(checkupService.wasExposed()){
                Log.e("Was exposed", "Was exposed");
                updateUserStatus(UserStatus.exposed);
            }
            else{
                Log.e("Was not exposed", "Was not exposed");
                updateUserStatus(UserStatus.safe);
            }
        }

        broadcastSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setExposition(isChecked);
            }
        });

        shareWithFogButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<EmittedHashesData> emittedHashes = emittedHashesService.getData();
                Iterator<EmittedHashesData> iterator = emittedHashes.iterator();
                ArrayList<String> hashesToSend = new ArrayList<>();
                String columnHashName = EmittedHashesContract.COLUMN_EMITTED_HASH;
                while(iterator.hasNext()){
                    hashesToSend.add(iterator.next().getField(columnHashName));
                }
                apiService.sendHashes(hashesToSend,
                        new onPostHashesSuccessCallbackImpl(),
                        new onPostHashesFailureCallbackImpl());
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
        Log.e("updateUserStatus", "updatedUser " + status);
        switch (status){
            case safe:
                setSafeStatus();
                break;
            case outdated:
                setOutdatedStatus();
                break;
            case exposed:
                setExposedStatus();
                break;
        }
    }

    private void setSafeStatus() {
        status = UserStatus.safe;
        statusLabel.setText("Você está seguro");
        statusImage.setImageResource(R.drawable.ic_baseline_check_circle_24);
        statusImage.setColorFilter(Color.GREEN);
        exposedRecommendation.setVisibility(View.GONE);
    }
    private void setOutdatedStatus() {
        status = UserStatus.outdated;
        statusLabel.setText("Dados desatualizados");
        statusImage.setImageResource(R.drawable.ic_baseline_sync_problem_24);
        statusImage.setColorFilter(Color.BLUE);
        exposedRecommendation.setVisibility(View.GONE);
    }
    private void setExposedStatus(){
        status = UserStatus.exposed;
        statusLabel.setText("Você foi exposto!");
        statusImage.setImageResource(R.drawable.ic_baseline_warning_24);
        statusImage.setColorFilter(Color.RED);
        exposedRecommendation.setVisibility(View.VISIBLE);
    }
}
