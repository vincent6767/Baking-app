package com.example.android.bakingapp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.R;

public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mShortDescriptionTextView;
    private OnRecipeStepClickListener mListener;

    public RecipeStepViewHolder(View itemView, OnRecipeStepClickListener onRecipeStepClickListener) {
        super(itemView);
        mShortDescriptionTextView = itemView.findViewById(R.id.tv_recipe_step_short_description);
        mShortDescriptionTextView.setOnClickListener(this);
        mListener = onRecipeStepClickListener;
    }

    public TextView getmShortDescriptionTextView() {
        return mShortDescriptionTextView;
    }

    public void setmShortDescriptionTextView(TextView mShortDescriptionTextView) {
        this.mShortDescriptionTextView = mShortDescriptionTextView;
    }

    @Override
    public void onClick(View view) {
        mListener.onClick(getAdapterPosition());
    }

    public interface OnRecipeStepClickListener {
        void onClick(int position);
    }
}
