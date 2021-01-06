package com.qgqaqgqa.deviceinfomanager.intf;

import android.graphics.drawable.Drawable;

public interface LoadImageListener {
    void onLoadingFailed(Exception exception, Drawable drawable);

    void onLoadingStart();
}