package com.whatever.cris.pop;

import android.util.Log;

/**
 * Created by Cris on 3/24/2018.
 */

public class Frame {
    public static final long SECONDS_TO_NANOS = 1000000000;
    public static final long MILLIS_TO_NANOS = 1000000;
    public static final float NANOS_TO_MILLIS  = 1.0f/MILLIS_TO_NANOS;
    public static final float NANOS_TO_SECONDS = 1.0f/SECONDS_TO_NANOS;
    public static final long SAMPLE_INTERVAL = 1 * SECONDS_TO_NANOS;
    public static final long TARGET_FRAMERATE = 30;
    public static final long MS_PER_FRAME = 1000/TARGET_FRAMERATE;
    public static final long  NANOS_PER_FRAME = MS_PER_FRAME * MILLIS_TO_NANOS;

    private long mLastSampleTime = 0;
    private long mFrameCount = 0;
    private float mAvgFramerate = 0;

    public long start;

    public Frame(){

    }

    public void start(){
        start = System.nanoTime();
        onEnterFrame();
    }

    public void rateLimit(){
        float millisRemaining = ((start + NANOS_PER_FRAME) -
                System.nanoTime()) * NANOS_TO_MILLIS;
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
        //Log.d(TAG, String.format(getContext().getString(R.string.fpsdebug), mAvgFramerate));
    }

    public float getmAvgFramerate(){
        return mAvgFramerate;
    }

}
