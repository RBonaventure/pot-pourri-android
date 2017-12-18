package com.rbonaventure.potpourri.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.QuerySnapshot;
import com.rbonaventure.potpourri.App;
import com.rbonaventure.potpourri.utils.FirestoreCollections;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rbonaventure.potpourri.App.TAG;

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
    String mIcon;

    @PropertyName("job")
    String mJob;

    @PropertyName("location")
    String mLocation;

    @PropertyName("keywords")
    List<String> mKeywords = new ArrayList<>();

    @PropertyName("references")
    List<String> mReferences = new ArrayList<>();

    DocumentReference mRef;

    public String getName() {
        return mName;
    }

    public String getIcon() {
        return mIcon;
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

    public List<String> getKeywords() {
        return mKeywords;
    }

    public List<String> getReferences() {
        return mReferences;
    }

    public void setRef(DocumentReference ref) {
        this.mRef = ref;
    }
    public DocumentReference getRef() {
        return mRef;
    }

    public void like() {
        Map<String, Date> like = new HashMap<>();
        like.put("date", new Date());

        mRef.collection(FirestoreCollections.LIKES).document(App.getGUID())
                .set(like).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()) {
                    Log.v(TAG, task.getException().toString());
                }

            }
        });
    }

    public void setOnlikesCountChange(EventListener listener) {
        mRef.collection(FirestoreCollections.LIKES).addSnapshotListener(listener);
    }

    public void setOnLikeChange(EventListener listener) {
        mRef.collection(FirestoreCollections.LIKES).document(App.getGUID()).addSnapshotListener(listener);
    }

    public static void getAll(OnCompleteListener<QuerySnapshot> listener) {
        FirebaseFirestore.getInstance()
                .collection(FirestoreCollections.RESOURCES).orderBy("icon").get().addOnCompleteListener(listener);
    }
}
