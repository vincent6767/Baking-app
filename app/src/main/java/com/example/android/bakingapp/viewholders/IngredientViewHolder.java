package com.example.android.bakingapp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.R;

public class IngredientViewHolder extends RecyclerView.ViewHolder {
    private TextView mNameTextView;
    private TextView mQtyStringTextView;

    public IngredientViewHolder(View itemView) {
        super(itemView);
        mNameTextView = itemView.findViewById(R.id.tv_ingredient_name);
        mQtyStringTextView = itemView.findViewById(R.id.tv_ingredient_qty_string);
    }

    public TextView getmNameTextView() {
        return mNameTextView;
    }

    public void setmNameTextView(TextView mNameTextView) {
        this.mNameTextView = mNameTextView;
    }

    public TextView getmQtyStringTextView() {
        return mQtyStringTextView;
    }

    public void setmQtyStringTextView(TextView mQtyStringTextView) {
        this.mQtyStringTextView = mQtyStringTextView;
    }
}