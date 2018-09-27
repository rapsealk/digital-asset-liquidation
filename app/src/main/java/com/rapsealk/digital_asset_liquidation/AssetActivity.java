package com.rapsealk.digital_asset_liquidation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rapsealk.digital_asset_liquidation.struct.Asset;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class AssetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);

        ImageView ivAsset = (ImageView) findViewById(R.id.iv_asset);
        TextView tvAssetName = (TextView) findViewById(R.id.tv_asset_name);
        TextView tvAssetPrice = (TextView) findViewById(R.id.tv_asset_price);

        Asset asset = (Asset) getIntent().getExtras().getParcelable("asset");

        Picasso.get().load(asset.imageUrl).fit().centerCrop().into(ivAsset);

        tvTitle.setText(asset.name);

        tvAssetName.setText(asset.name);
        tvAssetPrice.setText(String.format(Locale.KOREA, "%d", asset.price));

        ibBack.setOnClickListener(view -> finish());
    }
}
