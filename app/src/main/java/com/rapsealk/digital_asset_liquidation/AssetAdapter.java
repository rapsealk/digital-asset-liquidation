package com.rapsealk.digital_asset_liquidation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rapsealk.digital_asset_liquidation.schema.Asset;
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

    public void addItem(Asset item) {
        this.mItems.add(item);
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
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView assetImage;
        TextView assetName;
        TextView assetPrice;

        public ViewHolder(View view) {
            super(view);
            assetImage = (ImageView) view.findViewById(R.id.iv_asset);
            assetName = (TextView) view.findViewById(R.id.tv_asset_name);
            assetPrice = (TextView) view.findViewById(R.id.tv_asset_price);
        }
    }
}
