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
import com.rbonaventure.potpourri.App;
import com.rbonaventure.potpourri.BuildConfig;
import com.rbonaventure.potpourri.utils.Traces;

/**
 * Created by rbonaventure on 10/11/2017.
 */

public class BewizyuWebSite extends WebView {

    private Trace mRenderingTrace;

    private final String WEBSITE_URL = "https://www.bewizyu.com/";

    public BewizyuWebSite(Context context, AttributeSet attrs) {
        super(context, attrs);

        getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(!WEBSITE_URL.equals(url)) return; // Uniquement le site BEWIZYU

                Log.d(App.TAG, "onPageStarted " + url);
                mRenderingTrace = FirebasePerformance.getInstance().newTrace(Traces.BEWIZYU_TIME);
                mRenderingTrace.start();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(!WEBSITE_URL.equals(url)) return; // Uniquement le site BEWIZYU

                Log.d(App.TAG, "onPageFinished " + url);
                mRenderingTrace.stop();
            }
        });

        loadUrl(WEBSITE_URL);
    }

}
