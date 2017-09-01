package com.example.android.bakingapp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.android.bakingapp.R;

public class RecipeImageViewHolder extends RecyclerView.ViewHolder {
    private ImageView mRecipeImage;

    public RecipeImageViewHolder(View itemView) {
        super(itemView);
        mRecipeImage = itemView.findViewById(R.id.iv_recipe_image);
    }

    public ImageView getmRecipeImage() {
        return mRecipeImage;
    }

    public void setmRecipeImage(ImageView mRecipeImage) {
        this.mRecipeImage = mRecipeImage;
    }
}
