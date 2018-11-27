package com.example.android.bakingapp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bakingapp.provider.IngredientsContract.IngredientEntry;

public class IngredientsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ingredients.db";
    private static final int DATABASE_VERSION = 1;

    IngredientsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                IngredientEntry.COLUMN_INGREDIENT_QUANTITY + " INTEGER NOT NULL, " +
                IngredientEntry.COLUMN_INGREDIENT_MEASURE + " TEXT NOT NULL, " +
                IngredientEntry.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
