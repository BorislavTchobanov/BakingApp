package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.model.Ingredient;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredients;

    IngredientsAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.ingredient_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewHolder, int position) {
        final Ingredient ingredient = ingredients.get(position);
        ingredientViewHolder.setIngredientInfo(ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getName());
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) {
            return 0;
        }
        return ingredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView quantityTv;
        TextView measureTv;
        TextView ingredientTv;

        IngredientViewHolder(View itemView) {
            super(itemView);

            quantityTv = itemView.findViewById(R.id.ingredient_quantity);
            measureTv = itemView.findViewById(R.id.ingredient_measure);
            ingredientTv = itemView.findViewById(R.id.ingredient_name);
        }

        void setIngredientInfo(int quantity, String measure, String ingredient) {
            quantityTv.setText(String.valueOf(quantity));
            measureTv.setText(measure);
            ingredientTv.setText(ingredient);
        }
    }
}
