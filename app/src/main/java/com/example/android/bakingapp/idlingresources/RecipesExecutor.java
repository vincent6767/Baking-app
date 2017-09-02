package com.example.android.bakingapp.idlingresources;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RecipesExecutor extends ThreadPoolExecutor implements IdlingResource {
    private boolean idleStateChanged = true;
    private boolean mIsIdle = true;
    private volatile ResourceCallback mResourceCallback;

    public RecipesExecutor() {
        super(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    public ResourceCallback getResourceCallback() {
        return mResourceCallback;
    }

    public void setIdleState(boolean idle) {
        idleStateChanged = (mIsIdle == idle);
        mIsIdle = idle;
    }

    @Override
    public String getName() {
        return RecipesExecutor.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        if (mIsIdle && mResourceCallback != null) {
            mResourceCallback.onTransitionToIdle();
        }
        return mIsIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mResourceCallback = callback;
    }
}
