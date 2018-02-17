package com.whatever.cris.pop;

public class Utils {

    public static float wrap(float val, final float min, final float max){
        if(val < min){
            val = max;
        } else if(val > max){
            val = min;
        }
        return val;
    }

}
