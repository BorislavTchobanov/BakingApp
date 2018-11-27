package com.example.android.bakingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.provider.IngredientsContract;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.ListItemClickListener {

    public static final String EXTRA_RECIPE = "extra_recipe";
    public static final String EXTRA_STEP = "extra_step";
    public static final String EXTRA_CURRENT_STEP_INDEX = "extra_current_step_index";
    public static final String EXTRA_TWO_PANE = "extra_two_pane";

    private List<Ingredient> ingredientList;
    private List<Step> stepList;
    private boolean mTwoPane;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipe = (Recipe) getIntent().getSerializableExtra(EXTRA_RECIPE);
        ingredientList = recipe.getIngredients();
        stepList = recipe.getSteps();

        mTwoPane = findViewById(R.id.recipe_detail_container) != null;

        if (mTwoPane) {
            Toolbar toolbar = findViewById(R.id.detail_toolbar);
            setSupportActionBar(toolbar);
            toolbar.setTitle(recipe.getName());
        } else {
            CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(recipe.getName());
        }

        populateWidgetsDB();
        populateIngredientsView();
        populateStepsView();
    }

    private void populateWidgetsDB() {
        getContentResolver().delete(IngredientsContract.IngredientEntry.CONTENT_URI, null, null);
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < ingredientList.size(); i++) {
            contentValues.put(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY, ingredientList.get(i).getQuantity());
            contentValues.put(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE, ingredientList.get(i).getMeasure());
            contentValues.put(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_NAME, ingredientList.get(i).getName());
            getContentResolver().insert(IngredientsContract.IngredientEntry.CONTENT_URI, contentValues);
        }
        IngredientsUpdateService.startActionUpdateIngredientsWidgets(this);
    }

    private void populateIngredientsView() {
        RecyclerView ingredientsRecyclerView = findViewById(R.id.ingredients_list);
        ingredientsRecyclerView.setAdapter(new IngredientsAdapter(ingredientList));
    }

    private void populateStepsView() {
        RecyclerView stepsRecyclerView = findViewById(R.id.steps_list);
        stepsRecyclerView.setAdapter(new StepsAdapter(stepList, this));
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Step step = recipe.getSteps().get(clickedItemIndex);

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(EXTRA_RECIPE, recipe);
            arguments.putSerializable(EXTRA_STEP, step);
            arguments.putInt(EXTRA_CURRENT_STEP_INDEX, clickedItemIndex);
            arguments.putBoolean(EXTRA_TWO_PANE, mTwoPane);

            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(EXTRA_RECIPE, recipe);
            intent.putExtra(EXTRA_CURRENT_STEP_INDEX, clickedItemIndex);

            startActivity(intent);
        }
    }

}
