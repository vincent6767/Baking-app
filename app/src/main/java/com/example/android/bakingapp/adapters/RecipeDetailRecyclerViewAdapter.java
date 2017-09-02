package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.Ingredient;
import com.example.android.bakingapp.entities.RecipeStep;
import com.example.android.bakingapp.viewholders.IngredientViewHolder;
import com.example.android.bakingapp.viewholders.RecipeDetailTitleSectionViewHolder;
import com.example.android.bakingapp.viewholders.RecipeImageViewHolder;
import com.example.android.bakingapp.viewholders.RecipeStepViewHolder;

import java.util.List;

public class RecipeDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecipeStepViewHolder.OnRecipeStepClickListener {
    private static final String LOG_TAG = RecipeDetailRecyclerViewAdapter.class.getSimpleName();
    private static final int RECIPE_IMAGE = 0, TITLE = 1, INGREDIENT = 2, RECIPE_STEP = 3;
    private Context mContext;
    private List<Object> mItems;
    private OnRecipeStepSelectedListener mRecipeStepListener;

    public RecipeDetailRecyclerViewAdapter(Context context, List<Object> items, OnRecipeStepSelectedListener onRecipeStepSelectedListener) {
        mContext = context;
        mItems = items;
        mRecipeStepListener = onRecipeStepSelectedListener;
    }

    @Override
    public void onClick(int position) {
        mRecipeStepListener.onSelected((RecipeStep) mItems.get(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mItems.get(position);
        if (item instanceof Uri) {
            return RECIPE_IMAGE;
        } else if (item instanceof String) {
            return TITLE;
        } else if (item instanceof Ingredient) {
            return INGREDIENT;
        } else if (item instanceof RecipeStep) {
            return RECIPE_STEP;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case RECIPE_IMAGE:
                View recipeImageView = inflater.inflate(R.layout.viewholder_recipe_detail_recipe_image, parent, false);
                viewHolder = new RecipeImageViewHolder(recipeImageView);
                break;
            case TITLE:
                View titleSectionView = inflater.inflate(R.layout.viewholder_recipe_detail_title_section, parent, false);
                viewHolder = new RecipeDetailTitleSectionViewHolder(titleSectionView);
                break;
            case INGREDIENT:
                View ingredientsView = inflater.inflate(R.layout.viewholder_list_ingredient_item, parent, false);
                viewHolder = new IngredientViewHolder(ingredientsView);
                break;
            case RECIPE_STEP:
                View recipeStepView = inflater.inflate(R.layout.view_holder_list_recipe_step_item, parent, false);
                viewHolder = new RecipeStepViewHolder(recipeStepView, this);
                break;
            default:
                Log.d(LOG_TAG, "Oops. Nothing in here");
                viewHolder = null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case RECIPE_IMAGE:
                configureRecipeImageViewHolder((RecipeImageViewHolder) holder, position);
                break;
            case TITLE:
                configureRecipeDetailTitleSectionViewHolder((RecipeDetailTitleSectionViewHolder) holder, position);
                break;
            case INGREDIENT:
                configureIngredientViewHolder((IngredientViewHolder) holder, position);
                break;
            case RECIPE_STEP:
                configureRecipeStepViewHolder((RecipeStepViewHolder) holder, position);
                break;
            default:
                throw new RuntimeException("No matched view holder");
        }
    }

    private void configureRecipeStepViewHolder(RecipeStepViewHolder viewHolder, int position) {
        RecipeStep recipeStep = (RecipeStep) mItems.get(position);

        viewHolder.getmShortDescriptionTextView().setText(recipeStep.getShortDescriptionWithOrderNumber());
    }

    private void configureIngredientViewHolder(IngredientViewHolder viewHolder, int position) {
        Ingredient ingredient = (Ingredient) mItems.get(position);

        viewHolder.getmNameTextView().setText(ingredient.getName());
        viewHolder.getmQtyStringTextView().setText(String.valueOf(ingredient.getQtyString()));
    }

    @Override
    public int getItemCount() {
        if (mItems == null) return 0;
        else return mItems.size();
    }

    private void configureRecipeDetailTitleSectionViewHolder(RecipeDetailTitleSectionViewHolder viewHolder, int position) {
        String title = (String) mItems.get(position);
        viewHolder.getmTitle().setText(title);
    }

    private void configureRecipeImageViewHolder(RecipeImageViewHolder viewHolder, int position) {
        String recipeImageUrl = mItems.get(position).toString();
        Glide.with(mContext)
                .load(recipeImageUrl)
                .placeholder(R.drawable.image_placeholder)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.getmRecipeImage());
    }

    public interface OnRecipeStepSelectedListener {
        void onSelected(RecipeStep recipeStep, int position);
    }
}
