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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utilities.InternetCheck;
import com.example.android.bakingapp.utilities.JsonRecipeParser;
import com.example.android.bakingapp.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.example.android.bakingapp.RecipeDetailActivity.EXTRA_RECIPE;

public class RecipeActivity extends AppCompatActivity implements RecipesAdapter.ListItemClickListener {

    private List<Recipe> recipeList;
    private RecyclerView recyclerView;
    private TextView errorNoInternetTv;
    private Button errorNoInternetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        errorNoInternetTv = findViewById(R.id.no_internet_error);
        errorNoInternetBtn = findViewById(R.id.no_internet_error_btn);
        errorNoInternetBtn.setOnClickListener(view -> retrieveRecipes());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        recyclerView = findViewById(R.id.recipe_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        recyclerView.setLayoutManager(gridLayoutManager);

        retrieveRecipes();
    }

    private void retrieveRecipes() {
        new InternetCheck((Boolean internet) -> {
            if (internet) {
                hideNoInternetConnectionError();
                new RecipeQueryTask().execute(NetworkUtils.buildUrl());
            } else {
                showNoInternetConnectionError();
            }
        });
    }

    private void showNoInternetConnectionError() {
        errorNoInternetTv.setVisibility(View.VISIBLE);
        errorNoInternetBtn.setVisibility(View.VISIBLE);
    }

    private void hideNoInternetConnectionError() {
        errorNoInternetTv.setVisibility(View.GONE);
        errorNoInternetBtn.setVisibility(View.GONE);
    }

    private static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;

        return (int) (dpWidth / scalingFactor);
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
    }


    class RecipeQueryTask extends AsyncTask<URL, Void, List<Recipe>> {

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
            recipeList = recipes;
            setupRecyclerView(recyclerView);
        }
    }
}
