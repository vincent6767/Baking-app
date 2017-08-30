package com.example.android.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.Recipe;
import com.example.android.bakingapp.widgets.IngredientsWidget;
import com.google.gson.Gson;

import java.util.List;

public class RecipeWidgetSettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, CompoundButton.OnCheckedChangeListener {
    public static final String DESIRED_RECIPE_NAME = "desiredRecipeName";
    public static final String DESIRED_RECIPE_INGREDIENTS = "desiredRecipeIngredients";
    public static final String RECIPE_WIDGET_SETTINGS = "recipeWidgetSettings";
    public static final String RECIPE_PREF = "com.example.android.bakingapp.ingredients";
    private static final String LOG_TAG = RecipeWidgetSettingsActivity.class.getSimpleName();
    private List<Recipe> mRecipes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_widget_settings);

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(RECIPE_WIDGET_SETTINGS)) {
                mRecipes = intent.getParcelableArrayListExtra(RECIPE_WIDGET_SETTINGS);

                if (mRecipes != null) {
                    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_recipes_list_for_setting);
                    initializeRecipeRadioButtons(radioGroup);
                }
            }
        }
        getSharedPreferences(RECIPE_PREF, MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
    }

    private void initializeRecipeRadioButtons(RadioGroup radioGroup) {
        if (mRecipes != null) {
            radioGroup.setOrientation(LinearLayout.VERTICAL);

            for (int i = 0; i < mRecipes.size(); i++) {
                Recipe recipe = mRecipes.get(i);
                RadioButton recipeRadioButton = new RadioButton(this);
                recipeRadioButton.setId(i);
                recipeRadioButton.setText(recipe.getName());
                recipeRadioButton.setOnCheckedChangeListener(this);
                radioGroup.addView(recipeRadioButton);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSharedPreferences(RECIPE_PREF, MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Trigger update on IngredientsWidget
        Log.d(LOG_TAG, "onSharedPreferenceChanged when all shared preferences changed");
        Intent intent = new Intent(this, IngredientsWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), IngredientsWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = getSharedPreferences(RECIPE_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Log.d(LOG_TAG, "ID: " + String.valueOf(compoundButton.getId()));
            editor.putString(DESIRED_RECIPE_NAME, mRecipes.get(compoundButton.getId()).getName());
            editor.putString(DESIRED_RECIPE_INGREDIENTS, gson.toJson(mRecipes.get(compoundButton.getId()).getIngredients()));
            editor.apply();
        }
    }
}
