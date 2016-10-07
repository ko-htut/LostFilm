package com.alexandr.lostfilm.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by alexandr on 07/09/16.
 */
public class ConvertDensity {
    public static int convertPixelsToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static int convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
