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
import com.example.android.bakingapp.adapters.RecipeStepsAdapter;
import com.example.android.bakingapp.entities.Recipe;

public class RecipeMasterListFragment extends Fragment {
    private static final String LOG_TAG = RecipeMasterListFragment.class.getSimpleName();
    private static final String SELECTED_RECIPE_KEY = "selectedRecipe";

    private Recipe mRecipe;
    private IngredientsAdapter mIngredientsAdapter;
    private RecyclerView mIngredientRecylcerView;
    private RecipeStepsAdapter mRecipeStepsAdapter;
    private RecyclerView mRecipeStepsRecyclerView;

    public RecipeMasterListFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mRecipe == null) {
            try {
                OnInitializationListener listener = (OnInitializationListener) getActivity();
                mRecipe = listener.getInitialRecipe();

                mIngredientsAdapter = new IngredientsAdapter(getActivity().getApplicationContext(), mRecipe.getIngredients());
                mIngredientRecylcerView.setAdapter(mIngredientsAdapter);
                try {
                    mRecipeStepsAdapter = new RecipeStepsAdapter(getActivity().getApplicationContext(), mRecipe.getSteps(), (RecipeStepsAdapter.OnRecipeStepClickListener) getActivity());
                    mRecipeStepsRecyclerView.setAdapter(mRecipeStepsAdapter);
                } catch (ClassCastException e) {
                    Log.d(LOG_TAG, "Attached activity didn't implement OnRecipeStepClickListener");
                }
            } catch (ClassCastException e) {
                Log.d(LOG_TAG, "Attached Activity didn't implement OnInitializationListener");
            }
        }
        Log.d(LOG_TAG, "onActivityCreated");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_master_list, container, false);

        mIngredientRecylcerView = rootView.findViewById(R.id.rv_ingredients_list);
        mIngredientRecylcerView.setHasFixedSize(true);
        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mIngredientRecylcerView.setLayoutManager(ingredientLayoutManager);

        mRecipeStepsRecyclerView = rootView.findViewById(R.id.rv_recipe_steps_list);
        mRecipeStepsRecyclerView.setHasFixedSize(true);
        NoScrollbarLinearLayoutManager recipeStepLayoutManager = new NoScrollbarLinearLayoutManager(getActivity().getApplicationContext());

        mRecipeStepsRecyclerView.setLayoutManager(recipeStepLayoutManager);

        Log.d(LOG_TAG, "onCreateView");
        return rootView;
    }

    public interface OnInitializationListener {
        Recipe getInitialRecipe();
    }
}
