package com.rapsealk.digital_asset_liquidation.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

/**
 * Created by rapsealk on 2018. 5. 10..
 */
public class IndeterminateProgressBar extends ProgressBar {

    public IndeterminateProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setIndeterminate(true);
    }

    /*
    public void setVisibility(int visibility, Window window) {
        switch (visibility) {
            case ProgressBar.VISIBLE: {
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                break;
            }
            case ProgressBar.GONE: {
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
        super.setVisibility(visibility);
    }
    */
}
