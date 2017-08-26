package com.example.android.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by vincent on 8/26/17.
 */

public class NoScrollbarLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public NoScrollbarLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}
