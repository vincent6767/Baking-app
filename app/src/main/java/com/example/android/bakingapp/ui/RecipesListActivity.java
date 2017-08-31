package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.RecipesAdapter;
import com.example.android.bakingapp.entities.Recipe;
import com.example.android.bakingapp.services.RecipesService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesListActivity extends AppCompatActivity {
    public static final String SELECTED_RECIPE_KEY = "recipe";

    private static final String TAG = RecipesListActivity.class.getSimpleName();
    private static final String RECIPE_BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    private static final String GRID_VIEW_CURRENT_POSITION_KEY = "gridViewCurrentPositionKey";
    private static final String RECIPES_KEY = "recipesKey";
    private static final String LOG_TAG = RecipesListActivity.class.getSimpleName();
    private RecipesService mRecipeService;
    private RecipesAdapter mRecipesAdapter;
    private GridView mRecipesGridView;
    private TextView mErrorMessage;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        initializeViews();
        initializeRecipeService();

        if (savedInstanceState != null) {
            int currentPosition = savedInstanceState.getInt(GRID_VIEW_CURRENT_POSITION_KEY);
            List<Recipe> recipes = savedInstanceState.getParcelableArrayList(RECIPES_KEY);
            mRecipesAdapter.setRecipes(recipes);
            mRecipesGridView.smoothScrollToPosition(currentPosition);
            Log.d(TAG, "Current position: " + currentPosition);
        } else {
            fetchRecipes();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_recipes_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_recipe_widget_settings:
                // Start Widget Settings Activity
                Intent intent = new Intent(this, RecipeWidgetSettingsActivity.class);
                intent.putParcelableArrayListExtra(RecipeWidgetSettingsActivity.RECIPE_WIDGET_SETTINGS, (ArrayList<? extends Parcelable>) mRecipesAdapter.getRecipes());
                startActivity(intent);
                break;
            default:
                Log.d(LOG_TAG, "No menu item found");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(GRID_VIEW_CURRENT_POSITION_KEY, mRecipesGridView.getFirstVisiblePosition());
        outState.putParcelableArrayList(RECIPES_KEY, (ArrayList<Recipe>) mRecipesAdapter.getRecipes());
        super.onSaveInstanceState(outState);
    }

    private void initializeViews() {
        mRecipesGridView = (GridView) findViewById(R.id.gv_recipes);
        mRecipesAdapter = new RecipesAdapter(this, new ArrayList<Recipe>());
        mRecipesGridView.setAdapter(mRecipesAdapter);
        mRecipesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent recipeDetailIntent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
                recipeDetailIntent.putExtra(SELECTED_RECIPE_KEY, mRecipesAdapter.getItem(position));
                startActivity(recipeDetailIntent);
            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessage = (TextView) findViewById(R.id.tv_recipes_list_error_message);
    }

    private void fetchRecipes() {
        Callback<List<Recipe>> callback = new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mProgressBar.setVisibility(View.INVISIBLE);
                showRecipes();
                List<Recipe> recipes = response.body();
                mRecipesAdapter.setRecipes(recipes);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                showErrorMessage(t.getMessage());
            }
        };
        Call<List<Recipe>> call = mRecipeService.getRecipes();
        mProgressBar.setVisibility(View.VISIBLE);
        // Fetch recipes on the background mode.
        call.enqueue(callback);
    }

    private void showErrorMessage(String errorMessage) {
        mRecipesGridView.setVisibility(View.INVISIBLE);
        mErrorMessage.setText(errorMessage);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showRecipes() {
        mErrorMessage.setText("");
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecipesGridView.setVisibility(View.VISIBLE);
    }

    private void onConnectivityException() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showErrorMessage(getString(R.string.no_connectivity_exception_message));
            }
        });
    }


    private void initializeRecipeService() {
        mRecipeService = new Retrofit.Builder().baseUrl(RECIPE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RecipesService.class);
    }
}
