package com.whatever.cris.pop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    public static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();
        final Button startGameButton = findViewById(R.id.startButton);
        startGameButton.setOnClickListener(this);

        SharedPreferences prefs = getSharedPreferences(Game.PREFS, Context.MODE_PRIVATE);
        Long longestDist = prefs.getLong(Game.LONGEST_DIST, 0);

        final TextView highScore = (TextView) findViewById(R.id.highScoreText);
        highScore.setText(String.format(getString(R.string.longdist), longestDist));
    }

    @Override
    public void onClick(View view) {
        final Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        finish();
    }

    // Snippet grabbed from
    // https://developer.android.com/training/system-ui/immersive.html
    // This snippet hides the system bars.
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
