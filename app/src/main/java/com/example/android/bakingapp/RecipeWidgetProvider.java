package com.example.android.bakingapp;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;


public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String EXTRA_QUANTITY = "com.example.android.stackwidget.EXTRA_QUANTITY";
    private static final String EXTRA_MEASURE = "com.example.android.stackwidget.EXTRA_MEASURE";
    private static final String EXTRA_NAME = "com.example.android.stackwidget.EXTRA_NAME";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        IngredientsUpdateService.startActionUpdateIngredientsWidgets(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateIngredientsWidgets(Context context, AppWidgetManager appWidgetManager,
                                                int ingredientQuantity, String ingredientMeasure, String ingredientName, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, ingredientQuantity, ingredientMeasure, ingredientName, appWidgetId);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int ingredientQuantity, String ingredientMeasure, String ingredientName, int appWidgetId) {

        RemoteViews rv = getIngredientsListRemoteView(context, ingredientQuantity, ingredientMeasure, ingredientName);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private static RemoteViews getIngredientsListRemoteView(Context context, int ingredientQuantity, String ingredientMeasure, String ingredientName) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_layout);

        views.setTextViewText(R.id.recipe_name_tv, context.getString(R.string.widget_recipe_title));

        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra(EXTRA_QUANTITY, ingredientQuantity);
        intent.putExtra(EXTRA_MEASURE, ingredientMeasure);
        intent.putExtra(EXTRA_NAME, ingredientName);
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);

        views.setEmptyView(R.id.widget_list_view, R.id.empty_view);
        return views;
    }
}

