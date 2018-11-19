package com.example.android.bakingapp.utilities;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonRecipeParser {

    public static List<Recipe> parseRecipeJson(String json) {

        List<Recipe> recipes = null;
        int recipeId;
        String recipeName;
        List<Ingredient> ingredients;
        List<Step> steps;
        int servings;
        String image;

        int quantity;
        String ingredientName;
        String measure;

        int stepId;
        String shortDescription;
        String description;
        String videoUrl;
        String thumbnailUrl;

        JSONArray jsonArrayRecipe;
        try {
            jsonArrayRecipe = new JSONArray(json);
            recipes = new ArrayList<>();
            for(int i = 0; i < jsonArrayRecipe.length(); i++) {
                JSONObject jsonObjRecipe = jsonArrayRecipe.getJSONObject(i);

                recipeId = jsonObjRecipe.getInt("id");
                recipeName = jsonObjRecipe.getString("name");
                servings = jsonObjRecipe.getInt("servings");
                image = jsonObjRecipe.getString("image");

                JSONArray jsonArrayIngredients = jsonObjRecipe.getJSONArray("ingredients");
                ingredients = new ArrayList<>();
                for (int j = 0; j < jsonArrayIngredients.length(); j++) {
                    JSONObject jsonObjIngredient = jsonArrayIngredients.getJSONObject(j);

                    quantity = jsonObjIngredient.getInt("quantity");
                    ingredientName = jsonObjIngredient.getString("ingredient");
                    measure = jsonObjIngredient.getString("measure");

                    ingredients.add(j, new Ingredient(quantity, ingredientName, measure));
                }

                JSONArray jsonArraySteps = jsonObjRecipe.getJSONArray("steps");
                steps = new ArrayList<>();
                for (int k = 0; k < jsonArraySteps.length(); k++) {
                    JSONObject jsonObjStep = jsonArraySteps.getJSONObject(k);

                    stepId = jsonObjStep.getInt("id");
                    shortDescription = jsonObjStep.getString("shortDescription");
                    description = jsonObjStep.getString("description");
                    videoUrl = jsonObjStep.getString("videoURL");
                    thumbnailUrl = jsonObjStep.getString("thumbnailURL");

                    steps.add(k, new Step(stepId, shortDescription, description, videoUrl, thumbnailUrl));
                }

                recipes.add(i, new Recipe(recipeId, recipeName, ingredients, steps, servings, image));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipes;
    }
}
