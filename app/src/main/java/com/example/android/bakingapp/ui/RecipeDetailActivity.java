package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();
    private Recipe mRecipe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ActionBar actionBar = getSupportActionBar();

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(RecipesListActivity.SELECTED_RECIPE_KEY)) {
                mRecipe = intentThatStartedThisActivity.getParcelableExtra(RecipesListActivity.SELECTED_RECIPE_KEY);
                if (mRecipe != null) actionBar.setTitle(mRecipe.getName());
            }
        }

        // Do nothing.
    }

    public Recipe getRecipe() {
        return mRecipe;
    }
}
