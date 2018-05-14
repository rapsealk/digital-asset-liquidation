package com.rapsealk.digital_asset_liquidation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;

/**
 * Created by rapsealk on 2018. 5. 15..
 */
public class RealmAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(getApplicationContext());
    }
}
