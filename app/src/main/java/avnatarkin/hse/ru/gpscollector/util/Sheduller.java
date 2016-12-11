package avnatarkin.hse.ru.gpscollector.util;

import android.app.Notification;

/**
 * Created by sanjar on 11.12.16.
 */

public interface Sheduller {

    public void scheduleNotification(Notification notification, int delay);
    public Notification getNotification(String content);

}
