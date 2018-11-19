package com.example.android.bakingapp.utilities;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class JsonIngredientParser {

    public static List<Ingredient> parseIngredientJson(String json) {

        List<Ingredient> ingredients = null;
        int quantity;
        String name;
        String measure;

        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(json);


            JSONArray results = jsonObj.getJSONArray("ingredients");
            ingredients = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieObj = results.getJSONObject(i);

                quantity = movieObj.getInt("quantity");
                name = movieObj.getString("ingredient");
                measure = movieObj.getString("measure");

                ingredients.add(i, new Ingredient(quantity, name, measure));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ingredients;
    }
}
