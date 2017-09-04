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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.entities.RecipeStep;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class RecipeStepDetailFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener {
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
    private ImageView mVideoThumbnailImageView;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private long mResumePosition;
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
        Log.d(LOG_TAG, "OnSaveInstanceState -> Resume position: " + mResumePosition);
        outState.putLong(PLAYER_POSITION_KEY, mResumePosition);
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
            mVideoThumbnailImageView = rootView.findViewById(R.id.iv_video_thumbnail);
            if (mSelectedRecipeStep.hasVideo()) {
                showPlayerView(mSimpleExoPlayerView);
                errorMessage.setVisibility(View.INVISIBLE);
                if (savedInstanceState != null) {
                    mResumePosition = savedInstanceState.getLong(PLAYER_POSITION_KEY);
                    Log.d(LOG_TAG, "onCreateView -> SavedInstanceState: " + mResumePosition);
                }
                initializePlayer(mSelectedRecipeStep.getVideoUri(), mSimpleExoPlayerView, mResumePosition);
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
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 && mSelectedRecipeStep.hasVideo()) {
            initializePlayer(mSelectedRecipeStep.getVideoUri(), mSimpleExoPlayerView, mResumePosition);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 && mSelectedRecipeStep.hasVideo())) {
            initializePlayer(mSelectedRecipeStep.getVideoUri(), mSimpleExoPlayerView, mResumePosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseExoPlayer();
    }

    private void releaseExoPlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mResumePosition = mExoPlayer.getContentPosition();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
    private void showPlayerView(SimpleExoPlayerView playerView) {
        playerView.setVisibility(View.VISIBLE);
        playerView.showController();
        Glide.with(getActivity().getApplicationContext())
                .load(mSelectedRecipeStep.getThumbnailURL())
                .placeholder(R.drawable.video_placeholder)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mVideoThumbnailImageView);
        mVideoThumbnailImageView.setVisibility(View.VISIBLE);
    }

    private void hidePlayerview(SimpleExoPlayerView playerView) {
        playerView.setVisibility(View.INVISIBLE);
        playerView.hideController();
        mVideoThumbnailImageView.setVisibility(View.INVISIBLE);
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
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.addListener(this);
        }
        if (position > -1) {
            mExoPlayer.seekTo(position);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_READY) {
            mVideoThumbnailImageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    public interface OnChangeRecipeStepListener {
        void onChange(RecipeStep recipeStep);
    }
}
