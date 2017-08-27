package com.example.android.bakingapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class RecipeStepDetailFragment extends Fragment {
    private static final String LOG_TAG = RecipeStepDetailFragment.class.getSimpleName();
    private static final String RECIPE_STEP_KEY = "recipeStep";
    private RecipeStep mRecipeStep;
    private SimpleExoPlayer mExoPlayer;

    public RecipeStepDetailFragment() {
    }

    public static RecipeStepDetailFragment newInstance(RecipeStep recipeStep) {
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_STEP_KEY, recipeStep);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        if (this.getArguments() != null) {
            Log.d(LOG_TAG, "There is a saved instance state");
            mRecipeStep = this.getArguments().getParcelable(RECIPE_STEP_KEY);
        }
        if (mRecipeStep != null) {
            Log.d(LOG_TAG, "There is a saved instance state");
            SimpleExoPlayerView playerView = rootView.findViewById(R.id.player_view);
            TextView errorMessage = rootView.findViewById(R.id.tv_video_error_message);
            if (mRecipeStep.hasVideo()) {
                showPlayerView(playerView);
                errorMessage.setVisibility(View.INVISIBLE);
                initializePlayer(mRecipeStep.getVideoUri(), playerView);
            } else {
                hidePlayerview(playerView);
                errorMessage.setVisibility(View.VISIBLE);
            }
            TextView recipeStepDescriptionTextView = rootView.findViewById(R.id.tv_recipe_step_description);
            recipeStepDescriptionTextView.setText(mRecipeStep.getDescription());
        } else {
            Log.d(LOG_TAG, "This fragment has a null RecipeStep");
        }
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
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

    private void initializePlayer(Uri mediaUri, SimpleExoPlayerView playerView) {
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
            mExoPlayer.setPlayWhenReady(true);
        }
    }
}
