package com.graduation.a3ltreq.ontheroad.app;

import android.content.Context;
import android.database.Cursor;

import com.graduation.a3ltreq.ontheroad.helper.TimelineContract;

import java.util.HashMap;

/**
 * Created by Ahmed El-Mahdi on 8/23/2017.
 */

public class Utility {

    private static final String[] USER_COLUMNS = {
            TimelineContract.PickEntry._ID,
            TimelineContract.PickEntry.COLUMN_KEY_ID,
            TimelineContract.PickEntry.COLUMN_USER_NAME,
            TimelineContract.PickEntry.COLUMN_KEY_EMAIL,

    };

    private static final int COL_ID = 1;
    private static final int COL_NAME = 2;
    private static final int COL_EMAIL= 3;
    public static HashMap<String, String> getUserDetails(Context context) {
        HashMap<String, String> user = new HashMap<>();
        Cursor cursor = context.getContentResolver().query(
                TimelineContract.PickEntry.CONTENT_URI_LOGIN,
                USER_COLUMNS,
                null,
                null,
                null);

        assert cursor != null;
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put(TimelineContract.PickEntry.COLUMN_KEY_ID, cursor.getString(COL_ID));
            user.put(TimelineContract.PickEntry.COLUMN_USER_NAME, cursor.getString(COL_NAME));
            user.put(TimelineContract.PickEntry.COLUMN_KEY_EMAIL, cursor.getString(COL_EMAIL));
        }
        cursor.close();
        return user;
    }

}
