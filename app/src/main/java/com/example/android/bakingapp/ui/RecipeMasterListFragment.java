package com.example.android.bakingapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.RecipeDetailRecyclerViewAdapter;
import com.example.android.bakingapp.entities.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeMasterListFragment extends Fragment {
    private static final String LOG_TAG = RecipeMasterListFragment.class.getSimpleName();
    private static final String SELECTED_RECIPE_KEY = "selectedRecipe";

    private Recipe mRecipe;
    private RecyclerView mItemsRecyclerView;

    public RecipeMasterListFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mRecipe == null) {
            try {
                OnInitializationListener listener = (OnInitializationListener) getActivity();
                mRecipe = listener.getInitialRecipe();

                try {
                    List<Object> items = initializeRecipeDetailAdapter(mRecipe);

                    RecipeDetailRecyclerViewAdapter.OnRecipeStepSelectedListener onRecipeStepSelectedListener = (RecipeDetailRecyclerViewAdapter.OnRecipeStepSelectedListener) getActivity();
                    mItemsRecyclerView.setAdapter(new RecipeDetailRecyclerViewAdapter(getActivity().getApplicationContext(), items, onRecipeStepSelectedListener));
                } catch (ClassCastException e) {
                    Log.d(LOG_TAG, "Attached activity didn't implement OnRecipeStepSelectedListener");
                }
            } catch (ClassCastException e) {
                Log.d(LOG_TAG, "Attached Activity didn't implement OnInitializationListener");
            }
        }

        Log.d(LOG_TAG, "onActivityCreated");
    }

    private List<Object> initializeRecipeDetailAdapter(Recipe recipe) {
        List<Object> items = new ArrayList<>();
        items.add(Uri.parse(recipe.getImage()));
        items.add(getString(R.string.ingredients_title));
        items.addAll(recipe.getIngredients());
        items.add(getString(R.string.recipe_steps_title));
        items.addAll(recipe.getSteps());
        return items;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_master_list, container, false);
        mItemsRecyclerView = rootView.findViewById(R.id.rv_items_list);
        mItemsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mItemsRecyclerView.setLayoutManager(lm);

        Log.d(LOG_TAG, "onCreateView");
        return rootView;
    }

    public interface OnInitializationListener {
        Recipe getInitialRecipe();
    }
}
