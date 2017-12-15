package com.rbonaventure.potpourri.models;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.QuerySnapshot;
import com.rbonaventure.potpourri.utils.FirestoreCollections;

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

    public static void getAll(OnCompleteListener<QuerySnapshot> listener) {
        FirebaseFirestore.getInstance().collection(FirestoreCollections.LOCATIONS).get().addOnCompleteListener(listener);
    }
}
