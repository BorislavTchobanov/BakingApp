package com.example.android.bakingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.provider.IngredientsContract;

import java.util.List;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.ListItemClickListener {

    public static final String EXTRA_RECIPE = "extra_recipe";
    public static final String CURRENT_STEP_INDEX = "current_step_index";
    public static final String EXTRA_TWO_PANE = "extra_two_pane";
    public static final String EXTRA_AT_FIRST_STEP = "extra_at_first_step";
    public static final String EXTRA_AT_LAST_STEP = "extra_at_last_step";
    private boolean mTwoPane;
    private Recipe recipe;
    private List<Step> stepList;
    private RecyclerView stepsRecyclerView;
    private List<Ingredient> ingredientList;
    private RecyclerView ingredientsRecyclerView;

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

        getContentResolver().delete(IngredientsContract.IngredientEntry.CONTENT_URI, null, null);
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i <ingredientList.size(); i++) {
            contentValues.put(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_QUANTITY, ingredientList.get(i).getQuantity());
            contentValues.put(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_MEASURE, ingredientList.get(i).getMeasure());
            contentValues.put(IngredientsContract.IngredientEntry.COLUMN_INGREDIENT_NAME, ingredientList.get(i).getName());
            getContentResolver().insert(IngredientsContract.IngredientEntry.CONTENT_URI, contentValues);
        }
        IngredientsUpdateService.startActionUpdatePlantWidgets(this);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
//        if (savedInstanceState == null) {
//            // Create the detail fragment and add it to the activity
//            // using a fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putSerializable(StepDetailFragment.ARG_ITEM_ID, (Serializable) recipe.getSteps());
//            StepDetailFragment fragment = new StepDetailFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.recipe_detail_container, fragment)
//                    .commit();
//        }

        ingredientsRecyclerView = findViewById(R.id.ingredients_list);
        ingredientsRecyclerView.setAdapter(new IngredientsAdapter(ingredientList));
        stepsRecyclerView = findViewById(R.id.steps_list);
        stepsRecyclerView.setAdapter(new StepsAdapter(stepList, this));

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Step step = recipe.getSteps().get(clickedItemIndex);

//        Intent intent = new Intent(this, RecipeDetailActivity.class);
//        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);
//        startActivity(intent);

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(StepDetailFragment.ARG_ITEM_ID, step);
            arguments.putBoolean(EXTRA_AT_FIRST_STEP, (clickedItemIndex == 0));
            arguments.putBoolean(EXTRA_AT_LAST_STEP, (clickedItemIndex == recipe.getSteps().size() - 1));
            arguments.putSerializable(EXTRA_RECIPE, recipe);
            arguments.putInt(CURRENT_STEP_INDEX, clickedItemIndex);
            arguments.putBoolean(EXTRA_TWO_PANE, mTwoPane);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(EXTRA_RECIPE, recipe);
            intent.putExtra(CURRENT_STEP_INDEX, clickedItemIndex);

            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, RecipeActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
