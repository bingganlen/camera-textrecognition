package com.qgqaqgqa.deviceinfomanager.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class GlideRoundTransform extends BitmapTransformation {
    private static float radius = 0.0f;
    private boolean round = false;

    public GlideRoundTransform(int dp) {
        if (dp == -1) {
            this.round = true;
            return;
        }
        this.round = false;
        radius = Resources.getSystem().getDisplayMetrics().density * ((float) dp);
    }

    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        if (this.round) {
            return circleCrop(pool, toTransform);
        }
        return roundCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        int size = Math.min(source.getWidth(), source.getHeight());
        Bitmap squared = Bitmap.createBitmap(source, (source.getWidth() - size) / 2, (source.getHeight() - size) / 2, size, size);
        Bitmap result = pool.get(size, size, Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, TileMode.CLAMP, TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = ((float) size) / 2.0f;
        canvas.drawCircle(r, r, r, paint);
        return result;
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, TileMode.CLAMP, TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0.0f, 0.0f, (float) source.getWidth(), (float) source.getHeight());
        float f = radius;
        canvas.drawRoundRect(rectF, f, f, paint);
        return result;
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
    }
}