package com.rapsealk.digital_asset_liquidation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.rapsealk.digital_asset_liquidation.schema.Asset;
import com.squareup.picasso.Picasso;

public class AssetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        Asset asset = (Asset) getIntent().getExtras().getParcelable("asset");

        ImageView ivAsset = (ImageView) findViewById(R.id.iv_asset);
        TextView tvAssetName = (TextView) findViewById(R.id.tv_asset_name);

        Picasso.get().load(asset.imageUrl).fit().centerCrop().into(ivAsset);

        tvAssetName.setText(asset.name);
    }
}
