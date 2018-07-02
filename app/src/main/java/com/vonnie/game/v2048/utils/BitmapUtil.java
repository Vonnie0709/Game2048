package com.vonnie.game.v2048.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 * @author LongpingZou
 * @date 2018/7/2
 */
public class BitmapUtil {
    public static Bitmap loadBitmapFromView(View view) {
        if (view == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas();
        c.setBitmap(screenshot);
        view.draw(c);
        return screenshot;
    }
}
