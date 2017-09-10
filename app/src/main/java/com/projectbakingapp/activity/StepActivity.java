package com.projectbakingapp.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projectbakingapp.R;
import com.projectbakingapp.fragment.ListStepFragment;
import com.projectbakingapp.model.Step;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity implements ListStepFragment.OnStepClickListener, ExoPlayer.EventListener {

    private static final String TAG = StepActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.step_name_item)
    TextView stepDescripton;

    @Nullable
    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;

    @Nullable
    @BindView(R.id.view_description)
    LinearLayout viewDescription;

    @Nullable
    @BindView(R.id.step_thumbnail)
    ImageView stepThumbnail;

    @BindView(R.id.list_step_fragment)
    View listStep;

    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private Intent intent;
    private List<Step> stepList;
    private boolean mTwoPane;

    private View mDecorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        intent = getIntent();
        if (intent.hasExtra(getString(R.string.list_step))) {
            Type listType = new TypeToken<List<Step>>() {
            }.getType();
            String resultData = intent.getStringExtra(getString(R.string.list_step));
            Gson gson = new Gson();
            stepList = gson.fromJson(resultData, listType);

            ListStepFragment listStepFragment = new ListStepFragment();
            listStepFragment.setStepList(stepList);
            fragmentManager.beginTransaction()
                    .add(R.id.list_step_fragment, listStepFragment)
                    .commit();
        }

        if (findViewById(R.id.recipe_linear_layout) != null) {
            setModePotrait();

            mTwoPane = true;
            Step step = stepList.get(0);
            stepDescripton.setText(step.getShortDescription());

            if (step.getThumbnailURL() != null && !step.getThumbnailURL().equalsIgnoreCase("")) {
                Picasso.with(StepActivity.this).load(step.getThumbnailURL()).into(stepThumbnail);
            }

            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.question_mark));

            playVideo(step);

        } else {
            mTwoPane = false;
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (findViewById(R.id.recipe_linear_layout) != null) {
            int orientation = newConfig.orientation;

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                setModePotrait();
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setModeLandScape();
            }
        }
    }

    private void setModeLandScape() {
        toolbar.setVisibility(View.GONE);
        viewDescription.setVisibility(View.GONE);
        listStep.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDecorView = getWindow().getDecorView();
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    private void setModePotrait() {
        toolbar.setVisibility(View.VISIBLE);
        viewDescription.setVisibility(View.VISIBLE);
        listStep.setVisibility(View.VISIBLE);
        mDecorView = getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(View.VISIBLE);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void nnStepClickListener(int position) {
        Step step = stepList.get(position);

        if (mTwoPane) {
            stepDescripton.setText(step.getDescription());
            releasePlayer();
            mMediaSession.setActive(false);
            playVideo(step);

        } else {
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            String dataIntent = gson.toJson(step);
            bundle.putString("dataIntent", dataIntent);
            Intent intent = new Intent(this, DetailStepActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }

    }

    private void playVideo(Step step) {
        initializeMediaSession();
        String videoUri = "";
        if (!step.getVideoURL().equalsIgnoreCase("") || step.getVideoURL() == null) {
            videoUri = step.getVideoURL();
        } else {
            videoUri = step.getThumbnailURL();
        }
        initializePlayer(Uri.parse(videoUri));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(this, TAG);

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new MySessionCallback());

        mMediaSession.setActive(true);

    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(this, "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
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
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }

    }

}
