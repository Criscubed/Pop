package com.whatever.cris.pop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GameActivity extends AppCompatActivity {
    private Game nGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nGame = new Game(this);
        setContentView(nGame);
        hideSystemUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nGame.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        nGame.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nGame.onResume();
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
