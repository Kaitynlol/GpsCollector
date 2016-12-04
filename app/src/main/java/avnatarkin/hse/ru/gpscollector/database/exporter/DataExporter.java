package avnatarkin.hse.ru.gpscollector.database.exporter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import avnatarkin.hse.ru.gpscollector.Constants;

public abstract class DataExporter {

    public static final String LOG_TAG = "DataExporter";

    protected final SQLiteDatabase db;
    private static final String DATASUBDIRECTORY = "/tracking";

    public DataExporter(SQLiteDatabase db) {
        this.db = db;
    }

    public void export(String dbName, String exportFileNamePrefix) throws Exception {
        if (dbName == null) {
            throw new IllegalArgumentException("ExportConfig.databaseName must not be null");
        }
        Log.i(LOG_TAG, "exporting database - " + dbName + " exportFileNamePrefix=" + exportFileNamePrefix);

        prepairExport(dbName);

        // get the tables
        String sql = "select * from sqlite_master";
        Cursor c = this.db.rawQuery(sql, new String[0]);
        Log.d(LOG_TAG, "select * from sqlite_master, cur size " + c.getCount());
        while (c.moveToNext()) {
            String tableName = c.getString(c.getColumnIndex("name"));
            Log.d(LOG_TAG, "table name " + tableName);

            // skip metadata, sequence, and uidx (unique indexes)
            if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")
                    && !tableName.startsWith("uidx") && !tableName.startsWith("idx_") && !tableName.startsWith("_idx")
                    ) {
                try {
                    this.exportTable(tableName);
                } catch (SQLiteException e) {
                    Log.w(LOG_TAG, "Error exporting table " + tableName, e);
                }
            }
        }
        c.close();
        String output = getExportAsString();
        this.writeToFile(output, exportFileNamePrefix + Constants.FILE_NAME);
        Log.i(LOG_TAG, "exporting database complete");
    }

    private void exportTable(final String tableName) throws Exception {
        Log.d(LOG_TAG, "exporting table - " + tableName);

        String sql = "select * from " + tableName;
        Cursor c = this.db.rawQuery(sql, new String[0]);
        int count = 1;
        startTable(tableName);
        while (c.moveToNext()) {
            startRow();
            String id = c.getString(1);
            if (id == null || TextUtils.isEmpty(id)) {
                id = c.getString(0);
            }
            for (int i = 0; i < c.getColumnCount(); i++) {
                populateRowWithField(c.getColumnName(i), c.getString(i));
            }
            endRow();
        }
        c.close();
        endTable();
    }

    private void writeToFile(String xmlString, String exportFileName) throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory(), DATASUBDIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, exportFileName);
        file.createNewFile();

        ByteBuffer buff = ByteBuffer.wrap(xmlString.getBytes());
        FileChannel channel = new FileOutputStream(file).getChannel();
        try {
            channel.write(buff);
        } finally {
            if (channel != null)
                channel.close();
        }
    }

    public void closeDb() {
        if (db != null && db.isOpen()) {
            try {
                db.close();
            } catch (Throwable e) {
            }
        }
    }

    abstract protected String getExportAsString() throws Exception;

    abstract protected void prepairExport(String dbName) throws Exception;

    abstract protected void endRow() throws Exception;

    abstract protected void populateRowWithField(String columnName, String string) throws Exception;

    abstract protected void startRow() throws Exception;

    abstract protected void endTable() throws Exception;

    abstract protected void startTable(String tableName) throws Exception;


}