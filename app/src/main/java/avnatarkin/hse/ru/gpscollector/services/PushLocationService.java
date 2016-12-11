package avnatarkin.hse.ru.gpscollector.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import avnatarkin.hse.ru.gpscollector.constants.Constants;
import avnatarkin.hse.ru.gpscollector.R;
import avnatarkin.hse.ru.gpscollector.database.DBManager;
import avnatarkin.hse.ru.gpscollector.receivers.SyncReceiver;
import avnatarkin.hse.ru.gpscollector.util.NetworkUtil;
import avnatarkin.hse.ru.gpscollector.util.Sheduller;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class PushLocationService extends Service implements LocationListener, Sheduller {
    private static final String TAG = "LSERVICE";

    // For location updates
    private LocationManager mLocationManager;

    // Read preference data
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    //Shedulling
    private int mTimeToSync = 5;
    //DataBase
    private DBManager mDbManager;
    private Map<String, Long> mPreparedLocation;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        // super.onStartCommand(intent, flags, startId);
        // Get the update frequency
        int updateFreq = mPrefs.getInt(Constants.PUSH_TIME, 1);
        Log.d(TAG, "PushTime: " + updateFreq);
        // Get the Network status
        int connectionStatus = NetworkUtil.getConnectionStatus(this);
        // Check if the service is still activated by the user
        boolean isRunning = mPrefs.getBoolean(Constants.SERVICE_RUNNING, false);
        mTimeToSync = mPrefs.getInt(Constants.SYNC_TIME, 5);
        mDbManager = new  DBManager(this);


        // If we have network connection
        if ((isRunning) && (connectionStatus == NetworkUtil.TYPE_MOBILE ||
                connectionStatus == NetworkUtil.TYPE_WIFI)) {
            String provider = (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) ?
                    LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;
            Log.d(TAG, provider);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
            }
            Log.d(TAG, "BLABLA");
            mLocationManager.requestLocationUpdates(provider, updateFreq * 1000, 10f, this);
            //scheduleNotification(getNotification(mTimeToSync+"days delay"), mTimeToSync*1000);
        } else {

            mLocationManager.removeUpdates(this);
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = mPrefs.edit();

        initializeLocationUpdates();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        stopSelf();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mLocationManager.removeUpdates(this);
        deleteNotification();

        super.onDestroy();
    }

    public void initializeLocationUpdates() {
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    public void deleteNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager man = (NotificationManager)
                getApplicationContext().getSystemService(ns);
        man.cancel(Constants.NOTIFICATION_ID);
    }

    @Override
    public void onLocationChanged(Location location) {
        String roadName = "";
        try {
            roadName = getRoadFromLocation(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mPreparedLocation == null) {
            mPreparedLocation = new HashMap<String, Long>();
            mPreparedLocation.put(roadName, location.getTime());

        } else if (!mPreparedLocation.containsKey(roadName)) {
            long nTime = mPreparedLocation.values().iterator().next();
            mPreparedLocation.remove(mPreparedLocation.keySet().iterator().next());
            mPreparedLocation.put(roadName, location.getTime() - nTime);
            mDbManager.insert(mPreparedLocation, this);

        }

    }

    private String getRoadFromLocation(Location location) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String[] lines = geocoder.getFromLocation(location.getLatitude(),
                location.getLongitude(), 1).get(0).getAddressLine(0).split(",");
        return lines[0];
    }

    private void showLocation(Location location) {
        Log.e(TAG, formatLocation(location));
    }

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(this, SyncReceiver.class);
        notificationIntent.putExtra(SyncReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(SyncReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                futureInMillis,
                delay, pendingIntent);
    }

    @Override
    public Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Execution sync with server");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.cast_ic_notification_on);
        return builder.build();
    }

}