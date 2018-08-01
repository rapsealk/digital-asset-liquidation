package com.rapsealk.digital_asset_liquidation.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by rapsealk on 2018. 8. 1..
 */
public class CircularViewPager extends ViewPager {

    private int mCurrentPosition = 1;

    public CircularViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularViewPager(Context context) {
        this(context, null);
    }

    @Override
    public void setCurrentItem(int position) {
        
    }
}