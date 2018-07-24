package com.rapsealk.digital_asset_liquidation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rapsealk.digital_asset_liquidation.AssetActivity;
import com.rapsealk.digital_asset_liquidation.R;
import com.rapsealk.digital_asset_liquidation.struct.Asset;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private Context mContext;

    private List<CardView> mViews;
    private List<Asset> mItems;
    private float mBaseElevation;

    public CardPagerAdapter(Context context) {
        mContext = context;
        mViews = new ArrayList<>();
        mItems = new ArrayList<>();
    }

    public void addItem(Asset item) {
        mViews.add(null);
        mItems.add(item);
    }

    public void addItems(List<Asset> items) {
        for (Asset item: items) addItem(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_asset_preview, container, false);
        container.addView(view);
        bind(mItems.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardview);
        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);

        // TODO("replace to onItemClickListener")
        view.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AssetActivity.class)
                    .putExtra("asset", mItems.get(position));
            mContext.startActivity(intent);
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(Asset item, View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_asset);
        TextView titleTextView = (TextView) view.findViewById(R.id.tv_asset_name);
        Picasso.get()
                .load(item.imageUrl)
                .placeholder(R.color.cardview_dark_background)
                .fit()
                .centerCrop()
                .into(imageView);
        titleTextView.setText(item.name);
    }
}

