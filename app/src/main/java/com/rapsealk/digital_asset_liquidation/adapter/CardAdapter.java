package com.rapsealk.digital_asset_liquidation.adapter;

import android.support.v7.widget.CardView;

public interface CardAdapter {

    static int MAX_ELEVATION_FACTOR = 8;

    abstract float getBaseElevation();

    abstract CardView getCardViewAt(int position);

    abstract int getCount();
}
