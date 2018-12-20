package com.company.hermes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class MyService extends Service {

    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    public static final String ACTION_DECRYPT = "ACTION_DECRYPT";
    public static final String ACTION_ENCRYPT = "ACTION_ENCRYPT";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    String text;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        Objects.requireNonNull(clipboard).addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                ClipData clipData = clipboard.getPrimaryClip();
                ClipData.Item item = Objects.requireNonNull(clipData).getItemAt(0);
                text = item.getText().toString();
                //Toast.makeText(getBaseContext(),"Copy:\n"+a,Toast.LENGTH_LONG).show();
            }
        });

        //getting the clipboard text

        //changing the clipboard text
        ClipData clip = ClipData.newPlainText("text", "hello");
        //clipboard.setPrimaryClip(clip);
        Toast.makeText(getBaseContext(),"Copy:\n"+text,Toast.LENGTH_LONG).show();

        //Action listeners for notification buttons
        if(intent != null)
        {
            String action = intent.getAction();

            switch (Objects.requireNonNull(action))
            {
                case ACTION_START_FOREGROUND_SERVICE:
                    startForegroundService();
                    Toast.makeText(getApplicationContext(), "Foreground service is started.", Toast.LENGTH_LONG).show();
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopForegroundService();
                    Toast.makeText(getApplicationContext(), "Foreground service is stopped.", Toast.LENGTH_LONG).show();
                    break;
                case ACTION_ENCRYPT:
                    Toast.makeText(getApplicationContext(), "You click Encrypt button.", Toast.LENGTH_LONG).show();
                    break;
                case ACTION_DECRYPT:
                    Toast.makeText(getApplicationContext(), "You click Decrypt button.", Toast.LENGTH_LONG).show();
                    break;
            }
        }


        return super.onStartCommand(intent,flags,startId);
    }
    private void startForegroundService()
    {
        Log.d(TAG_FOREGROUND_SERVICE, "Start foreground service.");

        // Create notification default intent.
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        // Make notification show big text.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Encrypt your message.");
        bigTextStyle.bigText("Now your can encrypt your messages via notification.");
        // Set big text style.
        builder.setStyle(bigTextStyle);

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        builder.setLargeIcon(largeIconBitmap);
        // Make the notification max priority.
        builder.setPriority(Notification.PRIORITY_MAX);
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true);

        // Add Play button intent in notification.
        Intent playIntent = new Intent(this, MyService.class);
        playIntent.setAction(ACTION_ENCRYPT);
        PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);
        NotificationCompat.Action playAction = new NotificationCompat.Action(R.drawable.ic_lock, "Encrypt", pendingPlayIntent);
        builder.addAction(playAction);

        // Add Pause button intent in notification.
        Intent pauseIntent = new Intent(this, MyService.class);
        pauseIntent.setAction(ACTION_DECRYPT);
        PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        NotificationCompat.Action prevAction = new NotificationCompat.Action(R.drawable.ic_unlock, "Decrypt", pendingPrevIntent);
        builder.addAction(prevAction);

        Intent in = new Intent(this, MainActivity.class);
        PendingIntent ppendingIntent = PendingIntent.getActivity(this,
                0, in, 0);
        builder.setContentIntent(ppendingIntent).setAutoCancel(true);

        // Build the notification.
        Notification notification = builder.build();
        if(Build.VERSION.SDK_INT>=26) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("NOTIFICATION_CHANNEL_DESC");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        // Start foreground service.
        startForeground(1, notification);

    }
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private void stopForegroundService()
    {
        Log.d(TAG_FOREGROUND_SERVICE, "Stop foreground service.");

        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }
}
