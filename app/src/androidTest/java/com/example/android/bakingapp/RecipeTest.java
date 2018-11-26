package com.example.android.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.android.bakingapp.RecipeDetailActivity.EXTRA_RECIPE;

@RunWith(AndroidJUnit4.class)
public class RecipeTest {

    private final static int MOCK_RECIPE_ID = 0;
    private final static String MOCK_RECIPE_NAME = "Carrot cake";
    private final static int MOCK_RECIPE_SERVINGS = 8;
    private final static String MOCK_RECIPE_THUMBNAIL = "";

    private final static int MOCK_INGREDIENT_QUANTITY = 1;
    private final static String MOCK_INGREDIENT_MEASURE = "UNIT";
    private final static String MOCK_INGREDIENT_NAME = "Carrot";

    private final static int MOCK_STEP_ID = 0;
    private final static String MOCK_STEP_DESCRIPTION_SHORT = "Recipe Description Short";
    private final static String MOCK_STEP_DESCRIPTION_LONG = "Recipe Description Long";
    private final static String MOCK_STEP_VIDEO = "";
    private final static String MOCK_STEP_THUMBNAIL = "";


    @Rule
    public ActivityTestRule<RecipeActivity> mRecipeTestRule = new ActivityTestRule<>(RecipeActivity.class);

    @Before
    public void init() {
        final Ingredient ingredient = new Ingredient(MOCK_INGREDIENT_QUANTITY, MOCK_INGREDIENT_MEASURE, MOCK_INGREDIENT_NAME);
        final Step step = new Step(MOCK_STEP_ID, MOCK_STEP_DESCRIPTION_SHORT, MOCK_STEP_DESCRIPTION_LONG, MOCK_STEP_VIDEO, MOCK_STEP_THUMBNAIL);
        final Recipe recipe = new Recipe(MOCK_RECIPE_ID, MOCK_RECIPE_NAME, Collections.singletonList(ingredient),
                Collections.singletonList(step), MOCK_RECIPE_SERVINGS, MOCK_RECIPE_THUMBNAIL);

        final Intent intent = new Intent();
        intent.putExtra(EXTRA_RECIPE, recipe);
        mRecipeTestRule.launchActivity(intent);
    }

    @Test
    public void checkListIsVisible() {
        onView(withId(R.id.recipe_list))
                .check(matches(atPosition(0, isDisplayed())));
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}
