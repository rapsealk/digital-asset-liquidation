package com.rapsealk.digital_asset_liquidation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rapsealk.digital_asset_liquidation.network.body.TradeAssetBody;
import com.rapsealk.digital_asset_liquidation.struct.Asset;

public class PurchaseActivity extends AppCompatActivity implements View.OnClickListener {

    private Asset mAsset;

    private TextView mTvAssetName;
    private TextView mTvPrice;
    private EditText mEtAmount;
    private Button mBtnPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        mAsset = (Asset) getIntent().getParcelableExtra("asset");

        mTvAssetName = (TextView) findViewById(R.id.tv_asset_name);
        mTvAssetName.setText(mAsset.name);

        //mTvPrice = (TextView) findViewById(R.id.tv_price);
        //mTvPrice.setText(mAsset.price);

        mEtAmount = (EditText) findViewById(R.id.et_amount);

        mBtnPurchase = (Button) findViewById(R.id.btn_purchase);
        mBtnPurchase.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_purchase:
                int amount = Integer.parseInt(mEtAmount.getText().toString());
                long totalPrice = mAsset.price * amount;
                Toast.makeText(this, "Total price: " + totalPrice, Toast.LENGTH_SHORT).show();

                // TradeAssetBody body = new TradeAssetBody()
                break;
        }
    }
}
