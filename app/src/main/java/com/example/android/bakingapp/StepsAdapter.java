package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.model.Step;

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
        int layoutIdForListItem = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder stepViewHolder, int position) {
        final Step step = steps.get(position);
        stepViewHolder.setStepText(position, step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (steps == null) {
            return 0;
        }
        return steps.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView stepNumberTv;
        final TextView shortDescriptionTv;

        StepViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            stepNumberTv = itemView.findViewById(R.id.step_number);
            shortDescriptionTv = itemView.findViewById(R.id.step_short_description);
        }

        void setStepText(int position, final String text) {
            String step = "Step: ";
            stepNumberTv.setText(step.concat(String.valueOf(position)));
            shortDescriptionTv.setText(text);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
