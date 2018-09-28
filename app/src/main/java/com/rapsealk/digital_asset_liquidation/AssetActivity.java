package com.rapsealk.digital_asset_liquidation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rapsealk.digital_asset_liquidation.struct.Asset;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class AssetActivity extends AppCompatActivity implements View.OnClickListener {

    private Asset asset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibBack.setOnClickListener(this);

        TextView tvTitle = (TextView) findViewById(R.id.tv_title);

        ImageView ivAsset = (ImageView) findViewById(R.id.iv_asset);
        TextView tvAssetName = (TextView) findViewById(R.id.tv_asset_name);
        TextView tvAssetPrice = (TextView) findViewById(R.id.tv_asset_price);

        AppCompatButton btnPurchase = (AppCompatButton) findViewById(R.id.btn_purchase);
        btnPurchase.setOnClickListener(this);

        asset = (Asset) getIntent().getExtras().getParcelable("asset");

        Picasso.get().load(asset.imageUrl).fit().centerCrop().into(ivAsset);

        tvTitle.setText(asset.name);

        tvAssetName.setText(asset.name);
        tvAssetPrice.setText(String.format(Locale.KOREA, "%d", asset.price));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_purchase:
                Intent intent = new Intent(this, PurchaseActivity.class)
                        .putExtra("asset", asset);
                startActivity(intent);
                break;
            case R.id.ib_back:
                finish();
                break;
        }
    }
}
