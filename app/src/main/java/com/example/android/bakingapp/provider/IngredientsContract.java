package com.example.android.bakingapp.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class IngredientsContract {

    static final String AUTHORITY = "com.example.android.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_INGREDIENTS = "ingredients";

    public static final long INVALID_INGREDIENTS_ID = -1;

    public static final class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        static final String TABLE_NAME = "ingredients";
        public static final String COLUMN_INGREDIENT_QUANTITY = "ingredientQuantity";
        public static final String COLUMN_INGREDIENT_MEASURE = "ingredientMeasure";
        public static final String COLUMN_INGREDIENT_NAME = "ingredientName";
    }
}
