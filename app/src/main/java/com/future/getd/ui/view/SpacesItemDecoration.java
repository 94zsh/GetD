package com.future.getd.ui.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        outRect.top = space;
//            // Add top space for first item to avoid double space between items
//            if (parent.getChildLayoutPosition(view) == 0) {
//                outRect.top = space;
//            } else {
//                outRect.top = 0;
//            }
    }
}
