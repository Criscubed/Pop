package com.whatever.cris.pop;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Cris on 2/7/2018.
 */

public class Game extends SurfaceView implements Runnable {
    public static final String TAG = "Game";
    private boolean mIsRunning = false;
    private Thread mGameThread = null;
    private static final int STAGE_WIDTH = 1280;
    private static final int STAGE_HEIGHT = 720;
    private SurfaceHolder mHolder;

    public Game(final Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT);

    }

    public void onResume() {
        mIsRunning = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    public void onPause() {
        mIsRunning =false;
        try{
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.d(TAG, Log.getStackTraceString(e.getCause()))
            e.printStackTrace();
        }
    }

    public void onDestroy() {
    }

    private void input(){

    }

    private void update(){

    }

    private void render(){
m
    }

    @Override
    public void run() {
        while(mIsRunning){
            input();
            update();
            render();
        }
    }
}
