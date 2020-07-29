package com.example.dp3t_usp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

public class BLEService extends Service {
    private NotificationManager mNM;
    private int NOTIFICATION = R.string.advertising_notif;

    private BLEAdvertiserHandler advertiser;
    private BLEScannerHandler scanner;
    private ParcelUuid pUuid;

    private int currentHash = 0;

    private Handler handler;
    private Runnable runnable;


//    private static int TIME_BETWEEN_HASH_CHANGES = 1000; for tests
    private static int TIME_BETWEEN_HASH_CHANGES = 1000 * 60 * 5;

    public BLEService() {
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.e("Service BLE", "Showed notification");
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // Inserir o que fazer em segundo plano aqui.
        Log.e("Service BLE", "Service ble started");
        initializeBLE();
        Log.e("check", "enter start advertising");
        try {
            advertiser.startAdvertising();
            scanner.startScanning();
        }
        catch(Exception e){
            Log.e("BLE", "Exception in start scan" + e.getMessage());
        }

        this.handler = new Handler();

        this.runnable = new Runnable() {
            @Override
            public void run() {
                currentHash = currentHash + 1;
                String hashedString = String.valueOf(currentHash);
                advertiser.configData(hashedString, pUuid);
                Log.e("Hash change", "Changed to " + hashedString);
                handler.postDelayed(runnable, TIME_BETWEEN_HASH_CHANGES);
            }
        };

        this.runnable.run();

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mNM.cancel(NOTIFICATION);
        advertiser.stopAdvertising();
        scanner.stopScanning();
        handler.removeCallbacksAndMessages(runnable);
        handler.removeCallbacks(runnable);
        Toast.makeText(this, "O DP3T não está mais capturando", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void showNotification() {
        Log.e("Service BLE", "Showing Notification");
        CharSequence text = getText(NOTIFICATION); // Texto a mostrar na notificação.
        PendingIntent contentIntent = PendingIntent.
                getActivity(
                        this,
                        0,
                        new Intent(this,MainActivity.class),
                        0); // Se o usuário clicar na notificação, o app vai abrir.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_baseline_local_hospital_24)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getText(R.string.app_name))
                .setContentText(text)
                .setContentIntent(contentIntent)
                .build();

        mNM.notify(NOTIFICATION, notification);
    }
    private void initializeBLE(){
        this.pUuid = new ParcelUuid(UUID.fromString(getString(R.string.ble_uuid_dp3t)));
        this.advertiser = new BLEAdvertiserHandler(this.pUuid,"0");
        this.scanner = new BLEScannerHandler(this.pUuid, this);
    }

}
