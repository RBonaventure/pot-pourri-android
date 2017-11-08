package com.rbonaventure.potpourri.activites;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.rbonaventure.potpourri.R;

/**
 * Created by rbonaventure on 08/11/2017.
 */

public class MainActivity extends Activity {

    private TextView mWelcomeMessage;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String WELCOME_MESSAGE_KEY = "welcome_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mWelcomeMessage = findViewById(R.id.tv_welcome_message);
        mWelcomeMessage.setText(mFirebaseRemoteConfig.getString(WELCOME_MESSAGE_KEY));
    }

}
