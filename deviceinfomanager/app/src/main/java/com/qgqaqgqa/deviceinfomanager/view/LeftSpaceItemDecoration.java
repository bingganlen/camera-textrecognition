package com.qgqaqgqa.deviceinfomanager.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.qgqaqgqa.deviceinfomanager.R;

/**
 * Created by fengjh on 16/7/31.
 */
public class LeftSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int leftSpace;
    private int space;
    private int dividerHeight;
    private Paint dividerPaint;
    public LeftSpaceItemDecoration(Context context, int space) {
        this(context,space,0);
    }

    public LeftSpaceItemDecoration(Context context, int space, int leftSpace) {
        this.space=space;
        dividerPaint = new Paint();
        dividerPaint.setColor(context.getResources().getColor(R.color.colorLine));
//        dividerPaint.setColor(context.getResources().getColor(R.color.colorDanmu));
        dividerHeight = space;
        this.leftSpace=leftSpace;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left+leftSpace, top, right, bottom, dividerPaint);
        }
    }
}