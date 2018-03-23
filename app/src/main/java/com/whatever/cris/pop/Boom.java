package com.whatever.cris.pop;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Cris on 3/23/2018.
 */

public class Boom extends Entity{

    private static int NOT_MOVING = 0;
    private static int PROJECTILE_SIZE = 5;
    private static int HIDDEN_POSITION = -PROJECTILE_SIZE - 1;
    private static int BOOM_STROKE_WIDTH = 10;

    private boolean growing = false;
    private float mPlayerSpeed;

    public Boom() {
        respawn();
    }

    @Override
    public void input(Game game){
        mPlayerSpeed = game.getPlayerSpeed();
    }

    @Override
    public void update() {
        if(growing){
            mWidth+= mPlayerSpeed;
            mHeight+= mPlayerSpeed;
        }
        if(mWidth > Game.STAGE_HEIGHT/2){
            respawn();
        }
    }

    @Override
    public void render(final Canvas canvas, final Paint paint){
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(BOOM_STROKE_WIDTH);
        canvas.drawCircle(mX, mY, mWidth, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    public void respawn(){
        mX = HIDDEN_POSITION;
        mY = HIDDEN_POSITION;
        mWidth = PROJECTILE_SIZE;
        mHeight = PROJECTILE_SIZE;
        growing = false;
    }

    public void setGrowing(){
        growing = true;
    }
}
