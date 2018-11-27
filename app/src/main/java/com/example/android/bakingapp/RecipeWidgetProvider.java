package com.example.android.bakingapp;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public static final String TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";
    private static final String EXTRA_QUANTITY = "com.example.android.stackwidget.EXTRA_QUANTITY";
    private static final String EXTRA_MEASURE = "com.example.android.stackwidget.EXTRA_MEASURE";
    private static final String EXTRA_NAME = "com.example.android.stackwidget.EXTRA_NAME";

//    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
//                                int appWidgetId) {
//
////        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_layout);
////        views.setTextViewText(R.id.widgetListView, widgetText);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        IngredientsUpdateService.startActionUpdateIngredientsWidgets(context);
        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }

//        for (int i = 0; i < appWidgetIds.length; ++i) {
//            // Here we setup the intent which points to the StackViewService which will
//            // provide the views for this collection.
//            Intent intent = new Intent(context, StackWidgetService.class);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
//            // When intents are compared, the extras are ignored, so we need to embed the extras
//            // into the data so that the extras will not be ignored.
//            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
//            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_layout);
//            rv.setRemoteAdapter(R.id.widget_list_view, intent);
//            // The empty view is displayed when the collection has no items. It should be a sibling
//            // of the collection view.
//            rv.setEmptyView(R.id.widget_list_view, R.id.empty_view);
//            // Here we setup the a pending intent template. Individuals items of a collection
//            // cannot setup their own pending intents, instead, the collection as a whole can
//            // setup a pending intent template, and the individual items can set a fillInIntent
//            // to create unique before on an item to item basis.
//            Intent toastIntent = new Intent(context, RecipeWidgetProvider.class);
//            toastIntent.setAction(RecipeWidgetProvider.TOAST_ACTION);
//            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
//            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
//            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            rv.setPendingIntentTemplate(R.id.widget_list_view, toastPendingIntent);
//            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
//        }
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
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
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int ingredientQuantity, String ingredientMeasure, String ingredientName, int appWidgetId) {
        // Get current width to decide on single plant vs garden grid view
        RemoteViews rv = getGardenGridRemoteView(context, ingredientQuantity, ingredientMeasure, ingredientName);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private static RemoteViews getGardenGridRemoteView(Context context, int ingredientQuantity, String ingredientMeasure, String ingredientName) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_layout);

        views.setTextViewText(R.id.recipe_name_tv, context.getString(R.string.widget_recipe_title));
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(EXTRA_QUANTITY, ingredientQuantity);
        intent.putExtra(EXTRA_MEASURE, ingredientMeasure);
        intent.putExtra(EXTRA_NAME, ingredientName);
        views.setRemoteAdapter(R.id.widget_list_view, intent);
        // Set the PlantDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);
        // Handle empty gardens
        views.setEmptyView(R.id.widget_list_view, R.id.empty_view);
        return views;
    }
}

