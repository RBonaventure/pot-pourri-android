package com.rbonaventure.potpourri.activites;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.rbonaventure.potpourri.R;
import com.rbonaventure.potpourri.utils.RemoteConfig;

/**
 * Created by rbonaventure on 08/11/2017.
 */
public class MainActivity extends Activity {

    private TextView mWelcomeMessage;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        setContentView(mFirebaseRemoteConfig.getBoolean(RemoteConfig.EXPERIMENTATION_ENABLED_KEY) ?
                R.layout.activity_csr_vs_ssr : R.layout.activity_bewizyu);

        mWelcomeMessage = findViewById(R.id.tv_welcome_message);
        mWelcomeMessage.setText(mFirebaseRemoteConfig.getString(RemoteConfig.WELCOME_MESSAGE_KEY));
    }

}
