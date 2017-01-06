package avnatarkin.hse.ru.gpscollector.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import avnatarkin.hse.ru.gpscollector.util.constants.Constants;

/**
 * Created by sanjar on 11.11.16.
 */

public class DBLocation {
    // Database table
    public static final String TABLE_TODO = "routeData";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TODO
            + " ("
            + Constants.VARIABLE_DATABASE.ID_ROAD + " integer primary key autoincrement,"
            + Constants.VARIABLE_DATABASE.ROAD + " text not null, "
            + Constants.VARIABLE_DATABASE.TIME + " text not null "
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
