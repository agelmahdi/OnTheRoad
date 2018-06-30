package com.graduation.a3ltreq.ontheroad.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.graduation.a3ltreq.ontheroad.helper.TimelineContract.PickEntry.TABLE_LOGIN;

/**
 * Created by Ahmed El-Mahdi on 3/13/2017.
 */

class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "on_the_road";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TimelineContract.PickEntry.TABLE_NAME + " (" +
                TimelineContract.PickEntry._ID + " INTEGER PRIMARY KEY, " +
                TimelineContract.PickEntry.COLUMN_USERS_NAME + " TEXT NOT NULL, " +
                TimelineContract.PickEntry.COLUMN_MESSAGES + " TEXT NOT NULL, " +
                TimelineContract.PickEntry.COLUMN_CREATED_AT + " NUMERIC, " +
                TimelineContract.PickEntry.COLUMN_LOCATION + " TEXT," +
                TimelineContract.PickEntry.COLUMN_PICK_ID + " INTEGER NOT NULL," +

                " UNIQUE (" + TimelineContract.PickEntry.COLUMN_PICK_ID + ") ON CONFLICT REPLACE);";


        db.execSQL(CREATE_TABLE);


        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TimelineContract.PickEntry.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }


}
