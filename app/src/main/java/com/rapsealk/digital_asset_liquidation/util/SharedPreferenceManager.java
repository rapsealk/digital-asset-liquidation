package com.rapsealk.digital_asset_liquidation.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rapsealk on 2018. 5. 29..
 */
public class SharedPreferenceManager {

    private static SharedPreferenceManager sInstance;
    private static SharedPreferences sSharedPreference;

    private String AUTH_TOKEN = "AUTH_TOKEN";

    private SharedPreferenceManager(Context context) {
        String filename = "DigitalAssetLiquidation";
        sSharedPreference = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    public static SharedPreferenceManager getInstance(Context context) {
        if (sInstance == null) sInstance = new SharedPreferenceManager(context);
        return sInstance;
    }

    public String getAuthToken() {
        return sSharedPreference.getString(AUTH_TOKEN, null);
    }

    public SharedPreferenceManager setAuthToken(String token) {
        sSharedPreference.edit()
                .putString(AUTH_TOKEN, token)
                .apply();
        return this;
    }
}
