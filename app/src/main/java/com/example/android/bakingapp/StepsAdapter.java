package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utilities.ImageHandler;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private List<Step> steps;
    private final ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    StepsAdapter(List<Step> steps, ListItemClickListener listener){
        mOnClickListener = listener;
        this.steps = steps;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.step_list_content;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder recipeViewHolder, int position) {
        final Step step = steps.get(position);
        recipeViewHolder.setStepImage(step.getThumbnailUrl());
        recipeViewHolder.mContentView.setText(steps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (steps == null) {
            return 0;
        }
        return steps.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView poster;
        ProgressBar progressBar;
        final TextView mContentView;

        StepViewHolder(View itemView) {
            super(itemView);

//            poster = itemView.findViewById(R.id.thumbnail_iv);
//            progressBar = itemView.findViewById(R.id.loading_indicator);
            itemView.setOnClickListener(this);
            mContentView = itemView.findViewById(R.id.content);
        }

        void setStepImage(final String imageUrl) {
//            ImageHandler.loadImage(imageUrl, poster, progressBar);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
