package com.whatever.cris.pop;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class Star extends Entity {
    protected static final float MIN_STAR_SIZE = 5;
    protected static final float MAX_STAR_SIZE = 12;
    protected float mPlayerSpeed = 0.0f;
    protected int mStarColor;
    public Star(){
        super(mDice.nextInt(Game.STAGE_WIDTH),
                mDice.nextInt(Game.STAGE_HEIGHT));
        mVelocityX = -mDice.nextFloat();
        mHeight = ((-mVelocityX - 0)/ (1 - 0)) * (MAX_STAR_SIZE - MIN_STAR_SIZE) + MIN_STAR_SIZE;
        mWidth = mHeight;
        mStarColor = mDice.nextInt(5);
    }

    @Override
    public void input(Game game){
        mPlayerSpeed = game.getPlayerSpeed() * -1;
    }

    @Override
    public void update(){
        super.update();
        mX -= ((-mVelocityX - 0)/ (1 - 0)) * (-mPlayerSpeed - Player.MIN_SPEED) + Player.MIN_SPEED;
    }
    @Override
    public void render(final Canvas canvas, final Paint paint){
        switch(mStarColor){
            case 0:
                paint.setColor(Color.argb(255, 0x90, 0x0C, 0x3F));
                break;
            case 1:
                paint.setColor(Color.argb(255, 0xC7, 0x00, 0x39));
                break;
            case 2:
                paint.setColor(Color.argb(255, 0xFF, 0x57, 0x33));
                break;
            case 3:
                paint.setColor(Color.argb(255, 0xFF, 0xC3, 0x0F));
                break;
            case 4:
                paint.setColor(Color.argb(255, 0x58, 0x18, 0x45));
                break;
            default:
                paint.setColor(Color.WHITE);
                break;

        }
        canvas.drawCircle(mX, mY,mWidth, paint);
    }
}
