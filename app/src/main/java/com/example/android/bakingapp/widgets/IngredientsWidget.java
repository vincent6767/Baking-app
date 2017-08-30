package com.example.android.bakingapp.widgets;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.Ingredient;
import com.example.android.bakingapp.ui.RecipeWidgetSettingsActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {
    private static final String LOG_TAG = IngredientsWidget.class.getSimpleName();
    private Context mContext;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        Log.d(LOG_TAG, "Updating the widget");
        SharedPreferences pref = context.getSharedPreferences(RecipeWidgetSettingsActivity.RECIPE_PREF, 0);

        String recipeName = pref.getString(RecipeWidgetSettingsActivity.DESIRED_RECIPE_NAME, "");
        Gson gson = new Gson();
        String ingredientsJsonString = pref.getString(RecipeWidgetSettingsActivity.DESIRED_RECIPE_INGREDIENTS, "");
        Type type = new TypeToken<ArrayList<Ingredient>>() {
        }.getType();
        ArrayList<Ingredient> ingredientArrayList = gson.fromJson(ingredientsJsonString, type);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setTextViewText(R.id.tv_recipe_name_app_widget, recipeName);

        views.setTextViewText(R.id.tv_ingredients_list, "");
        StringBuilder stringBuilder = new StringBuilder();
        if (ingredientArrayList.size() > 0) {
            for (Ingredient ingredient : ingredientArrayList) {
                String formattedIngredient = String.format(Locale.US, "%s\t\t\t%.1f %s\n", ingredient.getName(), ingredient.getQuantity(), ingredient.getMeasure());
                stringBuilder.append(formattedIngredient);
            }
        }
        views.setTextViewText(R.id.tv_ingredients_list, stringBuilder.toString());
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        mContext = context;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

