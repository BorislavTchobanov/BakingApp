package com.example.android.bakingapp;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.bakingapp.provider.IngredientsContract;


import static com.example.android.bakingapp.provider.IngredientsContract.INVALID_INGREDIENTS_ID;
import static com.example.android.bakingapp.provider.IngredientsContract.PATH_INGREDIENTS;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class IngredientsUpdateService extends IntentService {

    public static final String ACTION_WATER_PLANT = "com.example.android.mygarden.action.water_plant";
    public static final String ACTION_UPDATE_PLANT_WIDGETS = "com.example.android.mygarden.action.update_plant_widgets";
    public static final String EXTRA_PLANT_ID = "com.example.android.mygarden.extra.PLANT_ID";;

    public IngredientsUpdateService() {
        super("IngredientsUpdateService");
    }

    /**
     * Starts this service to perform UpdatePlantWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdatePlantWidgets(Context context) {
        Intent intent = new Intent(context, IngredientsUpdateService.class);
        intent.setAction(ACTION_UPDATE_PLANT_WIDGETS);
        context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_PLANT_WIDGETS.equals(action)) {
                handleActionUpdatePlantWidgets();
            }
        }
    }


    /**
     * Handle action UpdatePlantWidgets in the provided background thread
     */
    private void handleActionUpdatePlantWidgets() {
        //Query to get the plant that's most in need for water (last watered)
        Uri INGREDIENTS_URI = IngredientsContract.BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();
        Cursor cursor = getContentResolver().query(
                INGREDIENTS_URI,
                null,
                null,
                null,
                null
        );
        // Extract the plant details
//        int imgRes = R.drawable.grass; // Default image in case our garden is empty
//        boolean canWater = false; // Default to hide the water drop button
        long ingredientId = INVALID_INGREDIENTS_ID;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int idIndex = cursor.getColumnIndex(IngredientsContract.IngredientEntry._ID);
            int quantityIndex = cursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY);
            int measureIndex = cursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE);
            int nameIndex = cursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_NAME);
            ingredientId = cursor.getLong(idIndex);
            int ingredientQuantity = cursor.getInt(quantityIndex);
            String ingredientMeasure = cursor.getString(measureIndex);
            String ingredientName = cursor.getString(nameIndex);
            cursor.close();


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
            //Trigger data update to handle the GridView widgets and force a data refresh
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
            //Now update all widgets
            RecipeWidgetProvider.updatePlantWidgets(this, appWidgetManager, ingredientQuantity, ingredientMeasure, ingredientName, appWidgetIds);
        }
    }
}
