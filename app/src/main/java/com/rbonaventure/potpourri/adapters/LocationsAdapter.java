package com.rbonaventure.potpourri.adapters;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

        TextView text = new TextView(parent.getContext());
        text.setText(location.getName());
        return text;
    }

    public void setLocations(QuerySnapshot snapshot) {
        Log.v("TAG", snapshot.size() + "");
        mLocations = snapshot;
        this.notifyDataSetChanged();
    }
}
