package com.rbonaventure.potpourri.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rbonaventure.potpourri.R;
import com.rbonaventure.potpourri.models.Location;

/**
 * Created by rbonaventure on 07/12/2017.
 */

public class LocationsAdapter extends BaseAdapter {

    private QuerySnapshot mLocations;

    @Override
    public int getCount() {
        return mLocations == null ? 0 : mLocations.size();
    }

    @Override
    public DocumentSnapshot getItem(int position) {
        return mLocations == null ? null : mLocations.getDocuments().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Location location = getItem(position).toObject(Location.class);

        if(view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, null);
        }

        TextView tvLocation = view.findViewById(R.id.tv_location);
        tvLocation.setText(location.getName());

        return view;
    }

    public void setLocations(QuerySnapshot snapshot) {
        mLocations = snapshot;
        this.notifyDataSetChanged();
    }
}
