package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.provider.IngredientsContract;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.bakingapp.provider.IngredientsContract.BASE_CONTENT_URI;
import static com.example.android.bakingapp.provider.IngredientsContract.PATH_INGREDIENTS;


public class StackWidgetService extends RemoteViewsService {

    public static final String ACTION_UPDATE_INGREDIENTS_WIDGET = "action_update_ingredients_widget";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }

//
//
//    private void updateIngredientsWidget() {
//        //Query to get the plant that's most in need for water (last watered)
//        Uri INGREDIENTS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();
//        Cursor cursor = getContentResolver().query(
//                INGREDIENTS_URI,
//                null,
//                null,
//                null,
//                null
//        );
//        // Extract the plant details
////        int imgRes = R.drawable.grass; // Default image in case our garden is empty
////        boolean canWater = false; // Default to hide the water drop button
//        long ingredientId = INVALID_INGREDIENTS_ID;
//        if (cursor != null && cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            int idIndex = cursor.getColumnIndex(IngredientsContract.IngredientEntry._ID);
//            int quantityIndex = cursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY);
//            int measureIndex = cursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE);
//            int nameIndex = cursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_NAME);
//            ingredientId = cursor.getLong(idIndex);
//            int ingredientQuantity = cursor.getInt(quantityIndex);
//            String ingredientMeasure = cursor.getString(measureIndex);
//            String ingredientName = cursor.getString(nameIndex);
//            cursor.close();
//
//        }
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
//        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
//        //Trigger data update to handle the GridView widgets and force a data refresh
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
//        //Now update all widgets
//        RecipeWidgetProvider.updatePlantWidgets(this, appWidgetManager, ingredientId,appWidgetIds);
//    }



}
class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {


    Context mContext;
    Cursor mCursor;

    public StackRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        // Get all plant info ordered by creation time
        Uri PLANT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(
                PLANT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);
//        int idIndex = mCursor.getColumnIndex(IngredientsContract.IngredientEntry._ID);
        int quantityIndex = mCursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY);
        int measureIndex = mCursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE);
        int nameIndex = mCursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_NAME);
//        ingredientId = mCursor.getLong(idIndex);
        int ingredientQuantity = mCursor.getInt(quantityIndex);
        String ingredientMeasure = mCursor.getString(measureIndex);
        String ingredientName = mCursor.getString(nameIndex);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);

        // Update the plant image
//        int imgRes = PlantUtils.getPlantImageRes(mContext, timeNow - createdAt, timeNow - wateredAt, plantType);
//        views.setImageViewResource(R.id.widget_plant_image, imgRes);
        views.setTextViewText(R.id.ingredient_quantity, String.valueOf(ingredientQuantity));
        views.setTextViewText(R.id.ingredient_measure, ingredientMeasure);
        views.setTextViewText(R.id.ingredient_name, ingredientName);

//        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
//        Bundle extras = new Bundle();
//        extras.putLong(PlantDetailActivity.EXTRA_PLANT_ID, plantId);
//        Intent fillInIntent = new Intent();
//        fillInIntent.putExtras(extras);
//        views.setOnClickFillInIntent(R.id.widget_plant_image, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }



//    private static final int mCount = 10;
//    private List<Ingredient> mWidgetItems = new ArrayList<Ingredient>();
//    private Context mContext;
//    private int mAppWidgetId;
//    public StackRemoteViewsFactory(Context context, Intent intent) {
//        mContext = context;
//        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
//                AppWidgetManager.INVALID_APPWIDGET_ID);
//    }
//    public void onCreate() {
//        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
//        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
//        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
//        for (int i = 0; i < mCount; i++) {
//            mWidgetItems.add(new Ingredient(i, "name" + i, "kg"));
//        }
//        // We sleep for 3 seconds here to show how the empty view appears in the interim.
//        // The empty view is set in the StackWidgetProvider and should be a sibling of the
//        // collection view.
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//    public void onDestroy() {
//        // In onDestroy() you should tear down anything that was setup for your data source,
//        // eg. cursors, connections, etc.
//        mWidgetItems.clear();
//    }
//    public int getCount() {
//        if (mCursor == null) return 0;
//        return mCursor.getCount();
//    }
//    public RemoteViews getViewAt(int position) {
//
//        if (mCursor == null || mCursor.getCount() == 0) return null;
//        mCursor.moveToPosition(position);
//        int idIndex = mCursor.getColumnIndex(PlantContract.PlantEntry._ID);
//        int createTimeIndex = mCursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_CREATION_TIME);
//        int waterTimeIndex = mCursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME);
//        int plantTypeIndex = mCursor.getColumnIndex(PlantContract.PlantEntry.COLUMN_PLANT_TYPE);
//
//        long plantId = mCursor.getLong(idIndex);
//        int plantType = mCursor.getInt(plantTypeIndex);
//        long createdAt = mCursor.getLong(createTimeIndex);
//        long wateredAt = mCursor.getLong(waterTimeIndex);
//        long timeNow = System.currentTimeMillis();
//
//        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.plant_widget);
//
//        // Update the plant image
//        int imgRes = PlantUtils.getPlantImageRes(mContext, timeNow - createdAt, timeNow - wateredAt, plantType);
//        views.setImageViewResource(R.id.widget_plant_image, imgRes);
//        views.setTextViewText(R.id.widget_plant_name, String.valueOf(plantId));
//        // Always hide the water drop in GridView mode
//        views.setViewVisibility(R.id.widget_water_button, View.GONE);
//
//        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
//        Bundle extras = new Bundle();
//        extras.putLong(PlantDetailActivity.EXTRA_PLANT_ID, plantId);
//        Intent fillInIntent = new Intent();
//        fillInIntent.putExtras(extras);
//        views.setOnClickFillInIntent(R.id.widget_plant_image, fillInIntent);
//
//        return views;
////        // position will always range from 0 to getCount() - 1.
////        // We construct a remote views item based on our widget item xml file, and set the
////        // text based on the position.
////        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);
////        rv.setTextViewText(R.id.ingredient_name, mWidgetItems.get(position).getName());
////        // Next, we set a fill-intent which will be used to fill-in the pending intent template
////        // which is set on the collection view in StackWidgetProvider.
////        Bundle extras = new Bundle();
////        extras.putInt(RecipeWidgetProvider.EXTRA_ITEM, position);
////        Intent fillInIntent = new Intent();
////        fillInIntent.putExtras(extras);
////        rv.setOnClickFillInIntent(R.id.ingredient_name, fillInIntent);
////        // You can do heaving lifting in here, synchronously. For example, if you need to
////        // process an image, fetch something from the network, etc., it is ok to do it here,
////        // synchronously. A loading view will show up in lieu of the actual contents in the
////        // interim.
////        try {
////            System.out.println("Loading view " + position);
////            Thread.sleep(500);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////        // Return the remote views object.
////        return rv;
//    }
//    public RemoteViews getLoadingView() {
//        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
//        // return null here, you will get the default loading view.
//        return null;
//    }
//    public int getViewTypeCount() {
//        return 1;
//    }
//    public long getItemId(int position) {
//        return position;
//    }
//    public boolean hasStableIds() {
//        return true;
//    }
//    public void onDataSetChanged() {
//        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
//        // on the collection view corresponding to this factory. You can do heaving lifting in
//        // here, synchronously. For example, if you need to process an image, fetch something
//        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
//        // in its current state while work is being done here, so you don't need to worry about
//        // locking up the widget.
//    }
}