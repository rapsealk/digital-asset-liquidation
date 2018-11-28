package com.rapsealk.digital_asset_liquidation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rapsealk.digital_asset_liquidation.R;
import com.rapsealk.digital_asset_liquidation.struct.EthAsset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rapsealk on 2018. 11. 28..
 */
public class EthAssetAdapter extends RecyclerView.Adapter<EthAssetAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<EthAsset> mItems;

    public EthAssetAdapter(Context context, ArrayList<EthAsset> items) {
        this.mContext = context;
        this.mItems = items;
    }

    public void addItem(EthAsset item) {
        this.mItems.add(item);
    }

    public void addAll(List<EthAsset> items) {
        this.mItems.addAll(items);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eth_asset, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EthAsset item = mItems.get(position);
        holder.assetId.setText(String.valueOf(item.getId()));
        holder.assetOwner.setText(item.getOwner());
        holder.assetPrice.setText(String.valueOf(item.getPrice()));
        holder.assetTotal.setText(String.valueOf(item.getTotalShare()));
        holder.assetOwning.setText(String.valueOf(item.getOwningShare()));
        holder.assetBuyable.setText(String.valueOf(item.getBuyableShare()));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView assetId;
        TextView assetOwner;
        TextView assetPrice;
        TextView assetTotal;
        TextView assetOwning;
        TextView assetBuyable;

        public ViewHolder(View view) {
            super(view);
            assetId = view.findViewById(R.id.tv_asset_id);
            assetOwner = view.findViewById(R.id.tv_asset_owner);
            assetPrice = view.findViewById(R.id.tv_asset_price);
            assetTotal = view.findViewById(R.id.tv_asset_total);
            assetOwning = view.findViewById(R.id.tv_asset_owning);
            assetBuyable = view.findViewById(R.id.tv_asset_buyable);
        }
    }
}
