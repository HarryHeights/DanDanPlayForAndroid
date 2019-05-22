package com.xyoye.dandanplay2.ui.weight;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2015/12/29.
 * 空白的分割线
 */
public class ItemDecorationSpaces extends RecyclerView.ItemDecoration {

    private int top, left, right, bottom;
    private int spanCount;

    public ItemDecorationSpaces(int space) {
        this(space, space, space, space);
    }

    public ItemDecorationSpaces(int top, int left, int right, int bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.spanCount = 0;
    }

    public ItemDecorationSpaces(int top, int left, int right, int bottom, int spanCount) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.top = top;
        outRect.left = left;
        outRect.bottom = bottom;
        if (spanCount != 0) {
            int position = parent.getChildLayoutPosition(view);
            if ((position + 1) % spanCount == 0) {
                outRect.right = 0;
            } else {
                outRect.right = right;
            }
        } else {
            outRect.right = right;
        }
    }
}
