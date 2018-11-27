package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.model.Recipe;

import static com.example.android.bakingapp.RecipeDetailActivity.EXTRA_CURRENT_STEP_INDEX;
import static com.example.android.bakingapp.RecipeDetailActivity.EXTRA_RECIPE;
import static com.example.android.bakingapp.RecipeDetailActivity.EXTRA_STEP;
import static com.example.android.bakingapp.StepDetailFragment.BUTTON_NEXT;
import static com.example.android.bakingapp.StepDetailFragment.BUTTON_PREVIOUS;

public class RecipeStepDetailActivity extends AppCompatActivity implements StepDetailFragment.OnNavButtonClickListener {

    public static final String EXTRA_AT_FIRST_STEP = "extra_at_first_step";
    public static final String EXTRA_AT_LAST_STEP = "extra_at_last_step";

    private Recipe recipe;
    private int stepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        recipe = (Recipe) getIntent().getSerializableExtra(EXTRA_RECIPE);
        stepIndex = getIntent().getIntExtra(EXTRA_CURRENT_STEP_INDEX, 0);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, buildFragmentNav(stepIndex))
                    .commit();
        }

    }

    private Fragment buildFragmentNav(int index) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(EXTRA_STEP, recipe.getSteps().get(index));
        arguments.putBoolean(EXTRA_AT_FIRST_STEP, (index == 0));
        arguments.putBoolean(EXTRA_AT_LAST_STEP, (index == recipe.getSteps().size() - 1));
        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onStepNavButtonClick(String tag) {

        switch (tag) {
            case BUTTON_PREVIOUS:
                if (stepIndex > 0) {
                    stepIndex--;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, buildFragmentNav(stepIndex))
                            .commit();
                }
                break;
            case BUTTON_NEXT:
                if (stepIndex < recipe.getSteps().size() - 1) {
                    stepIndex++;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, buildFragmentNav(stepIndex))
                            .commit();
                }
                break;
        }
    }
}