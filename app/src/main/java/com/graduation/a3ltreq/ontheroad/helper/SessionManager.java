package com.graduation.a3ltreq.ontheroad.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by Ahmed El-Mahdi on 3/13/2017.
 */

public class SessionManager {
    // LogCat tag
    private static final String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    private final SharedPreferences pref;

    private final Editor editor;
    private final Context _context;

    // Shared pref mode
    private final int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "OnTheRoad";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
