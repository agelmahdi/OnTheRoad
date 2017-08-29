package com.graduation.a3ltreq.ontheroad.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.graduation.a3ltreq.ontheroad.MainActivity;
import com.graduation.a3ltreq.ontheroad.R;
import com.graduation.a3ltreq.ontheroad.Widget.TimelineWidgetProvider;
import com.graduation.a3ltreq.ontheroad.helper.TimelineContract;

/**
 * Created by Ahmed El-Mahdi on 6/25/2017.
 */

public class TimelineFirebaseMessageService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("name");
        String mes = remoteMessage.getData().get("problem");
        String location = remoteMessage.getData().get("location");
        String createdAt = remoteMessage.getData().get("created_at");
        int pick_id = Integer.parseInt(remoteMessage.getData().get("pick_id"));

        showNotification(mes, user);

        insertPick(user, mes, createdAt, location, pick_id);

        Intent intentUpdate = new Intent(this, TimelineWidgetProvider.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        sendBroadcast(intentUpdate);

    }

    private void showNotification(String mes, String user) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FILL_IN_ACTION);
        // Create the pending intent to launch the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.cast_ic_notification_0)
                .setContentTitle("On The Road : " + user)
                .setContentText(mes)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    private void insertPick(final String userName, final String message, final String createdAt, final String location, final int pick_id) {

        // Database operations should not be done on the main thread
        AsyncTask<Void, Void, Void> insertSquawkTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues newMessage = new ContentValues();
                newMessage.put(TimelineContract.PickEntry.COLUMN_USERS_NAME, userName);
                newMessage.put(TimelineContract.PickEntry.COLUMN_MESSAGES, message);
                newMessage.put(TimelineContract.PickEntry.COLUMN_CREATED_AT, createdAt);
                newMessage.put(TimelineContract.PickEntry.COLUMN_LOCATION, location);
                newMessage.put(TimelineContract.PickEntry.COLUMN_PICK_ID, pick_id);
                getContentResolver().insert(TimelineContract.PickEntry.CONTENT_URI_PICKS, newMessage);
                return null;
            }
        };

        insertSquawkTask.execute();
    }

}
