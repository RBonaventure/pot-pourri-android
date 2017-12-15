package com.rbonaventure.potpourri.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.rbonaventure.potpourri.App;
import com.rbonaventure.potpourri.BuildConfig;
import com.rbonaventure.potpourri.utils.RemoteConfig;
import com.rbonaventure.potpourri.utils.Traces;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

/**
 * Created by rbonaventure on 10/11/2017.
 */

public class ReactJSvsPlainHTML extends WebView {

    private final FirebaseRemoteConfig mFirebaseRemoteConfig;

    private RenderingType mRenderingType;

    private enum RenderingType {
        REACT_JS(Traces.REACT_JS_TIME, "https://pot-pourri-android.firebaseapp.com/reactjs"),
        PLAIN_HTML(Traces.PLAIN_HTML_TIME, "https://pot-pourri-android.firebaseapp.com/html");

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

    public ReactJSvsPlainHTML(Context context, AttributeSet attrs) {
        super(context, attrs);

        getSettings().setJavaScriptEnabled(true);
        getSettings().setAppCacheEnabled(false);
        getSettings().setCacheMode(LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mRenderingType = mFirebaseRemoteConfig.getBoolean(
                RemoteConfig.REACTJS_ENABLED_KEY) ? RenderingType.REACT_JS : RenderingType.PLAIN_HTML;

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.d(App.TAG, "onPageStarted " + url);
                Traces.start(mRenderingType.getTraceName());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Log.d(App.TAG, "onPageFinished " + url);
                Traces.stop(mRenderingType.getTraceName());
            }
        });

        loadUrl(mRenderingType.getBackendUrl());
    }

}
