package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.Recipe;

import java.util.List;

public class RecipesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String LOG_TAG = RecipesRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;
    private List<Recipe> mRecipeList;
    private OnRecipeClickListener mCallback;

    public RecipesRecyclerViewAdapter(Context context, List<Recipe> recipes, OnRecipeClickListener callback) {
        mContext = context;
        mRecipeList = recipes;
        mCallback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View recipesView = inflater.inflate(R.layout.recipe_list_item, parent, false);
        return new RecipesViewHolder(recipesView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecipesViewHolder recipesViewHolder = (RecipesViewHolder) holder;
        Recipe recipe = mRecipeList.get(position);
        recipesViewHolder.getmRecipeNameTextView().setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        if (mRecipeList == null) return 0;
        else return mRecipeList.size();
    }

    public List<Recipe> getRecipes() {
        return mRecipeList;
    }

    public void setRecipes(List<Recipe> recipes) {
        mRecipeList = recipes;
        if (mRecipeList != null) {
            notifyDataSetChanged();
        }
    }

    public interface OnRecipeClickListener {
        void onClick(Recipe recipe);
    }

    class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mRecipeNameTextView;

        public RecipesViewHolder(View itemView) {
            super(itemView);

            mRecipeNameTextView = itemView.findViewById(R.id.tv_recipe_name);
            mRecipeNameTextView.setOnClickListener(this);
        }

        public TextView getmRecipeNameTextView() {
            return mRecipeNameTextView;
        }

        @Override
        public void onClick(View view) {
            mCallback.onClick(mRecipeList.get(getAdapterPosition()));
        }
    }
}
