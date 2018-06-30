package com.graduation.a3ltreq.ontheroad;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;


public class MyService extends IntentService {

    public static String ID_QUERY_EXTRA= "query";
    public MyService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent( Intent intent) {
        String id_query = intent.getStringExtra(ID_QUERY_EXTRA);

    }
}
