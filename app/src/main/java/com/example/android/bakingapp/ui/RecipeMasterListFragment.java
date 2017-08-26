package com.example.android.bakingapp.ui;

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
import com.example.android.bakingapp.adapters.IngredientsAdapter;
import com.example.android.bakingapp.entities.Recipe;

public class RecipeMasterListFragment extends Fragment {
    private static final String LOG_TAG = RecipeMasterListFragment.class.getSimpleName();
    private static final String SELECTED_RECIPE_KEY = "selectedRecipe";
    private Recipe mRecipe;
    private IngredientsAdapter mIngredientsAdapter;
    private RecyclerView ingredientRecylcerView;

    public RecipeMasterListFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecipe = ((RecipeDetailActivity) getActivity()).getRecipe();

        mIngredientsAdapter = new IngredientsAdapter(getActivity().getApplicationContext(), mRecipe.getIngredients());
        ingredientRecylcerView.setAdapter(mIngredientsAdapter);
        Log.d(LOG_TAG, "onActivityCreated");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_master_list, container, false);

        ingredientRecylcerView = rootView.findViewById(R.id.rv_ingredients_list);
        ingredientRecylcerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        ingredientRecylcerView.setLayoutManager(layoutManager);

        Log.d(LOG_TAG, "onCreateView");
        return rootView;
    }
}
