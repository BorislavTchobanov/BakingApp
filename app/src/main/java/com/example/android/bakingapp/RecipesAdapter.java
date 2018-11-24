package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utilities.ImageHandler;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private List<Recipe> recipes;
    private final ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    RecipesAdapter(List<Recipe> recipes, ListItemClickListener listener){
        mOnClickListener = listener;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_content;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int position) {
        final Recipe recipe = recipes.get(position);
        recipeViewHolder.setRecipeImage(recipe.getImage());
        recipeViewHolder.mContentView.setText(recipes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (recipes == null) {
            return 0;
        }
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        ProgressBar progressBar;
        final TextView mContentView;

        RecipeViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.person_photo);
//            progressBar = itemView.findViewById(R.id.loading_indicator);
            itemView.setOnClickListener(this);
            mContentView = itemView.findViewById(R.id.recipe_name);
        }

        void setRecipeImage(final String imageUrl) {
            ImageHandler.loadImage(imageUrl, imageView, progressBar);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
