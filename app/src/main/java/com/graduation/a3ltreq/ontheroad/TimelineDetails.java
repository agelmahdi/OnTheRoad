package com.graduation.a3ltreq.ontheroad;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.graduation.a3ltreq.ontheroad.Adapter.ChatAdapter;
import com.graduation.a3ltreq.ontheroad.app.AppConfig;
import com.graduation.a3ltreq.ontheroad.helper.TimelineContract;
import com.graduation.a3ltreq.ontheroad.model.ChatMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.key;
import static com.graduation.a3ltreq.ontheroad.activity.LoginActivity.PREFERENCE_KEY;
import static com.graduation.a3ltreq.ontheroad.activity.LoginActivity.PREF_USER_ID;
import static com.graduation.a3ltreq.ontheroad.activity.LoginActivity.PREF_USER_NAME;

public class TimelineDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private ChatAdapter mAdapter;
    private ChatMessage chatMessage;

    SharedPreferences preferences;
    private static final String[] MESSAGES_PROJECTION = {
            TimelineContract.PickEntry.COLUMN_USERS_NAME,
            TimelineContract.PickEntry.COLUMN_MESSAGES,
            TimelineContract.PickEntry.COLUMN_CREATED_AT,
            TimelineContract.PickEntry.COLUMN_LOCATION,
            TimelineContract.PickEntry.COLUMN_PICK_ID
    };

    static final int COL_NUM_USERS_NAME = 0;
    static final int COL_NUM_MESSAGES = 1;
    static final int COL_NUM_CREATED_AT = 2;
    static final int COL_NUM_LOCATION = 3;
    private static final int COL_NUM_PICK_ID = 4;


    private static final int ID_DETAIL_LOADER = 353;

    private static final String TAG = TimelineDetails.class.getSimpleName();
    private Uri mUri;
    private int pick_id;

    private EditText textInputLayout;

   private RecyclerView recyclerView;
    private boolean ascending = true;

    private Intent intent;
    private TextView txtname, txtdescription, txtduration, txtlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_details);
        preferences = getSharedPreferences(PREFERENCE_KEY, 0);
        /*pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);*/

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_details);

        setSupportActionBar(mToolbar);


        textInputLayout = (EditText) findViewById(R.id.c_input);
        recyclerView = (RecyclerView) findViewById(R.id.c_recycler_view);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        //layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.smoothScrollToPosition(0);

        mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");



        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab_msg);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int id = preferences.getInt(PREF_USER_ID, 0);
                String msg = textInputLayout.getText().toString();


                if (!msg.isEmpty()) {
                    insertMsg(String.valueOf(pick_id), id, msg);
                    textInputLayout.setText("");
                    getSupportLoaderManager().restartLoader(ID_DETAIL_LOADER, null, TimelineDetails.this);

                } else {
                    Toast.makeText(TimelineDetails.this,
                            R.string.enter_details, Toast.LENGTH_LONG)
                            .show();
                }

            }
        });

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        stopService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        getSupportLoaderManager().restartLoader(ID_DETAIL_LOADER, null, this);

    }

    private void insertMsg(final String pick_id, final int u_id, final String msg) {

        RequestQueue queue = Volley.newRequestQueue(TimelineDetails.this);

       // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DO_COMMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, R.string.response + response);
               // hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    int error = jObj.getInt("status");
                    if (error == 200) {

                        Toast.makeText(TimelineDetails.this, R.string.message_sent, Toast.LENGTH_LONG).show();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error");
                        Toast.makeText(TimelineDetails.this,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, R.string.error + error.getMessage());
                Toast.makeText(TimelineDetails.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
             //   hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pick_id", pick_id);
                params.put("u_id", u_id + "");
                params.put("msg", msg);


                return params;
            }

        };

        queue.add(strReq);
    }


   /* private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case ID_DETAIL_LOADER:

                return new CursorLoader(this,
                        mUri,
                        MESSAGES_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }
        pick_id = data.getInt(COL_NUM_PICK_ID);
        //TimelinePick(String.valueOf(pick_id));
        intent = new Intent(this, ChatService.class);
        intent.putExtra("PICK_ID",pick_id);
        this.startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(ChatService.BROADCAST_ACTION));
        String name = data.getString(COL_NUM_USERS_NAME);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    private void updateUI(Intent intent) {

        ArrayList<ChatMessage> chatMessages = intent.getParcelableArrayListExtra("CHAT_MESSAGES");
        for (int i = 0;i<chatMessages.size();i++){
            ChatMessage chatMessage = chatMessages.get(i);
              int id =  chatMessage.getId();
            if (chatMessage.getMessageUser().equals(preferences.getString(PREF_USER_NAME,""))) {
                recyclerView.smoothScrollToPosition(id);
            }

        }
        mAdapter = new ChatAdapter(chatMessages, this);

        recyclerView.setAdapter(mAdapter);
    }


}
