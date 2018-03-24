package com.whatever.cris.pop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;


/**
 * Created by Cris on 2/17/2018.
 */

public class Enemy extends BitmapEntity {
    public static final String TAG = "Enemy";
    public static final float ENEMY_MAX_SPEED = 3;
    public static final float VELOCITY_VARIANCE = 2;
    public static final int TYPES_OF_ENEMIES = 3;
    public static final int SINNERS = 8;
    public static final int AMBUSHERS = 2;
    public static final int DROPPERS = 4;
    public static final int BACKWARDS = -1;

    private int sinCenter;
    private int mSelect;

    public Enemy(Context context){
        super();
        mSelect = mDice.nextInt(TYPES_OF_ENEMIES);
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
                Log.w(TAG, String.format(context.getString(R.string.enemyerror), mSelect));
                break;
        }
        setBitmap(context, resourceId);
        respawn();

    }

    @Override
    public void input(Game game){
        mPlayerSpeed = game.getPlayerSpeed() * BACKWARDS;
    }

    @Override
    public void update(){
        verticalWorldWrap(0, Game.STAGE_HEIGHT - mHeight);
        mX += mVelocityX;
        switch (mSelect){
            case 0:
                mY += mVelocityY;
                verticalWorldWrap(sinCenter - Game.STAGE_HEIGHT/SINNERS,
                        sinCenter + Game.STAGE_HEIGHT/SINNERS);
                break;
            case 1:
                mY += AMBUSHERS * mVelocityY;
                break;
            case 2:
                mY += mDice.nextFloat() * VELOCITY_VARIANCE + mVelocityY;
                mX += mPlayerSpeed/DROPPERS;
                break;
            default:
                break;
        }
        mX += mPlayerSpeed;
    }

    public void respawn(){
        mHeight = mBitmap.getHeight();
        mWidth = mBitmap.getWidth();
        mY = mDice.nextInt(Game.STAGE_HEIGHT - (int) mHeight);
        mX = Game.STAGE_WIDTH + mDice.nextInt((int)mWidth);
        mVelocityX = -1 + -mDice.nextInt((int)ENEMY_MAX_SPEED);
        mVelocityY = -1 + -mDice.nextInt((int)ENEMY_MAX_SPEED);
        mSelect = mDice.nextInt(TYPES_OF_ENEMIES);
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
