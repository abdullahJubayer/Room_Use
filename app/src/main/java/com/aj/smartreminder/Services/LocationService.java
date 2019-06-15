package com.aj.smartreminder.Services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.aj.smartreminder.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

public class LocationService extends IntentService {



    public LocationService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e("Service","Start");
        GeofencingEvent event=GeofencingEvent.fromIntent(intent);
        int transitionTyp=event.getGeofenceTransition();

        if (transitionTyp == Geofence.GEOFENCE_TRANSITION_ENTER ||
                transitionTyp == Geofence.GEOFENCE_TRANSITION_EXIT) {
            String transitionString="";
            switch (transitionTyp){
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    transitionString="Enter";
                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    transitionString="Exit";
                    break;
            }

            List<Geofence> geofenceList=event.getTriggeringGeofences();
            List<String> geofenceName=new ArrayList<>();
            for (Geofence g:geofenceList){
                geofenceName.add(g.getRequestId());
            }

            String notificationMessage="You have"+transitionString+""+ TextUtils.join(",",geofenceName);
            sendNotification(notificationMessage);
        }else {
            Log.e("Result","Not Enter");

        }
    }

    private void sendNotification(String notificationMessage) {
        Log.e("Result","Enter");

        String chennalId="Geofance";
        NotificationCompat.Builder builder=new NotificationCompat.Builder(LocationService.this,chennalId)
                .setContentTitle("Alert")
                .setContentText(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_foreground);


        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name="Notification";
            String description="Notification Channel";
            int priority=NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel=new NotificationChannel(chennalId,name,priority);
            channel.setDescription(description);

            manager.createNotificationChannel(channel);
        }

        manager.notify(1,builder.build());
    }
}
