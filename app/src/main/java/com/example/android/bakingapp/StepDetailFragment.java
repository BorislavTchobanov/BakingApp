package com.example.android.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utilities.ImageHandler;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.net.URLConnection;
import java.util.HashMap;

import static com.example.android.bakingapp.RecipeDetailActivity.EXTRA_STEP;
import static com.example.android.bakingapp.RecipeDetailActivity.EXTRA_TWO_PANE;
import static com.example.android.bakingapp.RecipeStepDetailActivity.EXTRA_AT_FIRST_STEP;
import static com.example.android.bakingapp.RecipeStepDetailActivity.EXTRA_AT_LAST_STEP;

public class StepDetailFragment extends Fragment {

    public static final String BUTTON_PREVIOUS = "button_previous";
    public static final String BUTTON_NEXT = "button_next";
    private static final String RESUMED_POSITION = "resumed_position";
    private static final String PLAYER_STATE = "player_state";

    private Step step;
    private SimpleExoPlayer mExoPlayer;
    private long resumePosition;
    private boolean shouldAutoPlay = false;
    private PlayerView simpleExoPlayerView;
    private ImageView thumbnailView;
    private OnStepNavButtonClickListener mListener;
    private boolean mTwoPane;

    public interface OnStepNavButtonClickListener {
        void onStepNavButtonClick(String tag);
    }

    public StepDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail_view, container, false);

        assert getArguments() != null;
        if (getArguments().containsKey(EXTRA_STEP)) {
            step = (Step) getArguments().getSerializable(EXTRA_STEP);
        }

        resumePosition = C.TIME_UNSET;
        shouldAutoPlay = true;
        if (savedInstanceState != null) {
            resumePosition = savedInstanceState.getLong(RESUMED_POSITION, C.TIME_UNSET);
            shouldAutoPlay = savedInstanceState.getBoolean(PLAYER_STATE, false);
        }

        boolean isAtFirstStep = getArguments().getBoolean(EXTRA_AT_FIRST_STEP, false);
        boolean isAtLastStep = getArguments().getBoolean(EXTRA_AT_LAST_STEP, false);
        int orientation = getResources().getConfiguration().orientation;

        if (step != null) {

            thumbnailView = rootView.findViewById(R.id.media_slot);
            simpleExoPlayerView = rootView.findViewById(R.id.player_view);

            if (orientation != Configuration.ORIENTATION_LANDSCAPE || mTwoPane) {
                setupInstructionSection(rootView);
                if (!mTwoPane) {
                    setupButtonSection(rootView, isAtFirstStep, isAtLastStep);
                }
            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(RESUMED_POSITION, resumePosition);
        outState.putBoolean(PLAYER_STATE, shouldAutoPlay);
    }

    private void setupMediaSection() {
        if (!step.getVideoUrl().isEmpty()) {
            checkMediaType(step.getVideoUrl());
        } else if (!step.getThumbnailUrl().isEmpty()) {
            checkMediaType(step.getThumbnailUrl());
        } else {
            simpleExoPlayerView.setVisibility(View.GONE);
            Picasso.get().load(R.drawable.no_image_available).into(thumbnailView);
        }
    }

    private void setupInstructionSection(View rootView) {
        ((TextView) rootView.findViewById(R.id.instruction_slot)).setText(step.getShortDescription());
    }

    private void setupButtonSection(View rootView, boolean isAtFirstStep, boolean isAtLastStep) {
        Button buttonPrevious = rootView.findViewById(R.id.button_prev);
        Button buttonNext = rootView.findViewById(R.id.button_next);
        buttonPrevious.setTag(BUTTON_PREVIOUS);
        buttonNext.setTag(BUTTON_NEXT);
        buttonPrevious.setOnClickListener((View it) -> {
            releasePlayer();
            mListener.onStepNavButtonClick(BUTTON_PREVIOUS);
        });
        buttonNext.setOnClickListener((View it) -> {
            releasePlayer();
            mListener.onStepNavButtonClick(BUTTON_NEXT);
        });

        if (isAtFirstStep) {
            buttonPrevious.setEnabled(false);
        }
        if (isAtLastStep) {
            buttonNext.setEnabled(false);
        }
    }

    private void checkMediaType(String url) {
        String mimeType = URLConnection.guessContentTypeFromName(url);

        if (mimeType != null && mimeType.startsWith("video")) {
            thumbnailView.setVisibility(View.GONE);
            initializePlayer(Uri.parse(url));
        } else if (mimeType != null && mimeType.startsWith("image")) {
            ImageHandler.loadImage(url, thumbnailView);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        assert getArguments() != null;
        if (getArguments().containsKey(EXTRA_TWO_PANE)) {
            mTwoPane = getArguments().getBoolean(EXTRA_TWO_PANE);
        }
        if (mTwoPane) return;
        try {
            mListener = (OnStepNavButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnStepNavButtonClickListener");
        }
    }

    public Bitmap createThumbnailFromUrl(String url) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(url, new HashMap<>());

        return retriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity());
            simpleExoPlayerView.setPlayer(mExoPlayer);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), "BakingApp"));
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaUri);
            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(shouldAutoPlay);

            boolean haveResumePosition = resumePosition != C.INDEX_UNSET;
            if (haveResumePosition) {
                mExoPlayer.seekTo(resumePosition);
            }
        }
    }

    private void updateResumePosition() {
        resumePosition = mExoPlayer.getContentPosition();
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {

            shouldAutoPlay = mExoPlayer.getPlayWhenReady();
            updateResumePosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer == null) {
            setupMediaSection();
        }
    }
}
