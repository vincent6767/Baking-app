package com.example.android.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.Recipe;

/**
 * Created by vincent on 8/26/17.
 */

public class RecipeMasterListFragment extends Fragment {
    private static final String SELECTED_RECIPE_KEY = "selectedRecipe";
    private Recipe mRecipe;

    public RecipeMasterListFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecipe = ((RecipeDetailActivity) getActivity()).getRecipe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_master_list, container, false);
        return rootView;
    }
}
