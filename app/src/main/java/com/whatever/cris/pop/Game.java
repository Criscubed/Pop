package com.whatever.cris.pop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Cris on 2/7/2018.
 */

public class Game extends SurfaceView implements Runnable {
    public static final String TAG = "Game";
    public static final int STAGE_WIDTH = 1280;
    public static final int STAGE_HEIGHT = 720;
    public static final int STAR_COUNT = 64;
    public static final int ENEMY_COUNT = 6;
    public static final long SECONDS_TO_NANOS = 1000000000;
    public static final long MILLIS_TO_NANOS = 1000000;
    public static final float NANOS_TO_MILLIS  = 1.0f/MILLIS_TO_NANOS;
    public static final float NANOS_TO_SECONDS = 1.0f/SECONDS_TO_NANOS;
    public static final long SAMPLE_INTERVAL = 1 * SECONDS_TO_NANOS;
    public static final long TARGET_FRAMERATE = 30;
    public static final long MS_PER_FRAME = 1000/TARGET_FRAMERATE;
    public static final long  NANOS_PER_FRAME = MS_PER_FRAME * MILLIS_TO_NANOS;

    private boolean mIsRunning = false;
    private Thread mGameThread = null;
    private long mLastSampleTime = 0;
    private long mFrameCount = 0;
    private float mAvgFramerate = 0;
    private SurfaceHolder mHolder;
    private Paint mPaint;
    private Canvas mCanvas;
    private boolean mIsBoosting;

    private ArrayList<Entity> mEntities = new ArrayList<>();
    private Player mPlayer;


    public Game(final Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        for(int i = 0; i < STAR_COUNT; i++){
            mEntities.add(new Star());
        }
        for(int i = 0; i < ENEMY_COUNT; i++){
            mEntities.add(new Enemy(context));
        }
        mPlayer = new Player(context);
        mEntities.add(mPlayer);

    }

    public void onResume() {
        Log.d(TAG, "OnResume");
        mIsRunning = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    public void onPause() {
        Log.d(TAG, "onPause");
        mIsRunning =false;
        try{
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.d(TAG, Log.getStackTraceString(e.getCause()));
            e.printStackTrace();
        }
    }

    public void onDestroy() {
    }

    //Q&D: stand in for an input manager interface
    public float getPlayerSpeed(){
        return mPlayer.getVelocityX();
    }
    private void input(){
        for(final Entity e: mEntities){
            e.input(this);
        }
    }

    public boolean isBooosting(){
        return mIsBoosting;
    }

    private void update(){
        for(final Entity e: mEntities){
            e.update();
            e.worldWrap(STAGE_WIDTH, STAGE_HEIGHT);
        }
    }

    private void render(){
        if (!acquireAndLockCanvas()){
            return;
        }
        mCanvas.drawColor(Color.argb(255, 0x1D, 0X00, 0X33));

        for(final Entity e: mEntities){
            e.render(mCanvas, mPaint);
        }
        drawHUD();
        mHolder.unlockCanvasAndPost(mCanvas);
    }

    public void drawHUD(){
        int size = 20;
        float relation = (float)Math.sqrt(mCanvas.getWidth()*mCanvas.getHeight())/250;
        float scaleSize = size*relation;
        mPaint.setColor(Color.YELLOW);
        mPaint.setTextSize(scaleSize);
        mCanvas.drawText("FPS: " + mAvgFramerate, 10, scaleSize, mPaint);
    }

    private boolean acquireAndLockCanvas() {
        if(!mHolder.getSurface().isValid()){
            return false;
        }
        mCanvas = mHolder.lockCanvas();
        return mCanvas != null;

    }

    @Override
    public void run() {
        while(mIsRunning){
            long start = System.nanoTime();
            onEnterFrame();
            input();
            update();
            render();
            float millisRemaining = ((start + NANOS_PER_FRAME) - System.nanoTime()) * NANOS_TO_MILLIS;
            if(millisRemaining > 1){
                try {
                    Thread.sleep((long)millisRemaining);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private void onEnterFrame(){
        mFrameCount++;
        long timeSinceLast = System.nanoTime() - mLastSampleTime;
        if(timeSinceLast < SAMPLE_INTERVAL){
            return;
        }
        mAvgFramerate = mFrameCount / (timeSinceLast * NANOS_TO_SECONDS);
        mLastSampleTime = System.nanoTime();
        mFrameCount = 0;
        Log.d(TAG, "FPS: " + mAvgFramerate);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event){
        switch(event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                mIsBoosting = true;
                break;
            case MotionEvent.ACTION_UP:
                mIsBoosting = false;
                break;
            default:
                //no action
                break;
        }
        return true;
    }
}
