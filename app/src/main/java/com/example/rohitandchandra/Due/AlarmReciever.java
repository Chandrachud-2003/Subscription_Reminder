package com.example.rohitandchandra.Due;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
            Log.d("notification", text);


            PendingIntent pendingIntent = PendingIntent.getActivity(context,10101,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "my_channel")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.notification)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notification))
                    .setContentTitle(title)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(title+" Due: "+currency+amount)
                    .setContentText(text+" | Due on "+date)
                    .setAutoCancel(true)
                    .setChannelId("my_channel");

            notificationManager.notify(10101,builder.build());









    }

}
