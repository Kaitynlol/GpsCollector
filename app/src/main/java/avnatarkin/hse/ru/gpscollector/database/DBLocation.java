package avnatarkin.hse.ru.gpscollector.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by sanjar on 11.11.16.
 */

public class DBLocation {
    // Database table
    public static final String TABLE_TODO = "loc";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_ROAD = "road";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TODO
            + " ("
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_LONGITUDE + " text not null, "
            + COLUMN_LATITUDE + " text not null, "
            + COLUMN_ROAD + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(DBLocation.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(database);
    }

}
