package avnatarkin.hse.ru.gpscollector.database.exporter;

import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataJsonExporter extends DataExporter {

    private JSONObject jsonRoot;
    private JSONObject row;
    private JSONArray table;


    public DataJsonExporter(SQLiteDatabase db) {
        super(db);
        jsonRoot = new JSONObject();
    }

    @Override
    protected void prepairExport(String dbName) throws Exception {
        jsonRoot.put("userName", "anatarkina@gmail.com");
    }

    @Override
    protected String getExportAsString() throws Exception {
        return jsonRoot.toString();
    }


    protected void startTable(String tableName) throws Exception {
        table = new JSONArray();
        jsonRoot.put(tableName, table);
    }

    protected void endTable() throws Exception {

    }

    protected void endRow() throws Exception {
        table.put(row);
    }


    protected void populateRowWithField(String columnName, String string) throws Exception {
        row.put(columnName, string);
    }

    protected void startRow() throws Exception {
        row = new JSONObject();
    }

}