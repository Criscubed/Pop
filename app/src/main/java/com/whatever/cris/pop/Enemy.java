package com.whatever.cris.pop;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;


/**
 * Created by Cris on 2/17/2018.
 */

public class Enemy extends Entity {
    protected float mPlayerSpeed = 0.0f;
    public static final String TAG = "Enemy";
    public static final float ENEMY_MAX_SPEED = 3;
    public static final int ENEMY_HEIGHT = 50;
    public static final float VELOCITY_VARIANCE = 2;
    private int sinCenter;
    private int mSelect;
    Bitmap mBitmap;

    public Enemy(Context context){
        super();
        mSelect = mDice.nextInt(3);
        int resourceId = R.drawable.skull;
        switch(mSelect){
            case 0:
                resourceId =  R.drawable.skull;
                break;
            case 1:
                resourceId = R.drawable.no;
                break;
            case 2:
                resourceId = R.drawable.warning;
                break;
            default:
                Log.w(TAG, "You messed up your EnemyID" + mSelect);
                break;
        }
        Bitmap temp = BitmapFactory.decodeResource(context.getResources(), resourceId);
        mBitmap = Utils.scaleToTargetHeight(temp ,  ENEMY_HEIGHT);
        mBitmap = Utils.flipBitmap(mBitmap, false);
        if(temp != mBitmap){
            temp.recycle();
        }
        respawn();

    }

    @Override
    public void input(Game game){
        mPlayerSpeed = game.getPlayerSpeed() * -1;
    }

    @Override
    public void update(){
        verticalWorldWrap(0, Game.STAGE_HEIGHT - mHeight);
        mX += mVelocityX;
        switch (mSelect){
            case 0:
                mY += mVelocityY;
                verticalWorldWrap(sinCenter - Game.STAGE_HEIGHT/8, sinCenter + Game.STAGE_HEIGHT/8);
                break;
            case 1:
                mY += 2 * mVelocityY;
                break;
            case 2:
                mY += mDice.nextFloat() * VELOCITY_VARIANCE + mVelocityY;
                mX += mPlayerSpeed/4;
                break;
            default:
                break;
        }
        mX += mPlayerSpeed;
    }

    @Override
    public void render(final Canvas canvas,final Paint paint) {
        canvas.drawBitmap(mBitmap, mX, mY, paint);
    }

    public void respawn(){
        mHeight = mBitmap.getHeight();
        mWidth = mBitmap.getWidth();
        mY = mDice.nextInt(Game.STAGE_HEIGHT - (int) mHeight);
        mX = Game.STAGE_WIDTH + mDice.nextInt((int)mWidth);
        mVelocityX = -1 + -mDice.nextInt((int)ENEMY_MAX_SPEED);
        mVelocityY = -1 + -mDice.nextInt((int)ENEMY_MAX_SPEED);
        mSelect = mDice.nextInt(3);
        if(mSelect == 0){
            mY = Game.STAGE_HEIGHT/2;
        }
        sinCenter = mDice.nextInt(Game.STAGE_HEIGHT);
    }

    @Override
    public void worldWrap(final float width, final float height){
        if(mX < -mWidth){
            respawn();
        }
    }

    public void verticalWorldWrap(float min, float max){
        if(mY < min){
            mVelocityY = (mVelocityY > 0) ? mVelocityY : -mVelocityY;
        } else if(mY > max){
            mVelocityY = (mVelocityY > 0) ? -mVelocityY : mVelocityY;
        }

    }

    @Override
    public void onCollision(Entity that){
        respawn();
    }



}
