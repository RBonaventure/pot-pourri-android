package com.rbonaventure.potpourri.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rbonaventure.potpourri.R;
import com.rbonaventure.potpourri.adapters.CollaboratorsAdapter;
import com.rbonaventure.potpourri.adapters.LocationsAdapter;


public class CollaboratorsFragment extends Fragment {

    private RecyclerView mReferencesRecycler;
    private CollaboratorsAdapter mCollaboratorsAdapter;

    public CollaboratorsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resources, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final LocationsAdapter locationsAdapter = new LocationsAdapter();

        Spinner spinner = view.findViewById(R.id.sp_location_selector);
        spinner.setAdapter(locationsAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DocumentSnapshot snapshot = (DocumentSnapshot) parent.getAdapter().getItem(position);
                Log.v("TAG", snapshot.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }

        });

        FirebaseFirestore.getInstance().collection("location").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            locationsAdapter.setLocations(task.getResult());
                        }
                    }
                }
        );

        mCollaboratorsAdapter = new CollaboratorsAdapter();

        mReferencesRecycler = view.findViewById(R.id.rv_collaborators);
        mReferencesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mReferencesRecycler.setAdapter(mCollaboratorsAdapter);
    }
}
