package com.example.dp3t_usp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class BLEService extends Service {
    private NotificationManager mNM;
    private int NOTIFICATION = R.string.advertising_notif;
    public BLEService() {
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // Inserir o que fazer em segundo plano aqui.
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mNM.cancel(NOTIFICATION);
        Toast.makeText(this, "O DP3T não está mais capturando", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void showNotification() {
        CharSequence text = getText(NOTIFICATION); // Texto a mostrar na notificação.
        PendingIntent contentIntent = PendingIntent.
                getActivity(
                        this,
                        0,
                        new Intent(this,MainActivity.class),
                        0); // Se o usuário clicar na notificação, o app vai abrir.
        Notification notification = new Notification.Builder(this)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getText(R.string.app_name))
                .setContentText(text)
                .setContentIntent(contentIntent)
                .build();

        mNM.notify(NOTIFICATION, notification);
    }
}
