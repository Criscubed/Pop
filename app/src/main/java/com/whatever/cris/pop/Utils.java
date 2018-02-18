package com.whatever.cris.pop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class Utils {

    public static float wrap(float val, final float min, final float max){
        if(val < min){
            val = max;
        } else if(val > max){
            val = min;
        }
        return val;
    }

    public static float clamp(float val, final float min, final float max){
        if(val < min){
            val = min;
        } else if(val > max){
            val = max;
        }
        return val;
    }

    public static Bitmap flipBitmap(Bitmap source, boolean horizontally){
        Matrix m = new Matrix();
        int cx = source.getWidth()/2;
        int cy = source.getWidth()/2;
        if(horizontally){
            m.postScale(1, -1, cx, cy);
        } else {
            m.postScale(-1, 1, cx, cy);
        }
        return Bitmap.createBitmap(source, 0, 0,
                source.getWidth(), source.getHeight(),
                m, true);
    }
    public static Bitmap scaleToTargetHeight(Bitmap source, int height){
        float ratio = height / (float) source.getHeight();
        int newHeight = (int) (source.getHeight() * ratio);
        int newWidth = (int) (source.getWidth() * ratio);
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

}
