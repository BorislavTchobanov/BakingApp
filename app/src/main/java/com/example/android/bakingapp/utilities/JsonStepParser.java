package com.example.android.bakingapp.utilities;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class JsonStepParser {

    public static List<Step> parseStepJson(String json) {

        List<Step> steps = null;
        int id;
        String shortDescription;
        String description;
        String videoUrl;
        String thumbnailUrl;

        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(json);


            JSONArray results = jsonObj.getJSONArray("steps");
            steps = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieObj = results.getJSONObject(i);

                id = movieObj.getInt("id");
                shortDescription = movieObj.getString("shortDescription");
                description = movieObj.getString("description");
                videoUrl = movieObj.getString("videoURL");
                thumbnailUrl = movieObj.getString("thumbnailURL");

                steps.add(i, new Step(id, shortDescription, description, videoUrl, thumbnailUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return steps;
    }
}
