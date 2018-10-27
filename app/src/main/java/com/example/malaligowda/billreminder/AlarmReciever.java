package com.example.malaligowda.billreminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmReciever extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {



            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Intent intent1 = new Intent(context,MainActivity.class);
            String title = intent.getStringExtra("type");
            String text = intent.getStringExtra("name");
            String currency = intent.getStringExtra("currency");
            String date = intent.getStringExtra("dates");
            String amount = intent.getStringExtra("amount");
        Log.d("notification", "onReceive: "+title);

            PendingIntent pendingIntent = PendingIntent.getActivity(context,10101,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.notification)
                    .setContentTitle(title)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(title+" Due")
                    .setContentText(title+" \""+text+"\" of "+currency+amount+" due on "+date)
                    .setAutoCancel(true)
                    .setChannelId("myID");

            notificationManager.notify(10101,builder.build());








        }
}
