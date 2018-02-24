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
    Bitmap mBitmap;

    public Enemy(Context context){
        super();
        int select = mDice.nextInt(3);
        int resourceId = R.drawable.skull;
        switch(select){
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
                Log.w(TAG, "You messed up your EnemyID" + select);
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
        super.update();
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
