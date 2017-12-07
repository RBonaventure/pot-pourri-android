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

    public CSRvsSSR(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO
    }

}
