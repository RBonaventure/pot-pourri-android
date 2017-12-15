package com.rbonaventure.potpourri;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.UUID;

/**
 * Created by rbonaventure on 08/11/2017.
 */

public class App extends Application {

    public static final String TAG = App.class.getName();
    public static final String UNIQUE_ID_KEY = "uuid";
    private static String mGUID;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = getSharedPreferences(TAG, MODE_PRIVATE);

        if(!prefs.contains(UNIQUE_ID_KEY)) {
            String guid = UUID.randomUUID().toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(UNIQUE_ID_KEY, guid);
            editor.apply();

            Log.v(TAG, "GUID : " + guid);
        }

        App.mGUID = prefs.getString(UNIQUE_ID_KEY, "");
    }

    public static String getGUID() {
        return mGUID;
    }

}
