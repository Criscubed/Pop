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
    public static final String TAG = "Enemy";
    public static final float ENEMY_MAX_SPEED = 3;
    Bitmap mBitmap;

    public Enemy(Context context){
        super();
        int select = mDice.nextInt(3);
        int resourceId = R.drawable.skull1;
        switch(select){
            case 0:
                resourceId =  R.drawable.skull1;
                break;
            case 1:
                resourceId = R.drawable.skull2;
                break;
            case 2:
                resourceId = R.drawable.skull3;
                break;
            default:
                Log.w(TAG, "You messed up your EnemyID" + select);
                break;
        }
        mBitmap = decodeSampledBitmapFromResource(context.getResources(), resourceId,50,50);
        mHeight = mBitmap.getHeight();
        mWidth = mBitmap.getWidth();
        mY = mDice.nextInt(Game.STAGE_HEIGHT - (int) mHeight);
        mX = Game.STAGE_WIDTH + mDice.nextInt((int)mWidth);
        mVelocityX = -1 + -mDice.nextInt((int)ENEMY_MAX_SPEED);



    }


    @Override
    public void render(final Canvas canvas,final Paint paint) {
        canvas.drawBitmap(mBitmap, mX, mY, paint);
    }

    //The following two methods were borrowed from
    //https://developer.android.com/topic/performance/graphics/load-bitmap.html

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
