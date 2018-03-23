package com.whatever.cris.pop;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class Star extends Entity {

    protected static final float MIN_STAR_SIZE = 5;
    protected static final float MAX_STAR_SIZE = 12;
    protected static final int TYPES_OF_STARS = 5;
    protected static final int BACKWARDS = -1;

    protected float mPlayerSpeed = 0.0f;
    protected int mStarColor;

    public Star(){
        super(mDice.nextInt(Game.STAGE_WIDTH),
                mDice.nextInt(Game.STAGE_HEIGHT));
        mVelocityX = -mDice.nextFloat();
        mHeight = ((-mVelocityX - 0)/ (1 - 0)) * (MAX_STAR_SIZE - MIN_STAR_SIZE) + MIN_STAR_SIZE;
        mWidth = mHeight;
        mStarColor = mDice.nextInt(TYPES_OF_STARS);
    }

    @Override
    public void input(Game game){
        mPlayerSpeed = game.getPlayerSpeed() * BACKWARDS;
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
                paint.setColor(Color.argb(Config.S1_A, Config.S1_R, Config.S1_G, Config.S1_B));
                break;
            case 1:
                paint.setColor(Color.argb(Config.S2_A, Config.S2_R, Config.S2_G, Config.S2_B));
                break;
            case 2:
                paint.setColor(Color.argb(Config.S3_A, Config.S3_R, Config.S3_G, Config.S3_B));
                break;
            case 3:
                paint.setColor(Color.argb(Config.S4_A, Config.S4_R, Config.S4_G, Config.S4_B));
                break;
            case 4:
                paint.setColor(Color.argb(Config.S5_A, Config.S5_R, Config.S5_G, Config.S5_B));
                break;
            default:
                paint.setColor(Color.WHITE);
                break;

        }
        canvas.drawCircle(mX, mY,mWidth, paint);
    }
}
