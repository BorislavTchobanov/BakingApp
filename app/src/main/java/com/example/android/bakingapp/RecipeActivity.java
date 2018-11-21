package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utilities.JsonRecipeParser;
import com.example.android.bakingapp.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.example.android.bakingapp.RecipeDetailActivity.EXTRA_RECIPE;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeActivity extends AppCompatActivity implements RecipesAdapter.ListItemClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private List<Recipe> recipeList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

//        if (findViewById(R.id.recipe_detail_container) != null) {
//            // The detail container view will be present only in the
//            // large-screen layouts (res/values-w900dp).
//            // If this view is present, then the
//            // activity should be in two-pane mode.
//            mTwoPane = true;
//        }

        recyclerView = findViewById(R.id.recipe_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        recyclerView.setLayoutManager(gridLayoutManager);

//        assert recyclerView != null;
        new RecipeQueryTask().execute(NetworkUtils.buildUrl());

    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);

        return noOfColumns;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RecipesAdapter(recipeList, this));
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Recipe recipe = recipeList.get(clickedItemIndex);

        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        startActivity(intent);

//        if (mTwoPane) {
//            Bundle arguments = new Bundle();
//            arguments.putSerializable(StepDetailFragment.ARG_ITEM_ID, recipe);
//            StepDetailFragment fragment = new StepDetailFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.recipe_detail_container, fragment)
//                    .commit();
//        } else {
//            Intent intent = new Intent(this, RecipeDetailActivity.class);
//            intent.putExtra(StepDetailFragment.ARG_ITEM_ID, recipe);
//
//            startActivity(intent);
//        }
    }


    public class RecipeQueryTask extends AsyncTask<URL, Void, List<Recipe>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Recipe> doInBackground(URL... params) {
            URL searchUrl = params[0];
            List<Recipe> recipes = null;
            try {
                String response = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                recipes = JsonRecipeParser.parseRecipeJson(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return recipes;
        }

        @Override
        protected void onPostExecute(final List<Recipe> recipes) {
            if (recipes == null) {
                return;
            }
//            populateView(recipes);
            recipeList = recipes;
            setupRecyclerView((RecyclerView) recyclerView);
        }
    }
}
