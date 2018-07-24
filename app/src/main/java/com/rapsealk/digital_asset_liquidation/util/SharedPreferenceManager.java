package com.rapsealk.digital_asset_liquidation.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.rapsealk.digital_asset_liquidation.struct.User;

/**
 * Created by rapsealk on 2018. 5. 29..
 */
public class SharedPreferenceManager {

    private static SharedPreferenceManager sInstance;
    private static SharedPreferences sSharedPreference;

    private Gson gson;

    private String USER = "USER";
    // private String AUTH_TOKEN = "AUTH_TOKEN";
    // private String ETH_ACCOUNT  = "ETH_ACCOUNT";

    private SharedPreferenceManager(Context context) {
        String filename = "DigitalAssetLiquidation";
        sSharedPreference = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static SharedPreferenceManager getInstance(Context context) {
        if (sInstance == null) sInstance = new SharedPreferenceManager(context);
        return sInstance;
    }

    public SharedPreferenceManager setUser(User user) {
        sSharedPreference.edit()
                .putString(USER, gson.toJson(user))
                .apply();
        return this;
    }

    public User getUser() {
        String json = sSharedPreference.getString(USER, null);
        if (json == null) return null;
        return gson.fromJson(json, User.class);
    }

    /*
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
    */
}