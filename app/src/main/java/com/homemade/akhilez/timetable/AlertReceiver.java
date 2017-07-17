package com.homemade.akhilez.timetable;

/**
 * Created by Akhilez on 1/20/2016.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context content,Intent intent){
        notif1(content,"Times up","5 seconds have passed","Alert");
    }

    public void notif1(Context context, String msg,String msgText, String msgAlert){
        PendingIntent notifIntent = PendingIntent.getActivity(context,0,
                new Intent(context,MainActivity.class),0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                //.setSmallIcon(R.drawable.notif_icon_1)
                .setContentTitle(msg)
                .setTicker(msgAlert)
                .setContentText(msgText);
        mBuilder.setContentIntent(notifIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mBuilder.build());
    }
}
