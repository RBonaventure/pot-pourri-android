package com.rbonaventure.potpourri.models;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

/**
 * Created by rbonaventure on 04/12/2017.
 */
@IgnoreExtraProperties
public class Location {

    @PropertyName("name")
    String mName;

    @PropertyName("address")
    String mAddress;

    public Location() {

    }

    public String getName() {
        return mName;
    }

    public String getAddress() {
        return mAddress;
    }

}
