package avnatarkin.hse.ru.gpscollector.sync.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import avnatarkin.hse.ru.gpscollector.util.NotificationWrapper;

/**
 * Created by sanjar on 06.01.17.
 */

public class SyncWrapper {
    public static void sendDataToServer(Context context, final String url, JSONObject json) {
        NotificationWrapper.createNotification(context, "Syncronisation", NotificationWrapper.NOTIFICATION_SYNC_ID, false);
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
        // NotificationWrapper.deleteNotification(context,NotificationWrapper.NOTIFICATION_SYNC_ID);
    }

    public static JSONObject receiveDataFromServer() {
        return null;
    }
}
