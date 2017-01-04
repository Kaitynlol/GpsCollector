package avnatarkin.hse.ru.gpscollector.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sanjar on 11.11.16.
 */

public class DBHelper extends SQLiteOpenHelper {
    private final static String LOG_TAG = "TDbHelper";

    private final static int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "locationtable.db";

    public DBHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        DBLocation.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        DBLocation.onUpgrade(database, oldVersion, newVersion);
    }
}
