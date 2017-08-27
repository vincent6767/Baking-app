package com.example.android.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.RecipeStep;

public class RecipeStepDetailFragment extends Fragment {
    private static final String LOG_TAG = RecipeStepDetailFragment.class.getSimpleName();
    private static final String RECIPE_STEP_KEY = "recipeStep";
    private RecipeStep mRecipeStep;

    public RecipeStepDetailFragment() {
    }

    public static RecipeStepDetailFragment newInstance(RecipeStep recipeStep) {
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_STEP_KEY, recipeStep);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        if (this.getArguments() != null) {
            Log.d(LOG_TAG, "There is a saved instance state");
            mRecipeStep = this.getArguments().getParcelable(RECIPE_STEP_KEY);
        }
        if (mRecipeStep != null) {
            Log.d(LOG_TAG, "There is a saved instance state");

            TextView recipeStepDescriptionTextView = rootView.findViewById(R.id.tv_recipe_step_description);
            // Initialize ExoPlayer

            recipeStepDescriptionTextView.setText(mRecipeStep.getDescription());
        } else {
            Log.d(LOG_TAG, "This fragment has a null RecipeStep");
        }
        return rootView;
    }
}
