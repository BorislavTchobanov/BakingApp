package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.provider.IngredientsContract;

import static com.example.android.bakingapp.provider.IngredientsContract.BASE_CONTENT_URI;
import static com.example.android.bakingapp.provider.IngredientsContract.PATH_INGREDIENTS;


public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;

    ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Uri INGREDIENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(
                INGREDIENT_URI,
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

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);

        int quantityIndex = mCursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY);
        int measureIndex = mCursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE);
        int nameIndex = mCursor.getColumnIndex(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_NAME);

        int ingredientQuantity = mCursor.getInt(quantityIndex);
        String ingredientMeasure = mCursor.getString(measureIndex);
        String ingredientName = mCursor.getString(nameIndex);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);

        views.setTextViewText(R.id.ingredient_quantity, String.valueOf(ingredientQuantity));
        views.setTextViewText(R.id.ingredient_measure, ingredientMeasure);
        views.setTextViewText(R.id.ingredient_name, ingredientName);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}