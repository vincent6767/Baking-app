package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.Recipe;
import com.example.android.bakingapp.entities.RecipeStep;

public class RecipeStepDetailActivity extends AppCompatActivity implements RecipeStepDetailFragment.OnChangeRecipeStepListener {
    public static final String RECIPE_STEP_KEY = "recipeStep";
    public static final String RECIPE_KEY = "recipe";
    private static final String LOG_TAG = RecipeStepDetailActivity.class.getSimpleName();
    private static final String RECIPE_STEP_DETAIL_FRAGMENT_TAG = RecipeStepDetailFragment.class.getSimpleName();
    private RecipeStep mSelectedRecipeStep;
    private Recipe mRecipe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int displayMode = getResources().getConfiguration().orientation;
        ActionBar actionBar = getSupportActionBar();

        if (displayMode == Configuration.ORIENTATION_LANDSCAPE) {
            if (actionBar != null) {
                actionBar.hide();
            }
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            if (actionBar != null) {
                actionBar.show();
            }
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_recipe_step_detail);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.hasExtra(RECIPE_STEP_KEY) && intent.hasExtra(RECIPE_KEY)) {
                    mSelectedRecipeStep = intent.getParcelableExtra(RECIPE_STEP_KEY);
                    mRecipe = intent.getParcelableExtra(RECIPE_KEY);
                    RecipeStep nextStep = mRecipe.getNextStepAfter(mSelectedRecipeStep.getId());
                    RecipeStep previousStep = mRecipe.getPreviousStepBefore(mSelectedRecipeStep.getId());
                    RecipeStepDetailFragment fragment = RecipeStepDetailFragment.newInstance(mSelectedRecipeStep, previousStep, nextStep, this);

                    FragmentManager manager = getSupportFragmentManager();

                    manager.beginTransaction()
                            .add(R.id.recipe_step_detail_container, fragment, RECIPE_STEP_DETAIL_FRAGMENT_TAG)
                            .commit();
                    Log.d(LOG_TAG, "Created new fragment");
                    if (actionBar != null) {
                        actionBar.setTitle(mRecipe.getName());
                        actionBar.setDisplayHomeAsUpEnabled(true);
                    }
                }
            }
        } else {
            mSelectedRecipeStep = savedInstanceState.getParcelable(RECIPE_STEP_KEY);
            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
            RecipeStepDetailFragment fragment = (RecipeStepDetailFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_STEP_DETAIL_FRAGMENT_TAG);
            fragment.setOnChangeRecipeStepListener(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_STEP_KEY, mSelectedRecipeStep);
        outState.putParcelable(RECIPE_KEY, mRecipe);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onChange(RecipeStep recipeStep) {
        if (recipeStep != null) {
            Intent startRecipeStepDetailActivityIntent = new Intent(getApplicationContext(), RecipeStepDetailActivity.class);
            startRecipeStepDetailActivityIntent.putExtra(RecipeStepDetailActivity.RECIPE_STEP_KEY, recipeStep);
            startRecipeStepDetailActivityIntent.putExtra(RecipeStepDetailActivity.RECIPE_KEY, mRecipe);
            Log.d(LOG_TAG, "Send Recipe Step Detail Intent");
            startActivity(startRecipeStepDetailActivityIntent);
            finish();
        }
    }
}
