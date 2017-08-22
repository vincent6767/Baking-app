package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.Recipe;

import java.util.List;

/**
 * Created by vincent on 8/22/17.
 */

public class RecipesAdapter extends BaseAdapter {
    private Context mContext;
    private List<Recipe> mRecipes;

    public RecipesAdapter(Context c, List<Recipe> recipes) {
        mContext = c;
        this.mRecipes = recipes;
    }

    @Override
    public int getCount() {
        if (mRecipes == null) return 0;
        else return mRecipes.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecipes.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = new View(mContext);
            gridView = inflater.inflate(R.layout.recipe_list_item, null);

            // Set text based on selected text
            TextView recipeNameView = (TextView) gridView.findViewById(R.id.tv_recipe_name);

            recipeNameView.setText(mRecipes.get(position).getName());
        } else {
            gridView = (View) convertView;
        }
        return gridView;
    }

    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public void addRecipe(Recipe recipe) {
        mRecipes.add(recipe);
        notifyDataSetChanged();
    }

    public void removeRecipe(int position) {
        if (mRecipes == null) {
            return;
        }
        if (position > mRecipes.size() || position < 0) {
            throw new IllegalArgumentException();
        }
        mRecipes.remove(position);
        notifyDataSetChanged();
    }
}
