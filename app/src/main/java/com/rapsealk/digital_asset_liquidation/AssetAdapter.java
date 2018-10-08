package com.rapsealk.digital_asset_liquidation;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rapsealk.digital_asset_liquidation.struct.Asset;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rapsealk on 2018. 5. 5..
 */
public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Asset> mItems;

    public AssetAdapter(Context context, ArrayList<Asset> items) {
        this.mContext = context;
        this.mItems = items;
    }
;
    public void addItem(Asset item) {
        this.mItems.add(item);
    }

    public void clearItems() {
        this.mItems.clear();
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asset, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Asset item = mItems.get(position);
        holder.assetName.setText(item.name);
        holder.assetPrice.setText(String.valueOf(item.price));
        Picasso.get().load(item.imageUrl).placeholder(R.color.cardview_dark_background).into(holder.assetImage);
        holder.assetCategoryMajor.setText(item.category.major);
        holder.assetCategoryMinor.setText(item.category.minor);
        Drawable onChainStatus = mContext.getResources().getDrawable(R.drawable.item_circle, mContext.getApplicationContext().getTheme());
        int colorId = (item.isOnChain) ? R.color.green : R.color.red;
        int colorFilter = ContextCompat.getColor(mContext, colorId);
        onChainStatus.mutate().setColorFilter(colorFilter, PorterDuff.Mode.MULTIPLY);
        holder.assetOnChain.setImageDrawable(onChainStatus);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AssetActivity.class)
                        .putExtra("asset", item);
                mContext.startActivity(intent);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView assetImage;
        TextView assetName;
        TextView assetPrice;
        TextView assetCategoryMajor;
        TextView assetCategoryMinor;
        ImageView assetOnChain;

        public ViewHolder(View view) {
            super(view);
            assetImage = (ImageView) view.findViewById(R.id.iv_asset);
            assetName = (TextView) view.findViewById(R.id.tv_asset_name);
            assetPrice = (TextView) view.findViewById(R.id.tv_asset_price);
            assetCategoryMajor = (TextView) view.findViewById(R.id.tv_asset_category_major);
            assetCategoryMinor = (TextView) view.findViewById(R.id.tv_asset_category_minor);
            assetOnChain = (ImageView) view.findViewById(R.id.iv_asset_on_chain);
        }
    }
}
