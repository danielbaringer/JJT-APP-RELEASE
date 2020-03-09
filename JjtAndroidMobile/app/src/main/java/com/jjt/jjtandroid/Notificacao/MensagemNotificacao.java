package com.jjt.jjtandroid.Notificacao;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.jjt.jjtandroid.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class MensagemNotificacao extends Service {

    private static Timer timer = new Timer();
    private Context thisCtx;

    public void startNotificationListener() {
        //start's a new thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                //fetching notifications from server
                //if there is notifications then call this method
                timer.scheduleAtFixedRate(new mainTask(), 0, 10000);

            }
        }).start();
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
            ShowNotification();
        }
    }


    @Override
    public void onCreate()
    {
        startNotificationListener();
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void ShowNotification()
    {
        /*NotificationManager notificationManager =
                (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(getBaseContext(),"notification_id")
                .setSmallIcon(R.drawable.ic_mr_button_connected_00_dark)
                .setContentTitle("Aviso JJT Distribuidora")
                .setContentText("Seu cadastro foi aprovado. Agora você pode fazer seus pedidos pelo app.")
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .build();
        notificationManager.notify(0, notification);*/
        //the notification is not showing

    }
}
