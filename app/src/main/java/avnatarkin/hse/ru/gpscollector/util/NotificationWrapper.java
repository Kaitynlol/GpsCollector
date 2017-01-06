package avnatarkin.hse.ru.gpscollector.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import avnatarkin.hse.ru.gpscollector.R;
import avnatarkin.hse.ru.gpscollector.main.MainActivity;

/**
 * Created by sanjar on 06.01.17.
 */

public class NotificationWrapper {
    public static final int NOTIFICATION_ID = 1337;
    public static final int NOTIFICATION_SYNC_ID = 1338;

    public static void createNotification(Context context, String notificationTitle, int id, boolean isOngoing) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        // We should use Compat, because we are using API 14+
        // Create the manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification;
        Notification notificationCompat;
        if (isOngoing) {
            notification = new NotificationCompat.Builder(context)
                    .setContentTitle(notificationTitle)
                    .setSmallIcon(R.drawable.cast_ic_notification_on)
                    .setContentIntent(pendingIntent);
            // Build the notification
            notificationCompat = notification.build();
            notificationCompat.flags |= Notification.FLAG_ONGOING_EVENT;
        } else {
            notification = new NotificationCompat.Builder(context)
                    .setContentTitle(notificationTitle)
                    .setContentText("done")
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.cast_ic_notification_connecting)
                    .setContentIntent(pendingIntent);


            // Build the notification
            notificationCompat = notification.build();
            notificationCompat.defaults = Notification.DEFAULT_VIBRATE;

        }
        // Push the notification
        notificationManager.notify(id, notificationCompat);
    }

    public static void deleteNotification(Context context, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}
