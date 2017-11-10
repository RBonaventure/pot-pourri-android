package com.rbonaventure.potpourri.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.rbonaventure.potpourri.App;
import com.rbonaventure.potpourri.BuildConfig;
import com.rbonaventure.potpourri.utils.RemoteConfig;
import com.rbonaventure.potpourri.utils.Traces;

/**
 * Created by rbonaventure on 10/11/2017.
 */

public class CSRvsSSR extends WebView {

    private final FirebaseRemoteConfig mFirebaseRemoteConfig;

    private Trace mRenderingTrace;
    private RenderingType mRenderingType;

    private enum RenderingType {
        CLIENT_SIDE(Traces.CLIENT_TIME, "https://pot-pourri-android.firebaseapp.com/"),
        SERVER_SIDE(Traces.SERVER_TIME, "https://us-central1-pot-pourri-android.cloudfunctions.net/helloworld");

        private String mTraceName;
        private String mBackendUrl;

        RenderingType(String traceName, String backendUrl) {
            mTraceName = traceName;
            mBackendUrl = backendUrl;
        }

        String getTraceName() {
            return mTraceName;
        }

        String getBackendUrl() {
            return mBackendUrl;
        }
    }

    public CSRvsSSR(Context context, AttributeSet attrs) {
        super(context, attrs);

        getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mRenderingType = mFirebaseRemoteConfig.getBoolean(
                RemoteConfig.SERVERSIDE_RENDERING_ENABLED_KEY) ? RenderingType.SERVER_SIDE : RenderingType.CLIENT_SIDE;

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.d(App.TAG, "onPageStarted " + url);
                mRenderingTrace = FirebasePerformance.getInstance().newTrace(mRenderingType.getTraceName());
                mRenderingTrace.start();
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Log.d(App.TAG, "onPageFinished " + url);
                mRenderingTrace.stop();
            }
        });

        loadUrl(mRenderingType.getBackendUrl());
    }

}
