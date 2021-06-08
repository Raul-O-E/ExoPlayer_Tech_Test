package com.techtest.techtest_framework;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.time.Duration;
import java.time.Instant;

public class exoPlayerStatsManager {
SimpleExoPlayer player;
int timesPaused;
int timesResumed;

String timesSinceLastPause;
String timesSinceLastResume;
Context context;

    public exoPlayerStatsManager(SimpleExoPlayer player, Context context) {
        int timesPaused = 0;
        int timesResumed = 0;
        this.player = player;
        this.context = context;
        init();
    }

    private void init() {
        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying){
                    timesResumed++;

                    Toast.makeText(context,  "Resumed for " + timesResumed + " time", Toast.LENGTH_LONG).show();
                    Toast.makeText(context,  "Last pause was" + timeBetween(timesSinceLastPause, timesSinceLastResume) + " ago", Toast.LENGTH_LONG).show();
                } else {
                    timesPaused++;

                    Toast.makeText(context,  "Paused for " + timesPaused + " time", Toast.LENGTH_LONG).show();
                    Toast.makeText(context,  "Last resume was " + timeBetween(timesSinceLastResume, timesSinceLastPause) + " ago", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String timeBetween(String time1, String time2) {
        return "";
    }

    public void setPlayer (SimpleExoPlayer player) {
        this.player = player;
    }

}
