package com.whatever.cris.pop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Cris on 3/15/2018.
 */

public class Projectile extends Entity{
    protected float mPlayerSpeed = 0.0f;

    private static final int NOT_MOVING = 0;
    private static final int PROJECTILE_SIZE = 15;
    private static final int HIDDEN_POSITION = -PROJECTILE_SIZE - 1;
    private static final int GROWTH_INTERVAL = 10;
    private static final int FASTER = 2;

    public Projectile(){
        respawn();
    }

    @Override
    public void input(Game game){
        mPlayerSpeed = game.getPlayerSpeed() * FASTER;
    }

    @Override
    public void update() {
        super.update();
        mX += mPlayerSpeed;
    }

    @Override
    public void render(final Canvas canvas, final Paint paint){
        paint.setColor(Color.WHITE);
        canvas.drawCircle(mX, mY, mWidth, paint);
    }

    @Override
    public void worldWrap(final float width, final float height){
        if(mX > width + mWidth){
            respawn();
        }
    }

    public void respawn(){
        mX = HIDDEN_POSITION;
        mY = HIDDEN_POSITION;
        mVelocityX = NOT_MOVING;
        mHeight = PROJECTILE_SIZE;
        mWidth = PROJECTILE_SIZE;
    }

    @Override
    public void onCollision(Entity that){
        mWidth += GROWTH_INTERVAL;
        mHeight += GROWTH_INTERVAL;
    }

}
