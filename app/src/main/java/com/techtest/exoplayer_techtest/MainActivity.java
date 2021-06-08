package com.techtest.exoplayer_techtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaExtractor;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.techtest.techtest_framework.exoPlayerStatsManager;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Context mContext;
    exoPlayerStatsManager statsManager;

    //creating a variable for exoplayerview.
    PlayerView playerView;
    //creating a variable for exoplayer
    SimpleExoPlayer player;
    //url of video which we are loading.
    String urlStream="http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext =this;

        playerView = findViewById(R.id.exoPlayerView);
        initializePlayer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT>23){
            initializePlayer();
        }
    }

    private void initializePlayer() {
        //bandwisthmeter is used for getting default bandwidth
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(getApplicationContext()).build();
        //we are ading our track selector to exoplayer.
        player= new SimpleExoPlayer.Builder(getApplicationContext()).build();
        // we are parsing a video url and parsing its video uri.
        Uri videouri= Uri.parse(urlStream);
        HlsMediaSource mediaSource1 = buildMediaSource(videouri);

        //inside our exoplayer view we are setting our player
        playerView.setPlayer(player);
        player.setMediaSource(mediaSource1);
        //we are preparing our exoplayer with media source.
        player.prepare();
        //we are setting our exoplayer when it is ready.
        player.setPlayWhenReady(true);
        statsManager = new exoPlayerStatsManager(player, getApplicationContext());
    }

    private HlsMediaSource buildMediaSource(Uri uri) {
        // we are creating a variable for datasource factory and setting its user agent as 'exoplayer_view'
        DataSource.Factory dataSourceFactory1 = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), "firebasestorageimage"));
        HlsDataSourceFactory hlsDataSourceFactory = new DefaultHlsDataSourceFactory(dataSourceFactory1);
         //we are creating a media source with above variables and passing our event handler as null,
        HlsMediaSource.Factory mediaSource = new HlsMediaSource.Factory(hlsDataSourceFactory);
        return mediaSource.createMediaSource(MediaItem.fromUri(uri));
    }
}