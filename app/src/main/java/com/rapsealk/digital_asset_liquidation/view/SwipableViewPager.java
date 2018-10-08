package com.rapsealk.digital_asset_liquidation.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SwipableViewPager extends ViewPager {

    private boolean enabled = true;

    public SwipableViewPager(Context context) {
        super(context);
    }

    public SwipableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(ev);
        }

        if (MotionEventCompat.getActionMasked(ev) == MotionEvent.ACTION_MOVE) {
            // Ignore
        } else {
            if (super.onInterceptTouchEvent(ev)) {
                super.onTouchEvent(ev);
            }
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.enabled) {
            return super.onTouchEvent(ev);
        }

        return MotionEventCompat.getActionMasked(ev) != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev);
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}