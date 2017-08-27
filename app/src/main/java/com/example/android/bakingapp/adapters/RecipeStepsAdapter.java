package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.RecipeStep;

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<RecipeStep> mRecipeSteps;
    private OnRecipeStepClickListener mCallback;

    public RecipeStepsAdapter(Context context, List<RecipeStep> recipeSteps, OnRecipeStepClickListener listener) {
        mCallback = listener;
        mContext = context;
        mRecipeSteps = recipeSteps;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipe_step_item, parent, false);
        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecipeStepViewHolder recipeStepViewHolder = (RecipeStepViewHolder) holder;
        recipeStepViewHolder.mShortDescriptionTextView.setText(mRecipeSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mRecipeSteps == null) return 0;
        else return mRecipeSteps.size();
    }

    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        mRecipeSteps = recipeSteps;
        if (mRecipeSteps != null) notifyDataSetChanged();
    }

    public interface OnRecipeStepClickListener {
        void onRecipeStepSelected(RecipeStep recipeStep);
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mShortDescriptionTextView;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);

            mShortDescriptionTextView = itemView.findViewById(R.id.tv_recipe_step_short_description);
            mShortDescriptionTextView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            mCallback.onRecipeStepSelected(mRecipeSteps.get(getAdapterPosition()));
        }
    }
}
