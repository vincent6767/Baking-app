package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.RecipeStepsAdapter;
import com.example.android.bakingapp.entities.Recipe;
import com.example.android.bakingapp.entities.RecipeStep;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeMasterListFragment.OnInitializationListener, RecipeStepsAdapter.OnRecipeStepClickListener {
    private static final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();
    private Recipe mRecipe;
    // TODO: Preserve in OnSavedInstanceState
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
        mTwoPane = (findViewById(R.id.recipe_step_detail_linear_layout) != null);

        if (mTwoPane) {
            // Only Create new fragments when there is no previously saved state.
            if (savedInstanceState == null && mSelectedRecipeStep != null) {
                RecipeStepDetailFragment recipeStepDetailFragment = RecipeStepDetailFragment.newInstance(mSelectedRecipeStep);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_step_detail_container, recipeStepDetailFragment)
                        .commit();
            }
        }
    }
    @Override
    public Recipe getInitialRecipe() {
        return mRecipe;
    }

    @Override
    public void onRecipeStepSelected(RecipeStep recipeStep) {
        mSelectedRecipeStep = recipeStep;
        if (!mTwoPane) {
            Intent startRecipeStepDetailActivityIntent = new Intent(getApplicationContext(), RecipeStepDetailActivity.class);
            startRecipeStepDetailActivityIntent.putExtra(RecipeStepDetailActivity.RECIPE_STEP_KEY, mSelectedRecipeStep);
            startRecipeStepDetailActivityIntent.putExtra(RecipeStepDetailActivity.RECIPE_NAME_KEY, mRecipe.getName());
            Log.d(LOG_TAG, "Starting Recipe Step Detail Intent");
            startActivity(startRecipeStepDetailActivityIntent);
        } else if (mSelectedRecipeStep != null) {
            RecipeStepDetailFragment recipeStepDetailFragment = RecipeStepDetailFragment.newInstance(mSelectedRecipeStep);
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_step_detail_container, recipeStepDetailFragment).commit();
        } else {
            Log.d(LOG_TAG, "Passed Recipe Step value is null");
        }
    }
}
