package com.example.android.bakingapp;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.entities.Ingredient;
import com.example.android.bakingapp.entities.Recipe;
import com.example.android.bakingapp.entities.RecipeStep;
import com.example.android.bakingapp.ui.RecipeDetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.android.bakingapp.ui.RecipesListActivity.SELECTED_RECIPE_KEY;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {
    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityTestRule = new ActivityTestRule<>(RecipeDetailActivity.class, true, false);

    @Before
    public void setupRecipeDetailActivity() {
        List<Ingredient> ingredientList = new ArrayList<>();
        Ingredient ingredient = new Ingredient(2.0f, "CUP", "Graham Cracker crumbs");
        ingredientList.add(ingredient);
        List<RecipeStep> recipeSteps = new ArrayList<>();
        RecipeStep firstStep = new RecipeStep(0, "Recipe Introduction",
                "Recipe Introduction",
                "\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4\"",
                ""
        );
        RecipeStep secondStep = new RecipeStep(1, "Starting prep",
                "1. Preheat the oven to 350°F. Butter a 9\\\" deep dish pie pan.",
                "",
                ""
        );

        recipeSteps.add(firstStep);
        recipeSteps.add(secondStep);

        Recipe recipe = new Recipe(1, "Nutella Pie", ingredientList, recipeSteps, 8, "");

        Intent recipeDetailIntent = new Intent();

        recipeDetailIntent.putExtra(SELECTED_RECIPE_KEY, recipe);
        mActivityTestRule.launchActivity(recipeDetailIntent);
    }

    @Test
    public void clickRecipeStep_OpensRecipeStepDetailActivity() {
        onView(withId(R.id.rv_items_list)).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));

        onView(withId(R.id.tv_recipe_step_description)).check(matches(isDisplayed()));
    }

}
