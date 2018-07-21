package com.rapsealk.digital_asset_liquidation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rapsealk.digital_asset_liquidation.schema.Asset;
import com.squareup.picasso.Picasso;

public class AssetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);

        ImageView ivAsset = (ImageView) findViewById(R.id.iv_asset);
        TextView tvAssetName = (TextView) findViewById(R.id.tv_asset_name);

        Asset asset = (Asset) getIntent().getExtras().getParcelable("asset");

        Picasso.get().load(asset.imageUrl).fit().centerCrop().into(ivAsset);

        tvAssetName.setText(asset.name);

        ibBack.setOnClickListener(view -> finish());
    }
}
