package com.whatever.cris.pop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Cris on 3/23/2018.
 */

public class BitmapEntity extends Entity {

    public static final int HEIGHT = 50;
    public Bitmap mBitmap;
    public float mPlayerSpeed;

    public BitmapEntity(){
        super();
    }

    public void setBitmap(Context context, int resourceId){
        Bitmap temp = BitmapFactory.decodeResource(context.getResources(), resourceId);
        mBitmap = Utils.scaleToTargetHeight(temp , HEIGHT);
        mBitmap = Utils.flipBitmap(mBitmap, false);
        if(temp != mBitmap){
            temp.recycle();
        }
    }

    @Override
    public void render(final Canvas canvas, final Paint paint) {
        canvas.drawBitmap(mBitmap, mX, mY, paint);
    }

}
