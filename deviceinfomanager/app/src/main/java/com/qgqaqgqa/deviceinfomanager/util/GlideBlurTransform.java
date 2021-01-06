package com.qgqaqgqa.deviceinfomanager.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class GlideBlurTransform extends BitmapTransformation {
    private static float radius = 0.0f;
    private boolean round = false;

    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        long startMs = System.currentTimeMillis();
        Bitmap overlay = Bitmap.createBitmap((int) (((float) toTransform.getWidth()) / 4.0f), (int) (((float) toTransform.getHeight()) / 4.0f), Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1.0f / 4.0f, 1.0f / 4.0f);
        Paint paint = new Paint();
        paint.setFlags(2);
        canvas.drawBitmap(toTransform, 0.0f, 0.0f, paint);
        return FastBlur.doBlur(overlay, (int) 10.0f, true);
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    }
}