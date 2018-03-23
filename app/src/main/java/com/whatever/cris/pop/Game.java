package com.whatever.cris.pop;

import android.content.Context;
import android.content.SharedPreferences;
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
    private SurfaceHolder mHolder;
    private Paint mPaint;
    private Canvas mCanvas;
    private boolean mIsBoosting;
    private ArrayList<Entity> mEntities = new ArrayList<>();
    private Player mPlayer;
    private Projectile mProjectile;
    private Boom mBoom;
    private boolean mGameOver = false;
    private long mDistanceTraveled = 0;
    private long mLongestDistanceTraveled = 0;

    private SharedPreferences mPrefs;
    public static final String PREFS = "com.whatever.cris.pop";
    public static final String LONGEST_DIST = "longest_distance";

    private HUD mHud;
    private long mLastSampleTime = 0;
    private long mFrameCount = 0;
    private float mAvgFramerate = 0;

    private SoundManager mSoundManager;

    public Game(final Context context) {
        super(context);
        mHud = new HUD(context);
        mSoundManager = new SoundManager(context);
        mHolder = getHolder();
        mHolder.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        for(int i = 0; i < Config.STAR_COUNT; i++){
            mEntities.add(new Star());
        }
        for(int i = 0; i < Config.ENEMY_COUNT; i++){
            mEntities.add(new Enemy(context));
        }
        mPlayer = new Player(context);
        mEntities.add(new Power(context, Power.FLAG));
        mEntities.add(new Power(context, Power.BIRB));
        mEntities.add(new Power(context, Power.SWORD));
        mProjectile = new Projectile();
        mEntities.add(mProjectile);
        mBoom = new Boom();
        mEntities.add(mBoom);
        mPrefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        startGame();
    }

    private void startGame(){
        mPlayer.respawn();
        for(final Entity e: mEntities){
            e.respawn();
        }
        mGameOver = false;
        mDistanceTraveled = 0;
        mLongestDistanceTraveled = mPrefs.getLong(LONGEST_DIST, 0);

    }

    @Override
    public void run() {
        while(mIsRunning){
            long start = System.nanoTime();
            onEnterFrame();
            input();
            update();
            checkCollisions();
            checkForGameOver();
            render();
            rateLimit(start);
        }
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

    private void input(){
        mPlayer.input(this);
        for(final Entity e: mEntities){
            e.input(this);
        }
    }
    private void update(){
        if(mGameOver){
            return;
        }
        mPlayer.update();
        for(final Entity e: mEntities){
            e.update();
            e.worldWrap(STAGE_WIDTH, STAGE_HEIGHT);
        }
        mDistanceTraveled += mPlayer.getVelocityX();
    }
    private void render(){
        if (!acquireAndLockCanvas()){
            return;
        }
        mCanvas.drawColor(Color.argb(Config.BG_A,
                Config.BG_R,
                Config.BG_G,
                Config.BG_B));

        for(final Entity e: mEntities){
            e.render(mCanvas, mPaint);
        }
        mPlayer.render(mCanvas, mPaint);
        if(mGameOver){
            mHud.drawGameOverHUD(mCanvas, mPaint, mDistanceTraveled, mLongestDistanceTraveled);
        } else {
            mHud.drawHUD(mCanvas, mPaint, mPlayer, mAvgFramerate);
        }
        mHolder.unlockCanvasAndPost(mCanvas);
    }

    private void checkCollisions(){
        int ic = mEntities.size();
        Entity temp;
        for(int i = 0; i < ic; i++){
            temp = mEntities.get(i);
            if(mPlayer.isColliding(temp)){
                temp.onCollision(mPlayer);
                mPlayer.onCollision(temp);
                if(temp instanceof Enemy){
                    mSoundManager.play(SoundManager.CRASH);
                }
            }
            if(mProjectile.isColliding(temp)){
                if(temp instanceof Enemy){
                    temp.onCollision(mProjectile);
                    mProjectile.onCollision(temp);
                }
            } else if( mBoom.isColliding(temp)){
                if(temp instanceof Enemy){
                    temp.onCollision(mBoom);
                }
            }
        }
    }




    private boolean acquireAndLockCanvas() {
        if(!mHolder.getSurface().isValid()){
            return false;
        }
        mCanvas = mHolder.lockCanvas();
        return mCanvas != null;

    }


    private void checkForGameOver(){
        if(mGameOver){
            return;
        }
        mGameOver = (mPlayer.getHealth() <= 0);
        if(mDistanceTraveled > mLongestDistanceTraveled){
            SharedPreferences.Editor edit = mPrefs.edit();
            edit.putLong(LONGEST_DIST, mDistanceTraveled);
            edit.apply();
        }
    }

    public void rateLimit(long start){
        float millisRemaining = ((start + NANOS_PER_FRAME) - System.nanoTime()) * NANOS_TO_MILLIS;
        if(millisRemaining > 1){
            try {
                Thread.sleep((long)millisRemaining);
            } catch (InterruptedException e) {
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
        Log.d(TAG, String.format(getContext().getString(R.string.fpsdebug), mAvgFramerate));
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event){
        switch(event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                mIsBoosting = true;
                mSoundManager.play(SoundManager.BOOST);
                break;
            case MotionEvent.ACTION_UP:
                mIsBoosting = false;
                if(mGameOver){
                    startGame();
                }
                break;
            default:
                //no action
                break;
        }
        return true;
    }

    public boolean isBooosting(){
        return mIsBoosting;
    }

    //Q&D: stand in for an input manager interface
    public float getPlayerSpeed(){
        return mPlayer.getVelocityX();
    }

    public SoundManager getSoundManager(){
        return mSoundManager;
    }

    public Projectile getProjectile(){
        return mProjectile;
    }
    public Boom getBoom(){ return mBoom; }

    public float getPlayerX(){
        return mPlayer.getX();
    }

    public float getPlayerY(){
        return mPlayer.getY();
    }

}
