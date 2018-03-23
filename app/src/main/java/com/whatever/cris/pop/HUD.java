package com.whatever.cris.pop;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


/**
 * Created by Cris on 3/23/2018.
 */

public class HUD {

    private final int HUD_SIZE = 20;
    private final int GO_HUD_SIZE = 30;
    private final int FACTOR = 250;
    private final int TEXT_POSITION = 10;
    private String health;
    private String speed;
    private String fps;
    private String traveled;
    private String record;

    public HUD(Context context){
        health = context.getString(R.string.health);
        speed = context.getString(R.string.speed);
        fps = context.getString(R.string.fps);
        traveled = context.getString(R.string.traveled);
        record = context.getString(R.string.newrecord);
    }

    public void drawHUD(Canvas mCanvas, Paint mPaint, Player mPlayer, float mAvgFramerate){
        int size = HUD_SIZE;
        float relation = (float)Math.sqrt(mCanvas.getWidth()*mCanvas.getHeight())/FACTOR;
        float scaleSize = size*relation;
        mPaint.setColor(Color.YELLOW);
        mPaint.setTextSize(scaleSize);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mCanvas.drawText(String.format("%s%d", health, mPlayer.getHealth()),
                TEXT_POSITION, scaleSize, mPaint);
        mCanvas.drawText(String.format(speed, (int)mPlayer.getVelocityX()),
                Game.STAGE_WIDTH/2, scaleSize, mPaint);
        if(Config.SHOW_FPS) {
            mCanvas.drawText(String.format(fps, (int) mAvgFramerate),
                    TEXT_POSITION, Game.STAGE_HEIGHT, mPaint);
        }
    }

    public void drawGameOverHUD(Canvas mCanvas, Paint mPaint,
                                long mDistanceTraveled, long mLongestDistanceTraveled){
        int size = GO_HUD_SIZE;
        float relation = (float)Math.sqrt(mCanvas.getWidth()*mCanvas.getHeight())/FACTOR;
        float scaleSize = size*relation;
        mPaint.setColor(Color.WHITE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(scaleSize);
        String result = String.format(traveled, mDistanceTraveled);
        if(mDistanceTraveled > mLongestDistanceTraveled){
            result = String.format(record, mDistanceTraveled);
        }
        mCanvas.drawText(result, Game.STAGE_WIDTH/2, Game.STAGE_HEIGHT/2 - scaleSize, mPaint);

    }
}
