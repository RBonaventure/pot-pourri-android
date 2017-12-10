package com.rbonaventure.potpourri.models;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

/**
 * Created by rbonaventure on 04/12/2017.
 */
@IgnoreExtraProperties
public class Collaborator {

    @PropertyName("name")
    String mName;

    @PropertyName("quote")
    String mQuote;

    @PropertyName("icon")
    String mImageUrl;

    @PropertyName("job")
    String mJob;

    @PropertyName("location")
    String mLocation;

    public Collaborator() {

    }

    public String getName() {
        return mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getQuote() {
        return mQuote;
    }

    public String getJob() {
        return mJob;
    }

    public String getLocation() {
        return mLocation;
    }

}
