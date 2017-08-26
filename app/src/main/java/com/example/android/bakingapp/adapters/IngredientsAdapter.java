package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.Ingredient;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Ingredient> mIngredients;

    public IngredientsAdapter(Context context, List<Ingredient> ingredients) {
        mContext = context;
        mIngredients = ingredients;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;

        ingredientViewHolder.mNameTextView.setText(mIngredients.get(position).getName());
        ingredientViewHolder.mQtyStringTextView.setText(String.valueOf(mIngredients.get(position).getQtyString()));
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) return 0;
        else return mIngredients.size();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
        if (mIngredients != null) {
            notifyDataSetChanged();
        }
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView mNameTextView;
        TextView mQtyStringTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.tv_ingredient_name);
            mQtyStringTextView = itemView.findViewById(R.id.tv_ingredient_qty_string);
        }
    }
}
