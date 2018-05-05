package com.rapsealk.digital_asset_liquidation.schema;

public class AssetCategory {

    public String major;
    public String minor;

    public AssetCategory() {

    }

    public AssetCategory(String major, String minor) {
        this.major = major;
        this.minor = minor;
    }

    public AssetCategory setMajor(String major) {
        this.major = major;
        return this;
    }

    public AssetCategory setMinor(String minor) {
        this.minor = minor;
        return this;
    }
}
