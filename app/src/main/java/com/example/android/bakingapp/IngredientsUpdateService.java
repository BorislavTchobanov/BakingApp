package com.example.android.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.bakingapp.provider.IngredientsContract;

import static com.example.android.bakingapp.provider.IngredientsContract.PATH_INGREDIENTS;


public class IngredientsUpdateService extends IntentService {

    private static final String ACTION_UPDATE_INGREDIENTS_WIDGETS = "com.example.android.bakingapp.action.update_ingredients_widgets";

    public IngredientsUpdateService() {
        super("IngredientsUpdateService");
    }

    public static void startActionUpdateIngredientsWidgets(Context context) {
        Intent intent = new Intent(context, IngredientsUpdateService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_INGREDIENTS_WIDGETS.equals(action)) {
                handleActionUpdateIngredientWidgets();
            }
        }
    }

    private void handleActionUpdateIngredientWidgets() {
        Uri INGREDIENTS_URI = IngredientsContract.BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();
        Cursor cursor = getContentResolver().query(
                INGREDIENTS_URI,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int quantityIndex = cursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY);
            int measureIndex = cursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE);
            int nameIndex = cursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_NAME);
            int ingredientQuantity = cursor.getInt(quantityIndex);
            String ingredientMeasure = cursor.getString(measureIndex);
            String ingredientName = cursor.getString(nameIndex);
            cursor.close();


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);

            RecipeWidgetProvider.updateIngredientsWidgets(this, appWidgetManager, ingredientQuantity, ingredientMeasure, ingredientName, appWidgetIds);
        }
    }
}
