package com.rbonaventure.potpourri.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rbonaventure.potpourri.R;
import com.rbonaventure.potpourri.models.Collaborator;
import com.rbonaventure.potpourri.utils.Traces;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.rbonaventure.potpourri.App.TAG;

/**
 * Created by rbonaventure on 04/12/2017.
 */

public class CollaboratorsAdapter extends RecyclerView.Adapter<CollaboratorsAdapter.CollaboratorViewHolder> implements Filterable {
    private QuerySnapshot mCollaborators;
    private List<Collaborator> mSelectedCollaborators = new ArrayList<>();

    Context mContext;
    Filter mFilter;

    public CollaboratorsAdapter() {

        Traces.start(Traces.COLLABORATORS_TIME);

        // Fetch collaborators list from Firestore
        Collaborator.getAll(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    mCollaborators = task.getResult();
                    CollaboratorsAdapter.this.notifyDataSetChanged();
                }
                Traces.stop(Traces.COLLABORATORS_TIME);
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
    public void onBindViewHolder(final CollaboratorViewHolder holder, int position) {
        final Collaborator collaborator = mSelectedCollaborators.get(position);

        // How many likes did the collaborator get ? In real time.
        collaborator.setOnlikesCountChange(new EventListener<QuerySnapshot>() {
             @Override
             public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                 if(e != null) {
                    Log.v(TAG, e.getMessage());
                 } else {
                     holder.mLike.setText(documentSnapshots.getDocuments().size() + "");
                 }
             }
         });

        // Did I already like the collaborator's profile or not ?
        collaborator.setOnLikeChange(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(e != null) {
                    Log.v(TAG, e.getMessage());
                } else {
                    holder.mLike.setCompoundDrawablesWithIntrinsicBounds(
                            ContextCompat.getDrawable(mContext,
                                documentSnapshot.exists() ? R.drawable.ic_thumb_up : R.drawable.ic_thumb_up_outline),
                                null,
                                null,
                                null);
                }
            }
        });

        // Display a list of keywords
        holder.mKeywords.removeAllViews();
        for(String keyword : collaborator.getKeywords()) {
            TextView tag = new TextView(mContext);
            tag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            tag.setBackgroundResource(R.drawable.corner_radius);
            tag.setText(keyword);
            holder.mKeywords.addView(tag);
        }

        // Display the name, the job and the favourite quote of the collaborator
        String content = mContext.getString(R.string.resource_content_format,
                collaborator.getName(), collaborator.getJob(), collaborator.getQuote());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.mName.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.mName.setText(Html.fromHtml(content));
        }

        Picasso.with(mContext).load(collaborator.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.mIcon);

        holder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collaborator.like();
            }
        });
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
        View mParent;
        TextView mName;
        TextView mLike;
        ImageView mIcon;
        LinearLayout mKeywords;

        public CollaboratorViewHolder(View itemView) {
            super(itemView);
            mParent = itemView;
            mIcon = itemView.findViewById(R.id.iv_client_logo);
            mLike = itemView.findViewById(R.id.tv_like);
            mName = itemView.findViewById(R.id.tv_client_name);
            mKeywords = itemView.findViewById(R.id.ll_keywords);
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
                    collaborator.setRef(document.getReference());

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
