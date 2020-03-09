package com.jjt.jjtandroid.Notificacao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootSmartPhoneStartService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))
        {
            //here we start the service  again.
            Intent serviceIntent = new Intent(context, MensagemNotificacao.class);
            context.startService(serviceIntent);
        }
    }
}
