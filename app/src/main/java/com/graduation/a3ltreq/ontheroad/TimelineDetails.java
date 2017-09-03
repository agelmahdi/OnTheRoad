package com.graduation.a3ltreq.ontheroad;

import android.app.ProgressDialog;
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
import com.graduation.a3ltreq.ontheroad.app.Utility;
import com.graduation.a3ltreq.ontheroad.helper.TimelineContract;
import com.graduation.a3ltreq.ontheroad.model.ChatMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimelineDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private ChatAdapter mAdapter;
    private ChatMessage chatMessage;
    private TextView netError;

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
    private ProgressDialog pDialog;
    private Uri mUri;
    private int pick_id;

    private EditText textInputLayout;

    private TextView txtname, txtdescription, txtduration, txtlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_details);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_details);

        this.setSupportActionBar(mToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        netError =(TextView) findViewById(R.id.net_work_error_msg);

        txtname = (TextView) findViewById(R.id.name_msg);
        txtdescription = (TextView) findViewById(R.id.description_msg);
        txtduration = (TextView) findViewById(R.id.duration_msg);
        txtlocation = (TextView) findViewById(R.id.location_msg);

        textInputLayout = (EditText) findViewById(R.id.c_input);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.c_recycler_view);

        ArrayList<ChatMessage> mChatMessages = new ArrayList<>();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new ChatAdapter(mChatMessages, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");


        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab_msg);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> user = Utility.getUserDetails(TimelineDetails.this);
                final String id = user.get(TimelineContract.PickEntry.COLUMN_KEY_ID);
                String msg = textInputLayout.getText().toString();


                if (!msg.isEmpty() && !id.isEmpty()) {
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
    public void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(ID_DETAIL_LOADER, null, this);

    }

    private void insertMsg(final String pick_id, final String u_id, final String msg) {

        RequestQueue queue = Volley.newRequestQueue(TimelineDetails.this);

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DO_COMMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, R.string.response + response);
                hideDialog();

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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pick_id", pick_id);
                params.put("u_id", u_id);
                params.put("msg", msg);


                return params;
            }

        };

        queue.add(strReq);
    }

    private void TimelinePick(final String id) {

        RequestQueue queue = Volley.newRequestQueue(TimelineDetails.this);

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_TIMELINE_PICK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, R.string.response + response);
                hideDialog();
                ArrayList<ChatMessage> cMes = new ArrayList<>();
                netError.setVisibility(View.INVISIBLE);

                try {
                    JSONObject jObj = new JSONObject(response);
                    int error = jObj.getInt("status");
                    if (error == 200) {
                        JSONObject user = jObj.getJSONObject("data");
                        // int id = user.getInt("id");
                        String name = user.getString("name");
                        String message = user.getString("problem");
                        String location = user.getString("location");
                        String createdAt = user.getString("created_at");

                        txtname.setText(name);
                        txtdescription.setText(message);
                        txtlocation.setText(location);
                        txtduration.setText(String.valueOf(createdAt));

                        JSONArray array = jObj.getJSONArray("comments");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            chatMessage = new ChatMessage(jsonObject);
                            cMes.add(chatMessage);
                        }
                        if (mAdapter != null) {
                            mAdapter.add(cMes);
                        }
                    } else {

                        netError.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, R.string.error + error.getMessage());
                netError.setVisibility(View.VISIBLE);

                hideDialog();
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

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
        TimelinePick(String.valueOf(pick_id));


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
