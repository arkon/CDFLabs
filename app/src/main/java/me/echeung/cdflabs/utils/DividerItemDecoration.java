package me.echeung.cdflabs.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.echeung.cdflabs.R;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int mPaddingLeft;

    public DividerItemDecoration(Context context) {
        this.mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);

        this.mPaddingLeft = 2 * (int) context.getResources().getDimension(R.dimen.regular_margin)
                + (int) context.getResources().getDimension(R.dimen.free_size);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left = parent.getPaddingLeft() + this.mPaddingLeft;
        final int right = parent.getWidth() - parent.getPaddingRight();

        // Avoid last item and footer
        final int childCount = parent.getChildCount() - 2;
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);

                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
