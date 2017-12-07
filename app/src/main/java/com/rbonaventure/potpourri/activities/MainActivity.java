package com.rbonaventure.potpourri.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.rbonaventure.debug.DebugView;
import com.rbonaventure.potpourri.R;
import com.rbonaventure.potpourri.fragments.BewizyuFragment;
import com.rbonaventure.potpourri.fragments.CollaboratorsFragment;
import com.rbonaventure.potpourri.fragments.ReactJSvsHTMLFragment;
import com.rbonaventure.potpourri.utils.RemoteConfig;

/**
 * Created by rbonaventure on 08/11/2017.
 */
public class MainActivity extends AppCompatActivity {

    private TextView mWelcomeMessage;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private BottomNavigationView mBottomNavigation;

    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        setContentView(R.layout.activity_main);

        DebugView actionBarDebugView = new DebugView(this);
        actionBarDebugView.setTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(actionBarDebugView);

        mWelcomeMessage = findViewById(R.id.tv_welcome_message);

        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mBottomNavigation.inflateMenu(R.menu.bottom_menu);
        mFragmentManager = getSupportFragmentManager();

        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.action_perf:
                        mFragment = new BewizyuFragment();
                        mWelcomeMessage.setText(mFirebaseRemoteConfig.getString(RemoteConfig.WELCOME_MESSAGE_KEY));
                        break;
                    case R.id.action_ab_testing:
                        mFragment = new ReactJSvsHTMLFragment();
                        mWelcomeMessage.setText(mFirebaseRemoteConfig.getString(RemoteConfig.WELCOME_MESSAGE_KEY));
                        break;
                    case R.id.action_firestore:
                        mFragment = new CollaboratorsFragment();
                        mWelcomeMessage.setText(mFirebaseRemoteConfig.getString(RemoteConfig.THE_TEAM_KEY));
                        break;
                }
                final FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, mFragment).commit();
                return true;
            }
        });
        mBottomNavigation.setSelectedItemId(R.id.action_firestore);
    }

}
