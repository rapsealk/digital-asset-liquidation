package com.rapsealk.digital_asset_liquidation.network.body;

import java.util.Locale;

/**
 * Created by rapsealk on 2018. 07. 19..
 */
public class RegisterAssetBody {

    private String address;
    private long assetId;

    public RegisterAssetBody(String address, long assetId) {
        this.address = address;
        this.assetId = assetId;
    }

    public String getAddress() {
        return this.address;
    }

    public RegisterAssetBody setAddress(String address) {
        this.address = address;
        return this;
    }

    public long getAssetId() {
        return this.assetId;
    }

    public RegisterAssetBody setAssetId(long assetId) {
        this.assetId = assetId;
        return this;
    }

    @Override
    public String toString() {
        return String.format(Locale.KOREA, "{ address: %s, assetId: %d }", address, assetId);
    }
}