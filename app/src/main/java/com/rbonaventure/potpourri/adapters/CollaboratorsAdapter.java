package com.rbonaventure.potpourri.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.rbonaventure.potpourri.App;
import com.rbonaventure.potpourri.R;
import com.rbonaventure.potpourri.models.Collaborator;
import com.rbonaventure.potpourri.utils.FirestoreCollections;
import com.rbonaventure.potpourri.utils.Traces;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rbonaventure on 04/12/2017.
 */

public class CollaboratorsAdapter extends RecyclerView.Adapter<CollaboratorsAdapter.CollaboratorViewHolder> implements Filterable {
    private QuerySnapshot mCollaborators;
    private List<Collaborator> mSelectedCollaborators = new ArrayList<>();

    Context mContext;
    Trace mTrace;
    Filter mFilter;

    public CollaboratorsAdapter() {

        mTrace = FirebasePerformance.getInstance().newTrace(Traces.COLLABORATORS_TIME);
        mTrace.start();

        FirebaseFirestore.getInstance().collection(FirestoreCollections.RESOURCES).orderBy("icon").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    mCollaborators = task.getResult();
                    CollaboratorsAdapter.this.notifyDataSetChanged();
                } else {
                    Log.v(App.TAG, task.getException().toString());
                }
                mTrace.stop();
            }
        });
    }

    @Override
    public CollaboratorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new CollaboratorViewHolder(inflater.inflate(R.layout.item_collaborator, parent, false));
    }

    @Override
    public void onBindViewHolder(CollaboratorViewHolder holder, int position) {
        Collaborator collaborator = mSelectedCollaborators.get(position);

        holder.mKeywords.removeAllViews();
        for(String keyword : collaborator.getKeywords()) {
            TextView tag = new TextView(mContext);
            tag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            tag.setBackgroundResource(R.drawable.corner_radius);
            tag.setText(keyword);
            holder.mKeywords.addView(tag);
        }

        holder.mReferences.removeAllViews();
        for(String reference : collaborator.getReferences()) {
            ImageView logo = new ImageView(mContext);
            Picasso.with(mContext).load(reference).resize(0, 50).into(logo);
            holder.mReferences.addView(logo);
        }

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
        return mSelectedCollaborators.size();
    }

    @Override
    public Filter getFilter() {
        if(mFilter == null) {
            mFilter = new CollaboratorFilter();
        }
        return mFilter;
    }

    static class CollaboratorViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        ImageView mIcon;
        LinearLayout mKeywords;
        LinearLayout mReferences;

        public CollaboratorViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.iv_client_logo);
            mName = itemView.findViewById(R.id.tv_client_name);
            mKeywords = itemView.findViewById(R.id.ll_keywords);
            mReferences = itemView.findViewById(R.id.ll_references);
        }
    }

    private class CollaboratorFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            List<Collaborator> collaborators = new ArrayList<>();

            if(constraint != null && constraint.length() > 0) {

                for (DocumentSnapshot document : mCollaborators.getDocuments()) {
                    Collaborator collaborator = document.toObject(Collaborator.class);

                    if (constraint.equals(collaborator.getLocation())) {
                        collaborators.add(collaborator);
                    }
                }
            }

            results.count = collaborators.size();
            results.values = collaborators;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mSelectedCollaborators = (List<Collaborator>) results.values;
            notifyDataSetChanged();
        }
    }
}
