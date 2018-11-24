package com.example.android.bakingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.HashMap;

import static com.example.android.bakingapp.RecipeDetailActivity.EXTRA_TWO_PANE;

public class StepDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String BUTTON_PREVIOUS = "button_previous";
    public static final String BUTTON_NEXT = "button_next";

    private Step step;
    private SimpleExoPlayer mExoPlayer;
    private PlayerView simpleExoPlayerView;
    private ImageView thumbnailView;
    private OnNavButtonClickListener mListener;
    private boolean mTwoPane;

    public interface OnNavButtonClickListener {
        void onNavButtonClick(String tag);
    }

    public StepDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail_view, container, false);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            step = (Step) getArguments().getSerializable(ARG_ITEM_ID);
        }

        if (!mTwoPane) {
            Button buttonPrevious = rootView.findViewById(R.id.button_prev);
            Button buttonNext = rootView.findViewById(R.id.button_next);
            buttonPrevious.setTag(BUTTON_PREVIOUS);
            buttonNext.setTag(BUTTON_NEXT);
            buttonPrevious.setOnClickListener(it -> mListener.onNavButtonClick(BUTTON_PREVIOUS));
            buttonNext.setOnClickListener(it -> mListener.onNavButtonClick(BUTTON_NEXT));
        }

        if (step != null) {
            ((TextView) rootView.findViewById(R.id.instruction_slot)).setText(step.getShortDescription());
            thumbnailView = rootView.findViewById(R.id.media_slot);
            simpleExoPlayerView = rootView.findViewById(R.id.player_view);
//            simpleExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground));

            if (!step.getVideoUrl().isEmpty()) {
                initializePlayer(Uri.parse(step.getVideoUrl()));
            } else {
                simpleExoPlayerView.setVisibility(View.GONE);
            }

            if (!step.getThumbnailUrl().isEmpty()) {
                Bitmap bitmap = createThumbnailFromUrl(step.getThumbnailUrl());
                thumbnailView.setImageBitmap(bitmap);
            } else {
                thumbnailView.setVisibility(View.GONE);
            }
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments().containsKey(EXTRA_TWO_PANE)) {
            mTwoPane = getArguments().getBoolean(EXTRA_TWO_PANE);
        }
        if (mTwoPane) return;
        try {
            mListener = (OnNavButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnNavButtonClickListener");
        }
    }


    public Bitmap createThumbnailFromUrl(String url){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(url, new HashMap<>());
        Bitmap image = retriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

        return image;
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity());
            simpleExoPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), "BakingApp"));
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaUri);
            // Prepare the player with the source.
            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
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

}
