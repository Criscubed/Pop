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

public class Power extends BitmapEntity{
    public static final String TAG = "Power";
    public static final int SWORD = 0;
    public static final int FLAG = 1;
    public static final int BIRB = 2;
    private static final int FASTER = 2;
    private static final int BACKWARDS = -1;


    private int mType;
    private boolean invokingProjectile;
    private boolean invokingBoom;

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
                Log.w(TAG, String.format(context.getString(R.string.powererror), mType));
                break;
        }
        setBitmap(context, resourceId);
        respawn();
    }

    @Override
    public void input(Game game){
        mPlayerSpeed = game.getPlayerSpeed() * BACKWARDS;
        if(invokingProjectile){
            Projectile p = game.getProjectile();
            p.setX(game.getPlayerX());
            p.setY(game.getPlayerY());
            p.setVelocityX(-mPlayerSpeed * FASTER);
            invokingProjectile = false;
        } else if(invokingBoom){
            Boom b = game.getBoom();
            b.setX(game.getPlayerX());
            b.setY(game.getPlayerY());
            b.setGrowing();
            invokingBoom = false;
        }
    }

    @Override
    public void update(){
        verticalWorldWrap(0, Game.STAGE_HEIGHT - mHeight);
        mY += mVelocityY;
        mX += mPlayerSpeed;;
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

    public void summonProjectile(){
        invokingProjectile = true;
    }
    public void commenceExplosion(){ invokingBoom = true;}
}
