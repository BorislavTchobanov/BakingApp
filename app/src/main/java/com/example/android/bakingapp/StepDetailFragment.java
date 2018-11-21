package com.example.android.bakingapp;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class StepDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Step step;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView simpleExoPlayerView;

    public StepDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail_view, container, false);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            step = (Step) getArguments().getSerializable(ARG_ITEM_ID);
        }

        if (step != null) {
            ((TextView) rootView.findViewById(R.id.instruction_slot)).setText(step.getShortDescription());
            simpleExoPlayerView = rootView.findViewById(R.id.player_view);
            simpleExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground));

            if (!step.getVideoUrl().isEmpty()) {
                initializePlayer(Uri.parse(step.getVideoUrl()));
            } else if (!step.getThumbnailUrl().isEmpty()) {
                initializePlayer(Uri.parse(step.getThumbnailUrl()));
            }
        }

        return rootView;
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
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
