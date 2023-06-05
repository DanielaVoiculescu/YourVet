package com.example.yourvet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.yourvet.model.Notification;
import com.google.firebase.messaging.RemoteMessage;

public class AlarmReceiver  extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String doctor_name=intent.getStringExtra("appointment_doctor");
        String text="Peste o ora aveti programare la medicul "+doctor_name;
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,"foxandroid")
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Reminder Programare")
                .setContentText(text)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat= NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
    }
}
