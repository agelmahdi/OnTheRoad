package com.graduation.a3ltreq.ontheroad.helper;

import android.net.Uri;
import android.provider.BaseColumns;

import static com.graduation.a3ltreq.ontheroad.helper.TimelineContract.PickEntry.CONTENT_URI_PICKS;

/**
 * Created by Ahmed El-Mahdi on 6/23/2017.
 */

public class TimelineContract {

    public static final String AUTHORITY = "com.graduation.a3ltreq.ontheroad";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_PICK = "pick";
    public static final String PATH_PICKS = "picks";
    public static final String PATH_LOGIN = "data";


    public static final class PickEntry implements BaseColumns {

        public static final Uri CONTENT_URI_PICK =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PICK).build();
        public static final Uri CONTENT_URI_PICKS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PICKS).build();
        public static final Uri CONTENT_URI_LOGIN =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOGIN).build();

        public static final String TABLE_NAME = "picks";
        public static final String TABLE_LOGIN = "data";

        public static final String COLUMN_KEY_ID = "id";
        public static final String COLUMN_USER_NAME = "name";
        public static final String COLUMN_USER_LOCATION = "location";
        public static final String COLUMN_KEY_EMAIL = "email";

        // PICKS Table Columns names
        // public static final String COLUMN_USERS_ID = "id";
        public static final String COLUMN_USERS_NAME = "name";
        public static final String COLUMN_MESSAGES = "problem";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_PICK_ID = "pick_id";


    }

    public static Uri buildUriWithID(int date) {
        return CONTENT_URI_PICKS.buildUpon()
                .appendPath(Integer.toString(date))
                .build();
    }


}
