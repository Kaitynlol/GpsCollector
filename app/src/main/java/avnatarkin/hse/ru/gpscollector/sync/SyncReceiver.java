package avnatarkin.hse.ru.gpscollector.sync;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import avnatarkin.hse.ru.gpscollector.R;
import avnatarkin.hse.ru.gpscollector.database.DBManager;
import avnatarkin.hse.ru.gpscollector.sync.util.SyncWrapper;
import avnatarkin.hse.ru.gpscollector.util.constants.Constants;


public class SyncReceiver extends BroadcastReceiver {
    public static String NOTIFICATION = "notification";
    private final String TAG = "SYNC";

    public SyncReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        JSONObject base = null;
        try {
            base = DBManager.exportBase(context);
        } catch (Exception e) {
            throw new RuntimeException("Corrupted DataBase!");
        }
        final String url = sharedPreferences.getString(Constants.URL, "god damn");
        Log.w(TAG, "URL: " + url);
        SyncWrapper.sendDataToServer(context.getApplicationContext(), url, base);
    }

    @Deprecated
    private void sendDataToServer(Context context, final String url, JSONObject json) {
        RequestQueue queue = Volley.newRequestQueue(context);
        // Define the POST request
        VolleyLog.v("URL:" + url);
        JsonObjectRequest req = new JsonObjectRequest(url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage() + url);
            }
        });

        // Add the request object to the queue to be executed
        queue.add(req);
        deleteNotification(context);
    }

    @Deprecated
    private static void deleteNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // notificationManager.cancel(Constants.NOTIFICATION_SYNC_ID);
    }

    @Deprecated
    public static class HttpAsyncTask extends
            AsyncTask<Void, Void, Boolean> {

        String urlString;
        private final String TAG = "POST_JSON";
        private Context context;
        private String root;

        public HttpAsyncTask(Context context, String base, String URL) {
            this.urlString = URL;
            this.root = base;
            this.context = context.getApplicationContext();
        }

        @Override
        protected void onPreExecute() {
            Log.e(TAG, "1 - RequestVoteTask is about to start...");

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean status = false;

            String response = "";
            Log.e(TAG, "2 - pre Request to response...");

            try {
                response = performPostCall(urlString, new HashMap<String, String>() {

                    private static final long serialVersionUID = 1L;

                    {
                        put("Accept", "application/json");
                        put("Content-Type", "application/json");
                    }
                });
                Log.e(TAG, "3 - give Response...");
                Log.e(TAG, "4 " + response.toString());
            } catch (Exception e) {
                // displayLoding(false);

                Log.e(TAG, "Error ...");
            }
            Log.e(TAG, "5 - after Response...");

            if (!response.equalsIgnoreCase("")) {
                try {
                    Log.e(TAG, "6 - response !empty...");
                    //
                    JSONObject jRoot = new JSONObject(response);
                    JSONObject d = jRoot.getJSONObject("d");

                    int ResultType = d.getInt("ResultType");
                    Log.e("ResultType", ResultType + "");

                    if (ResultType == 1) {

                        status = true;

                    }

                } catch (JSONException e) {
                    // displayLoding(false);
                    // e.printStackTrace();
                    Log.e(TAG, "Error " + e.getMessage());
                } finally {

                }
            } else {
                Log.e(TAG, "6 - response is empty...");

                status = false;
            }

            return status;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            //
            Log.e(TAG, "7 - onPostExecute ...");

            if (result) {
                Log.e(TAG, "8 - Update UI ...");

                // setUpdateUI(adv);
            } else {
                Log.e(TAG, "8 - Finish ...");

                // displayLoding(false);
                // finish();
            }
            deleteNotification(context);
        }


        public String performPostCall(String requestURL,
                                      HashMap<String, String> postDataParams) {

            URL url;
            String response = "";
            try {
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(context.getResources().getInteger(
                        R.integer.maximum_timeout_to_server));
                conn.setConnectTimeout(context.getResources().getInteger(
                        R.integer.maximum_timeout_to_server));
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");

                Log.e(TAG, "11 - url : " + requestURL);

            /*
             * JSON
             */

                //
                // String token = Static.getPrefsToken(context);

                // root.put("securityInfo", Static.getSecurityInfo(context));
                // root.put("advertisementId", advertisementId);

                Log.e(TAG, "12 - root : " + root);

                byte[] outputBytes = root.getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(outputBytes);

                int responseCode = conn.getResponseCode();

                Log.e(TAG, "13 - responseCode : " + responseCode);

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.e(TAG, "14 - HTTP_OK");

                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    Log.e(TAG, "14 - False - HTTP_OK");
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.cancel(true);
            }

            return response;
        }
    }
}
