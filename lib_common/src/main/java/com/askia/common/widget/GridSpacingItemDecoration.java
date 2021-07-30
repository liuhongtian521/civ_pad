package com.askia.common.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int verticalSpace;
    private int horizontalSpace;

    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int verticalSpace,int horizontalSpace, boolean includeEdge) {
        this.spanCount = spanCount;
        this.verticalSpace = verticalSpace;
        this.horizontalSpace = horizontalSpace;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = horizontalSpace - column * horizontalSpace / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * horizontalSpace / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = verticalSpace;
            }
            outRect.bottom = verticalSpace; // item bottom
        } else {
            outRect.left = column * horizontalSpace / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = horizontalSpace - (column + 1) * horizontalSpace / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = verticalSpace; // item top
            }
        }
    }
}
