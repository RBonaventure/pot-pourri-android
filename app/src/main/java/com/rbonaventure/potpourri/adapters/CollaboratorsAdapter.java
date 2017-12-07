package com.rbonaventure.potpourri.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.rbonaventure.potpourri.R;
import com.rbonaventure.potpourri.models.Collaborator;
import com.rbonaventure.potpourri.utils.Traces;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rbonaventure on 04/12/2017.
 */

public class CollaboratorsAdapter extends RecyclerView.Adapter<CollaboratorsAdapter.ViewHolder> {
    private List<Collaborator> mCollaborators = new ArrayList<>();

    Context mContext;
    Trace mTrace;

    public CollaboratorsAdapter() {

        mTrace = FirebasePerformance.getInstance().newTrace(Traces.COLLABORATORS_TIME);
        mTrace.start();
        //.whereEqualTo("location", "LYS")
        FirebaseFirestore.getInstance().collection("resources").orderBy("icon").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    Log.v("TAG", "Results : " + task.getResult().size());
                    mCollaborators.clear();
                    for(DocumentSnapshot snapshot : task.getResult()) {
                        mCollaborators.add(snapshot.toObject(Collaborator.class));
                    }
                    CollaboratorsAdapter.this.notifyDataSetChanged();
                } else {
                    Log.v("TAG", task.getException().toString());
                }
                mTrace.stop();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_collaborator, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Collaborator collaborator = mCollaborators.get(position);
        String content = mContext.getString(R.string.resource_content_format,
                collaborator.getName(), collaborator.getJob(), collaborator.getQuote());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.mName.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.mName.setText(Html.fromHtml(content));
        }

        Picasso.with(mContext).load(collaborator.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.mIcon);
    }

    @Override
    public int getItemCount() {
        return mCollaborators.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mQuote;
        ImageView mIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_client_name);
            mQuote = itemView.findViewById(R.id.tv_client_name);
            mIcon = itemView.findViewById(R.id.iv_client_logo);
        }
    }

}
