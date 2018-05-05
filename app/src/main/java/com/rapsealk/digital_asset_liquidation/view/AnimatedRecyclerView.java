package com.rapsealk.digital_asset_liquidation.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rapsealk on 2018. 5. 5..
 */
public class AnimatedRecyclerView extends RecyclerView {

    private Context mContext;
    private boolean mScrollable;

    public AnimatedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        mContext = context;
        mScrollable = true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return !mScrollable || super.dispatchTouchEvent(ev);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < getChildCount(); i++) {
            animate(getChildAt(i), i);
        }
        getHandler().postDelayed(() -> {
            mScrollable = true;
        }, (getChildCount() - 1) * 100);
    }

    private void animate(View view, int position) {
        view.animate().cancel();
        view.setTranslationY(100f);
        view.setAlpha(0f);
        view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .setStartDelay(position * 100);
    }
}

