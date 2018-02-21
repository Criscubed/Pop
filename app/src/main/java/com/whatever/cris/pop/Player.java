package com.whatever.cris.pop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Cris on 2/18/2018.
 */

public class Player extends Entity {
    public static final int MAX_HEALTH = 3;
    public static final int MIN_SPEED = 1;
    public static final int MAX_SPEED = 20;
    public static final int GRAVITY = 12;
    public static final int HEIGHT = 50;
    private Bitmap mBitmap;

    public Player(Context context){
        super();
        Bitmap temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart2);
        mBitmap = Utils.scaleToTargetHeight(temp ,  HEIGHT);
        respawn();
    }

    public void respawn(){
        mHeight = mBitmap.getHeight();
        mWidth = mBitmap.getWidth();
        mY = Game.STAGE_HEIGHT/2 - mHeight/2;
        mX = 10;
        mVelocityX = MIN_SPEED;
    }

    @Override
    public void update(){
        mY += GRAVITY;
        mY = Utils.clamp(mY, 0, Game.STAGE_HEIGHT - HEIGHT);
        mX += mVelocityX;
    }

    @Override
    public void render(final Canvas canvas, final Paint paint) {
        canvas.drawBitmap(mBitmap, mX, mY, paint);
    }
}
