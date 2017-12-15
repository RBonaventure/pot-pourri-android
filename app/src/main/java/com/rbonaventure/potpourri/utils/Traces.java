package com.rbonaventure.potpourri.utils;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rbonaventure on 08/11/2017.
 */

public class Traces {

    private static Map<String, Trace> mTraces = new HashMap();

    public static final String CLIENT_TIME = "client_time";
    public static final String SERVER_TIME = "server_time";
    public static final String REACT_JS_TIME = "react_js_time";
    public static final String PLAIN_HTML_TIME = "plain_html_time";
    public static final String BEWIZYU_TIME = "bewizyu_landing_page";
    public static final String COLLABORATORS_TIME = "collaborators_time";

    public static void start(String traceName) {
        getTrace(traceName).start();
    }

    public static void stop(String traceName) {
        getTrace(traceName).stop();
    }

    private static Trace getTrace(String traceName) {
        return mTraces.containsKey(traceName) ? mTraces.get(traceName) : FirebasePerformance.getInstance().newTrace(traceName);
    }
}
