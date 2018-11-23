package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.List;

import static com.example.android.bakingapp.StepDetailFragment.BUTTON_NEXT;
import static com.example.android.bakingapp.StepDetailFragment.BUTTON_PREVIOUS;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.ListItemClickListener, StepDetailFragment.OnNavButtonClickListener {

    public static final String EXTRA_RECIPE = "extra_recipe";
    public static final String CURRENT_STEP_INDEX = "current_step_index";
    private boolean mTwoPane;
    private Recipe recipe;
    private List<Step> stepList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
//        setSupportActionBar(toolbar);

        recipe = (Recipe) getIntent().getSerializableExtra(EXTRA_RECIPE);
        stepList = recipe.getSteps();
        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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

        recyclerView = findViewById(R.id.step_list);
        recyclerView.setAdapter(new StepsAdapter(stepList, this));

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
            arguments.putSerializable(EXTRA_RECIPE, recipe);
            arguments.putInt(CURRENT_STEP_INDEX, clickedItemIndex);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(StepDetailFragment.ARG_ITEM_ID, step);
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

    @Override
    public void onNavButtonClick(String tag) {
        switch (tag) {
            case BUTTON_PREVIOUS:
                Toast.makeText(this, "Rec Det PREV", Toast.LENGTH_SHORT).show();
                break;
            case BUTTON_NEXT:
                Toast.makeText(this, "Rec Det NEXT", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
