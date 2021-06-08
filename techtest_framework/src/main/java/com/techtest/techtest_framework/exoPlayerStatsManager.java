package com.techtest.techtest_framework;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class exoPlayerStatsManager {
SimpleExoPlayer player;
int timesPaused;
int timesResumed;
boolean firstPlay;

LocalTime lastPause;
LocalTime lastResume;
DateTimeFormatter f;
volleyRequests requests;

Context context;

    public exoPlayerStatsManager(SimpleExoPlayer player, Context context) {
        int timesPaused = 0;
        int timesResumed = 0;
        firstPlay = true;

        this.player = player;
        this.context = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            f = DateTimeFormatter.ofPattern( "hh:mm:ss a" );
        }
        requests = new volleyRequests(context);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lastPause = LocalTime.now();
            lastResume = LocalTime.now();
        }
        player.addListener(new Player.Listener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying){
                    timesResumed++;
                        lastResume = LocalTime.now();
                        Toast.makeText(context,  "Resumed for " + timesResumed + " time "  + timeBetween(lastPause, LocalTime.now()) + " since last pause", Toast.LENGTH_LONG).show();

                    } else {
                    timesPaused++;
                        lastPause = LocalTime.now();
                        Toast.makeText(context,  "Paused for " + timesResumed + " time "  + timeBetween(lastResume, LocalTime.now()) + " since last resume", Toast.LENGTH_LONG).show();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    requests.finishMessage();
                    new AlertDialog.Builder(context)
                            .setTitle("Final Stats")
                            .setMessage("Times Resumed/Played: " + timesResumed +
                                    "\nTimes Paused: " + timesPaused +
                                    "\nTime since last Resume: " + timeBetween(lastResume, LocalTime.now()) +
                                    "\nTime since last Pause: " + timeBetween(lastPause, LocalTime.now()))
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                }
            }

            @Override
            public void onIsLoadingChanged(boolean isLoading) {
                if (firstPlay && isLoading){
                    firstPlay = false;
                    requests.startMessage();
                }
            }

            @Override
            public void onRenderedFirstFrame() {
                requests.frameMessage();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String timeBetween(LocalTime time1, LocalTime time2) {
        Duration d = Duration.between( time1 , time2 );
        return formatDuration(d);
    }

    public void setPlayer (SimpleExoPlayer player) {
        this.player = player;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        @SuppressLint("DefaultLocale") String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

}
