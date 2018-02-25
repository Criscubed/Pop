package com.whatever.cris.pop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Cris on 2/25/2018.
 */

public class Power extends Entity{
    public static final String TAG = "Power";
    public static final int POWER_HEIGHT = 50;
    public static final int SWORD = 0;
    public static final int FLAG = 1;
    public static final int BIRB = 2;

    private Bitmap mBitmap;
    private int mType;
    private float mPlayerSpeed;
    private int sinCenter;

    public Power(Context context, int type){
        super();
        mType = type;
        int resourceId = R.drawable.birb;
        switch(mType){
            case 0:
                resourceId =  R.drawable.sword;
                break;
            case 1:
                resourceId = R.drawable.flag;
                break;
            case 2:
                resourceId = R.drawable.birb;
                break;
            default:
                Log.w(TAG, "Those arent real powerups" + mType);
                break;
        }
        Bitmap temp = BitmapFactory.decodeResource(context.getResources(), resourceId);
        mBitmap = Utils.scaleToTargetHeight(temp ,  POWER_HEIGHT);
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
        mY += mVelocityY;
        mX += mPlayerSpeed;;
    }

    @Override
    public void render(final Canvas canvas, final Paint paint) {
        canvas.drawBitmap(mBitmap, mX, mY, paint);
    }

    public int getPower(){
        return mType;
    }

    public void verticalWorldWrap(float min, float max){
        if(mY < min){
            mVelocityY = (mVelocityY > 0) ? mVelocityY : -mVelocityY;
        } else if(mY > max){
            mVelocityY = (mVelocityY > 0) ? -mVelocityY : mVelocityY;
        }

    }

    public void respawn(){
        mHeight = mBitmap.getHeight();
        mWidth = mBitmap.getWidth();
        mY = mDice.nextInt(Game.STAGE_HEIGHT - (int) mHeight);
        mX = Game.STAGE_WIDTH + mDice.nextInt((int)mWidth);
        mVelocityX = -1 + -mDice.nextInt((int)Enemy.ENEMY_MAX_SPEED);
        mVelocityY = -1 + -mDice.nextInt((int)Enemy.ENEMY_MAX_SPEED);
        sinCenter = mDice.nextInt(Game.STAGE_HEIGHT);
    }

    @Override
    public void worldWrap(final float width, final float height){
        if(mX < -mWidth){
            respawn();
        }
    }

    @Override
    public void onCollision(Entity that){
        respawn();
    }
}
