package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.Recipe;
import com.example.android.bakingapp.entities.RecipeStep;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeMasterListFragment.OnInitializationListener,
        RecipeMasterListFragment.OnRecipeStepSelectedListener,
        RecipeStepDetailFragment.OnChangeRecipeStepListener {
    private static final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();
    private static final String RECIPE_KEY = "recipe";
    private static final String SELECTED_RECIPE_KEY = "selectedRecipe";
    private static final String TWO_PANE_STATE_KEY = "twoPane";
    private static final String RECIPE_DETAIL_FRAGMENT_KEY = "recipeDetailFragmentKey";

    private Recipe mRecipe;
    private RecipeStep mSelectedRecipeStep;
    private boolean mTwoPane;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ActionBar actionBar = getSupportActionBar();

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(RecipesListActivity.SELECTED_RECIPE_KEY)) {
                mRecipe = intentThatStartedThisActivity.getParcelableExtra(RecipesListActivity.SELECTED_RECIPE_KEY);
            }
        }
        if (actionBar != null) {
            actionBar.setTitle(mRecipe.getName());
        }
        if (savedInstanceState == null) {
            mTwoPane = (findViewById(R.id.recipe_step_detail_linear_layout) != null);
            if (mTwoPane) {
                // Only Create new fragments when there is no previously saved state and the selected recipe is not null
                if (mSelectedRecipeStep != null) {
                    RecipeStep previousStep = mRecipe.getPreviousStepBefore(mSelectedRecipeStep.getId());
                    RecipeStep nextStep = mRecipe.getNextStepAfter(mSelectedRecipeStep.getId());
                    RecipeStepDetailFragment recipeStepDetailFragment = RecipeStepDetailFragment.newInstance(mSelectedRecipeStep, previousStep, nextStep, this);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.recipe_step_detail_container, recipeStepDetailFragment, RECIPE_DETAIL_FRAGMENT_KEY)
                            .commit();
                    Log.d(LOG_TAG, "Created new fragment");
                } else {
                    // TODO: Handle "no selected recipe" condition
                    Log.e(LOG_TAG, "No selected recipe. Cannot show fragment and do nothing.");
                }
            }
        } else {
            // fragments will automatically preserved. You don't need to initialize it again.
            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
            mSelectedRecipeStep = savedInstanceState.getParcelable(SELECTED_RECIPE_KEY);
            mTwoPane = savedInstanceState.getBoolean(TWO_PANE_STATE_KEY);
            // You need to change the listener because the previous activity has been killed.
            if (mTwoPane) {
                RecipeStepDetailFragment fragment = (RecipeStepDetailFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_DETAIL_FRAGMENT_KEY);
                if (fragment != null) {
                    fragment.setOnChangeRecipeStepListener(this);
                }
            }
        }
    }
    @Override
    public Recipe getInitialRecipe() {
        return mRecipe;
    }

    @Override
    public void onSelected(RecipeStep recipeStep) {
        mSelectedRecipeStep = recipeStep;
        RecipeStep previousStep = mRecipe.getPreviousStepBefore(mSelectedRecipeStep.getId());
        RecipeStep nextStep = mRecipe.getNextStepAfter(mSelectedRecipeStep.getId());

        if (!mTwoPane) {
            Intent startRecipeStepDetailActivityIntent = new Intent(getApplicationContext(), RecipeStepDetailActivity.class);
            startRecipeStepDetailActivityIntent.putExtra(RecipeStepDetailActivity.RECIPE_STEP_KEY, mSelectedRecipeStep);
            startRecipeStepDetailActivityIntent.putExtra(RecipeStepDetailActivity.RECIPE_KEY, mRecipe);
            Log.d(LOG_TAG, "Send Recipe Step Detail Intent");
            startActivity(startRecipeStepDetailActivityIntent);
        } else if (mSelectedRecipeStep != null) {
            RecipeStepDetailFragment recipeStepDetailFragment = RecipeStepDetailFragment.newInstance(mSelectedRecipeStep, previousStep, nextStep, this);
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_step_detail_container, recipeStepDetailFragment, RECIPE_DETAIL_FRAGMENT_KEY).commit();
        } else {
            // TODO: Handle this.
            Log.e(LOG_TAG, "Passed Recipe Step value is null");
        }
    }
    @Override
    public void onChange(RecipeStep recipeStep) {
        if (recipeStep != null) {
            onSelected(recipeStep);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_KEY, mRecipe);
        outState.putParcelable(SELECTED_RECIPE_KEY, mSelectedRecipeStep);
        outState.putBoolean(TWO_PANE_STATE_KEY, mTwoPane);
        super.onSaveInstanceState(outState);
    }
}
