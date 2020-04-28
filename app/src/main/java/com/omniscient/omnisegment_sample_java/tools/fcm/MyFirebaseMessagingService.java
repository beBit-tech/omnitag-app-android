package com.omniscient.omnisegment_sample_java.tools.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.omniscient.omnisegment_java.OmniAnalytics;
import com.omniscient.omnisegment_sample_java.MainActivity;
import com.omniscient.omnisegment_sample_java.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String TAG = this.getClass().getName();
    Bitmap bitmap;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        // call OmniAnalytics.getInstance(this).processRemoteMessage() to get Intent object
        Intent intent = OmniAnalytics.getInstance(this).processRemoteMessage(remoteMessage);
        intent.setClass(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        String title = getResources().getString(R.string.app_name);
        String body = null;
        if (remoteMessage.getNotification() != null){
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        }

        if (remoteMessage.getData().size() > 0){
            intent.putExtra("document_id",remoteMessage.getData().get("document_id"));
            if (remoteMessage.getData().get("text") != null) {
                body =remoteMessage.getData().get("text");
            }
        }
        if (body == null){
            return;
        }

        String imageUri = remoteMessage.getData().get("image_url");
        bitmap = getBitmapfromUrl(imageUri);

        String extras = "";
        for (String key : remoteMessage.getData().keySet()) {
            extras+="key:"+key+", value:"+remoteMessage.getData().get(key)+"\n";
            Log.d(TAG, "sendNotification: "+key+", value="+remoteMessage.getData().get(key));
        }

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, (int)(Math.random() * 100), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = "channel01";
        int notifyId = (int) System.currentTimeMillis();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setLargeIcon(bitmap)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setStyle(new NotificationCompat.BigPictureStyle().setSummaryText("SummartText" + title +"\n"+extras).bigPicture(bitmap))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("bigText"+ body +"\n"+extras))
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setColor(ContextCompat.getColor(this, android.R.color.transparent))
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "TestGA notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(notifyId /* ID of notification */, notificationBuilder.build());
    }
    /*
     *To get a Bitmap image from the URL received
     * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

}