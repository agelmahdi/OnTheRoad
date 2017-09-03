package com.graduation.a3ltreq.ontheroad.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.graduation.a3ltreq.ontheroad.MainActivity;
import com.graduation.a3ltreq.ontheroad.R;
import com.graduation.a3ltreq.ontheroad.app.AppConfig;
import com.graduation.a3ltreq.ontheroad.app.Utility;
import com.graduation.a3ltreq.ontheroad.helper.TimelineContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddPickActivity extends AppCompatActivity {

    private static final String TAG = AddPickActivity.class.getSimpleName();

    private ProgressDialog pDialog;



    private EditText message;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pick);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        Date date = new Date();
        message = (EditText) findViewById(R.id.editTextTaskDescription);


        Button mButton = (Button) findViewById(R.id.addButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                HashMap<String, String> user = Utility.getUserDetails(AddPickActivity.this);
                final String id = user.get(TimelineContract.PickEntry.COLUMN_KEY_ID);
                String input = message.getText().toString();

                if (!input.isEmpty() && !id.isEmpty()) {
                    TimelineAddPick(id, input);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    private void TimelineAddPick(final String id, final String message) {

        RequestQueue queue = Volley.newRequestQueue(this);

        pDialog.setMessage("Posting ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_TIMELINE_PICK_DO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, R.string.response + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    int error = jObj.getInt("status");
                    if (error == 200) {

                        Toast.makeText(AddPickActivity.this, R.string.posted, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(
                                AddPickActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {


                        String errorMsg = jObj.getString("error");
                        Toast.makeText(AddPickActivity.this,
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
                Toast.makeText(AddPickActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("u_id", id);
                params.put("problem", message);
                params.put("lat", "30");
                params.put("lng", "31");
                return params;
            }

        };

        // Adding request to request queue
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


}

