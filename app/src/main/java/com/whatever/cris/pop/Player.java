package com.whatever.cris.pop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Cris on 2/18/2018.
 */

public class Player extends BitmapEntity {
    public static final String TAG = "Player";
    public static final int MAX_HEALTH = 3;
    public static final int MIN_SPEED = 1;
    public static final int MAX_SPEED = 20;
    public static float ACCELERATION = 2;
    public static final int GRAVITY = 6;
    public static final int PLAYER_SPAWN = 10;

    private String playerDied;
    private boolean mIsBoosting = false;
    private int mHealth = MAX_HEALTH;

    public Player(Context context){
        super();
        setBitmap(context, R.drawable.heart2);
        respawn();
        playerDied = context.getString(R.string.playerdieddebug);
    }

    public void respawn(){
        mHeight = mBitmap.getHeight();
        mWidth = mBitmap.getWidth();
        mY = Game.STAGE_HEIGHT/2 - mHeight/2;
        mX = PLAYER_SPAWN;
        mVelocityX = MIN_SPEED;
        mHealth = MAX_HEALTH;
    }

    @Override
    public void update(){
        if(mIsBoosting){
            mVelocityX += ACCELERATION;
            mVelocityY -= ACCELERATION;
        } else {
            mVelocityX -= ACCELERATION;
            mVelocityY += ACCELERATION;
        }
        mVelocityX = Utils.clamp(mVelocityX, MIN_SPEED, MAX_SPEED);
        mVelocityY = Utils.clamp(mVelocityY, -MAX_SPEED, GRAVITY);
        mY += mVelocityY;
        mY += GRAVITY;
        mY = Utils.clamp(mY, 0, Game.STAGE_HEIGHT - HEIGHT);
        //mX += mVelocityX;
    }
    @Override
    public void onCollision(Entity that){
        if(that instanceof Enemy) {
            mHealth--;
            if (mHealth < 0) {
                Log.d(TAG, playerDied);
            }
        } else if (that instanceof Power){
            Power p = (Power) that;
            int whatDo = p.getPower();
            switch (whatDo){
                case 0: //sword
                    p.summonProjectile();
                    break;
                case 1: //flag
                    mHealth++;
                    break;
                case 2: //birb
                    p.commenceExplosion();
                    break;
            }
            p.respawn();
        }
    }

    @Override
    public void input(Game game){
        mIsBoosting = game.isBooosting();
    }

    public int getHealth() {
        return mHealth;
    }
}
