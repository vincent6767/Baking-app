package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.RecipeStep;

public class RecipeStepDetailActivity extends AppCompatActivity {
    public static final String RECIPE_STEP_KEY = "recipeStep";
    public static final String RECIPE_NAME_KEY = "recipeName";
    private static final String LOG_TAG = RecipeStepDetailActivity.class.getSimpleName();

    private String mRecipeName;
    private RecipeStep mRecipeStep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        ActionBar actionBar = getSupportActionBar();

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.hasExtra(RECIPE_NAME_KEY)) {
                    mRecipeName = intent.getStringExtra(RECIPE_NAME_KEY);
                }
                if (intent.hasExtra(RECIPE_STEP_KEY)) {
                    mRecipeStep = intent.getParcelableExtra(RECIPE_STEP_KEY);
                    RecipeStepDetailFragment fragment = RecipeStepDetailFragment.newInstance(mRecipeStep);

                    FragmentManager manager = getSupportFragmentManager();

                    manager.beginTransaction()
                            .add(R.id.recipe_step_detail_container, fragment)
                            .commit();
                }
            }
        }
        if (actionBar != null) {
            Log.d(LOG_TAG, "Recipe name: " + mRecipeName);
            actionBar.setTitle(mRecipeName);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
