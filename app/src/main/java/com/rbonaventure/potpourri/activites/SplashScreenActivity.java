package com.rbonaventure.potpourri.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.rbonaventure.potpourri.App;
import com.rbonaventure.potpourri.BuildConfig;
import com.rbonaventure.potpourri.R;

/**
 * Created by rbonaventure on 08/11/2017.
 */
public class SplashScreenActivity extends Activity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private long cacheExpiration = 3600; // 1 hour

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG) // Depends on your build-type's 'debbugable' property (true / false)
                .build();

        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        // In case the first use of your App is made offline
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        // Try to fetch the remote configs
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(App.TAG, "Fetch Succeeded");
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Log.d(App.TAG, "Fetch Failed");
                        }

                        // Start the MainActivty
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

    }

}
