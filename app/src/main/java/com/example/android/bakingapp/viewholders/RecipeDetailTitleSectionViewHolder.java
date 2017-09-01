package com.example.android.bakingapp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.R;

public class RecipeDetailTitleSectionViewHolder extends RecyclerView.ViewHolder {
    private TextView mTitle;

    public RecipeDetailTitleSectionViewHolder(View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.tv_recipe_detail_title_section);
    }

    public TextView getmTitle() {
        return mTitle;
    }

    public void setmTitle(TextView mTitle) {
        this.mTitle = mTitle;
    }
}
