package avnatarkin.hse.ru.gpscollector.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import avnatarkin.hse.ru.gpscollector.constants.Constants;
import avnatarkin.hse.ru.gpscollector.database.exporter.DataExporter;
import avnatarkin.hse.ru.gpscollector.database.exporter.DataJsonExporter;

/**
 * Created by sanjar on 11.11.16.
 */

public class DBManager {
    private final static String LOG_TAG = "TDBManager";

    public DBManager() {
    }

    @Deprecated
    public void insert(Location location, Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        double Latitude = location.getLatitude();
        double Longitude = location.getLongitude();
        //getting road from location throw geocoder
        String road = geocoder.getFromLocation(Latitude,
                Longitude, 1).get(0).getAddressLine(0);

        Log.d(LOG_TAG, "Insert in table " + DBLocation.TABLE_TODO + " latitude " + Latitude + " longitude " + Longitude);
        ContentValues cv = new ContentValues();
        cv.put("latitude", Latitude);
        cv.put("longitude", Longitude);
        cv.put("road", road);
        // db.insert(DBLocation.TABLE_TODO, null, cv);
    }

    @Deprecated
    public static void insert(Context context, Map<String, Long> mPreparedLocation) {
        Log.d(LOG_TAG, "Write" + mPreparedLocation);
        DBHelper dbh = new DBHelper(context);
        SQLiteDatabase db = dbh.getWritableDatabase();

        ContentValues cv = new ContentValues();
        String road = mPreparedLocation.keySet().iterator().next();
        cv.put(Constants.VARIABLE_DATABASE.ROAD, road);
        cv.put(Constants.VARIABLE_DATABASE.TIME, parceTime(mPreparedLocation.get(road)));
        db.insert(DBLocation.TABLE_TODO, null, cv);
    }

    private static String parceTime(Long t) {
        String format = "HH";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+4"));
        String gmtTime = sdf.format(t);
        Log.w(LOG_TAG, "parcelable Time is: " + gmtTime);
        return gmtTime;

    }

    public static JSONObject exportBase(Context context) throws Exception {
        DBHelper dbh = new DBHelper(context);
        SQLiteDatabase db = dbh.getWritableDatabase();
        DataExporter dbe = new DataJsonExporter(db);
        return dbe.export(dbh.getDatabaseName());
    }

    public static void read(Context context) {
        Log.d(LOG_TAG, "Read table " + DBLocation.TABLE_TODO);
        DBHelper dbh = new DBHelper(context);
        SQLiteDatabase db = dbh.getWritableDatabase();

        String SQL_QUERY = "SELECT * FROM " + DBLocation.TABLE_TODO;
        Cursor c;

        c = db.rawQuery(SQL_QUERY, null);
        logCursor(c);

    }

    public static void cleanDB(Context context) {
        DBHelper dbh = new DBHelper(context);
        SQLiteDatabase db = dbh.getWritableDatabase();
        db.beginTransaction();

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    // Show data from cursor
    static void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }
        } else
            Log.d(LOG_TAG, "Cursor is null");
    }


    public static int getRiskByRoad(LocationListener locationListener, Location location) {
        return 6;
    }

    public static void insert(Context context, String roadName, long time) {
        Log.d(LOG_TAG, "Write road: " + roadName + " time:" + time);
        DBHelper dbh = new DBHelper(context);
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.VARIABLE_DATABASE.ROAD, roadName);
        cv.put(Constants.VARIABLE_DATABASE.TIME, (time / 1000) % 60);
        db.insert(DBLocation.TABLE_TODO, null, cv);
        read(context);
    }
}
