package com.graduation.a3ltreq.ontheroad;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.graduation.a3ltreq.ontheroad.Adapter.ChatAdapter;
import com.graduation.a3ltreq.ontheroad.app.AppConfig;
import com.graduation.a3ltreq.ontheroad.model.ChatMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ahmed El-Mahdi on 2/17/2018.
 */

public class ChatService extends Service {
    private static final String TAG = "BroadcastService";
    public static final String BROADCAST_ACTION = "com.graduation.a3ltreq.ontheroad.ACTION_DISPLAY";
    private int pick_id;
    private final Handler handler = new Handler();
    Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();


        intent = new Intent(BROADCAST_ACTION);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // = intent.getStringExtra();
        super.onStartCommand(intent, flags, startId);
        pick_id = (int) intent.getExtras().get("PICK_ID");

        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000);

        return flags;
    }

    private Runnable sendUpdatesToUI = new Runnable() {

        public void run() {
            TimelinePick(String.valueOf(pick_id));
            handler.postDelayed(this, 10000); // 10 seconds
        }

    };

    private void TimelinePick(final String id) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_TIMELINE_PICK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, R.string.response + response);
                //  hideDialog();
                ArrayList<ChatMessage> cMes = new ArrayList<>();

                try {
                    JSONObject jObj = new JSONObject(response);
                    int error = jObj.getInt("status");
                    if (error == 200) {

                        JSONArray array = jObj.getJSONArray("comments");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            ChatMessage chatMessage = new ChatMessage(jsonObject);
                            cMes.add(chatMessage);
                        }

                        intent.putParcelableArrayListExtra("CHAT_MESSAGES",cMes);
                        sendBroadcast(intent);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, R.string.error + error.getMessage());

                // hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("id", id);


                return params;
            }

        };

        queue.add(strReq);


    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
    }
}
