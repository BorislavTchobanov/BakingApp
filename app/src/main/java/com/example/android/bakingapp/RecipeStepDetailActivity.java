package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.bakingapp.model.Step;

import static com.example.android.bakingapp.RecipeDetailActivity.CURRENT_STEP_INDEX;
import static com.example.android.bakingapp.StepDetailFragment.ARG_ITEM_ID;
import static com.example.android.bakingapp.StepDetailFragment.BUTTON_NEXT;
import static com.example.android.bakingapp.StepDetailFragment.BUTTON_PREVIOUS;

public class RecipeStepDetailActivity extends AppCompatActivity implements StepDetailFragment.OnNavButtonClickListener {

    private Step step;
    private int stepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        step = (Step) getIntent().getSerializableExtra(ARG_ITEM_ID);
        stepIndex = getIntent().getIntExtra(CURRENT_STEP_INDEX, 0);
        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(StepDetailFragment.ARG_ITEM_ID, step);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, fragment)
                    .commit();
        }

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            // This ID represents the Home or Up button. In the case of this
//            // activity, the Up button is shown. For
//            // more details, see the Navigation pattern on Android Design:
//            //
//            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
//            //
//            navigateUpTo(new Intent(this, RecipeActivity.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onNavButtonClick(String tag) {
        switch (tag) {
            case BUTTON_PREVIOUS:
                Toast.makeText(this, "PREV", Toast.LENGTH_SHORT).show();
                break;
            case BUTTON_NEXT:
                Toast.makeText(this, "NEXT", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}