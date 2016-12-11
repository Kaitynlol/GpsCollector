package avnatarkin.hse.ru.gpscollector.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * Created by sanjar on 11.11.16.
 */

public class DBManager {
    private final static String LOG_TAG = "TDBManager";

    private final SQLiteDatabase db;
    private final DBHelper dbh;


    public DBManager(Context context) {
       // Connecting to DB
        dbh = new DBHelper(context);
        db = dbh.getWritableDatabase();
    }

    public void insert(Location location,Context context) throws IOException {
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
        db.insert(DBLocation.TABLE_TODO, null, cv);
    }
    public void insert(Map<String, Long> mPreparedLocation, Context context) {
        //TODO
    }

    public void read() {
        Log.d(LOG_TAG, "Read table " + DBLocation.TABLE_TODO);
        String SQL_QUERY = "SELECT * FROM " + DBLocation.TABLE_TODO;
        Cursor c;

        c = db.rawQuery(SQL_QUERY, null);
        logCursor(c);

    }

    void cleanDB() {
        db.beginTransaction();

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    // Show data from cursor
    void logCursor(Cursor c) {
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


}
