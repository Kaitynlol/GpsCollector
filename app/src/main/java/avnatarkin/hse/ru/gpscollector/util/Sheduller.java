package avnatarkin.hse.ru.gpscollector.util;

import android.app.Notification;

/**
 * Created by sanjar on 11.12.16.
 */

public interface Sheduller {
    void scheduleNotification(Notification notification, int delay);

    Notification getNotification(String content);

}
