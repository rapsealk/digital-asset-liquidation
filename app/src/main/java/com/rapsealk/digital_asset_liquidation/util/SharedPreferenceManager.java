package com.rapsealk.digital_asset_liquidation.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.rapsealk.digital_asset_liquidation.schema.Account;

import java.util.HashSet;

/**
 * Created by rapsealk on 2018. 5. 29..
 */
public class SharedPreferenceManager {

    private static SharedPreferenceManager sInstance;
    private static SharedPreferences sSharedPreference;

    // private String AUTH_TOKEN = "AUTH_TOKEN";
    private String ETH_ACCOUNT  = "ETH_ACCOUNT";

    private SharedPreferenceManager(Context context) {
        String filename = "DigitalAssetLiquidation";
        sSharedPreference = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    public static SharedPreferenceManager getInstance(Context context) {
        if (sInstance == null) sInstance = new SharedPreferenceManager(context);
        return sInstance;
    }

    /*
    public String getAuthToken() {
        return sSharedPreference.getString(AUTH_TOKEN, null);
    }

    public SharedPreferenceManager setAuthToken(String token) {
        sSharedPreference.edit()
                .putString(AUTH_TOKEN, token)
                .apply();
        return this;
    }
    */

    public SharedPreferenceManager setAccount(Account account) {
        Gson gson = new Gson();
        sSharedPreference.edit()
                .putString(ETH_ACCOUNT, gson.toJson(account))
                .apply();
        return this;
    }

    public Account getAccount() {
        String json = sSharedPreference.getString(ETH_ACCOUNT, null);
        if (json == null) return null;
        Gson gson = new Gson();
        return gson.fromJson(json, Account.class);
    }
}
