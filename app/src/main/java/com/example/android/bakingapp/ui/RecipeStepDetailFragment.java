package com.example.android.bakingapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.RecipeStep;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class RecipeStepDetailFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = RecipeStepDetailFragment.class.getSimpleName();
    private static final String RECIPE_STEP_KEY = "recipeStep";
    private static final String PREVIOUS_RECIPE_STEP_KEY = "previousRecipeStep";
    private static final String NEXT_RECIPE_STEP_KEY = "nextRecipeStep";
    private static final String PLAYER_POSITION_KEY = "playerPosition";

    private RecipeStep mSelectedRecipeStep;
    private SimpleExoPlayer mExoPlayer;
    private RecipeStep mPreviousRecipeStep;
    private RecipeStep mNextRecipeStep;
    private Button mPreviousButton;
    private Button mNextButton;
    private TextView mStepDescriptionTextView;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private OnChangeRecipeStepListener mOnChangeRecipeStepListener;

    private boolean mFullscreenMode;

    public RecipeStepDetailFragment() {
    }

    public static RecipeStepDetailFragment newInstance(RecipeStep recipeStep, RecipeStep previousStep, RecipeStep nexStep, OnChangeRecipeStepListener onChangeRecipeStepListener) {
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.mOnChangeRecipeStepListener = onChangeRecipeStepListener;
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_STEP_KEY, recipeStep);
        args.putParcelable(PREVIOUS_RECIPE_STEP_KEY, previousStep);
        args.putParcelable(NEXT_RECIPE_STEP_KEY, nexStep);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_step:
                mOnChangeRecipeStepListener.onChange(mNextRecipeStep);
                break;
            case R.id.btn_previous_step:
                mOnChangeRecipeStepListener.onChange(mPreviousRecipeStep);
                break;
            default:
                Log.w(LOG_TAG, "View that attached to the method not previous or next button. Do nothing about it");
        }
    }

    public void setOnChangeRecipeStepListener(OnChangeRecipeStepListener onChangeRecipeStepListener) {
        mOnChangeRecipeStepListener = onChangeRecipeStepListener;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mExoPlayer != null) {
            long playerPosition = mExoPlayer.getCurrentPosition();
            outState.putLong(PLAYER_POSITION_KEY, playerPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        if (this.getArguments() != null) {
            mSelectedRecipeStep = this.getArguments().getParcelable(RECIPE_STEP_KEY);
            mPreviousRecipeStep = this.getArguments().getParcelable(PREVIOUS_RECIPE_STEP_KEY);
            mNextRecipeStep = this.getArguments().getParcelable(NEXT_RECIPE_STEP_KEY);
        }
        if (mSelectedRecipeStep != null) {
            mSimpleExoPlayerView = rootView.findViewById(R.id.player_view);
            TextView errorMessage = rootView.findViewById(R.id.tv_video_error_message);
            if (mSelectedRecipeStep.hasVideo()) {
                showPlayerView(mSimpleExoPlayerView);
                errorMessage.setVisibility(View.INVISIBLE);
                long lastPosition = 0;
                if (savedInstanceState != null) {
                    lastPosition = savedInstanceState.getLong(PLAYER_POSITION_KEY);
                }
                initializePlayer(mSelectedRecipeStep.getVideoUri(), mSimpleExoPlayerView, lastPosition);
            } else {
                hidePlayerview(mSimpleExoPlayerView);
                errorMessage.setVisibility(View.VISIBLE);
            }

            mFullscreenMode = (rootView.findViewById(R.id.fl_video_player_container) == null);

            if (!mFullscreenMode) {
                mStepDescriptionTextView = rootView.findViewById(R.id.tv_recipe_step_description);
                mStepDescriptionTextView.setText(mSelectedRecipeStep.getDescription());

                mNextButton = rootView.findViewById(R.id.btn_next_step);
                mPreviousButton = rootView.findViewById(R.id.btn_previous_step);
                // Disable or enable button based on the existing next and previous step only if it is available.
                if (mNextButton != null) {
                    mNextButton.setEnabled((mNextRecipeStep != null));
                    mNextButton.setOnClickListener(this);
                }
                if (mPreviousButton != null) {
                    mPreviousButton.setEnabled((mPreviousRecipeStep != null));
                    mPreviousButton.setOnClickListener(this);
                }
            }
        } else {
            // TODO: Handle this scenario
            Log.e(LOG_TAG, "This fragment has a null RecipeStep");
        }
        return rootView;
    }
    @Override
    public void onStop() {
        super.onStop();
        releaseExoPlayer();
    }

    private void releaseExoPlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
    private void showPlayerView(SimpleExoPlayerView playerView) {
        playerView.setVisibility(View.VISIBLE);
        playerView.showController();
    }

    private void hidePlayerview(SimpleExoPlayerView playerView) {
        playerView.setVisibility(View.INVISIBLE);
        playerView.hideController();
    }

    private void initializePlayer(Uri mediaUri, SimpleExoPlayerView playerView, long position) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            playerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource
            String userAgent = Util.getUserAgent(getActivity(), "Recipe step");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getActivity(), userAgent),
                    new DefaultExtractorsFactory(),
                    null, null);
            mExoPlayer.prepare(mediaSource);
            if (position > -1) {
                mExoPlayer.seekTo(position);
            }
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    public interface OnChangeRecipeStepListener {
        void onChange(RecipeStep recipeStep);
    }
}
