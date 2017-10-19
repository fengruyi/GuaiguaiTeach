package com.guaiwa.guaiguaiteach.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.guaiwa.guaiguaiteach.MyApplication;
import com.guaiwa.guaiguaiteach.common.utils.PhoneInfo;


/**
 * Created by 80151689 on 2016-09-06.
 * recycleview横向间隔
 */
public class HorientalItemDecoration extends RecyclerView.ItemDecoration {
    private int innerSpace;//item间的间隔
    private int edgePadding;//第一个item和最后一个item与recycleview边距

    public HorientalItemDecoration(int space) {
        this(space, space);
    }

    public HorientalItemDecoration(int space, int padding) {
        innerSpace = PhoneInfo.dip2px(MyApplication.context, space);
        edgePadding = PhoneInfo.dip2px(MyApplication.context, padding);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        if (pos == 0) {
            outRect.left = edgePadding;
        } else {
            outRect.left = innerSpace;
        }
        outRect.top = 0;
        outRect.bottom = 0;
        if (pos == (parent.getAdapter().getItemCount() - 1)) {
            outRect.right = edgePadding;
        } else {
            outRect.right = 0;
        }
    }
}
