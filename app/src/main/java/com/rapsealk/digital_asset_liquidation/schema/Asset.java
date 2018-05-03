package com.rapsealk.digital_asset_liquidation.schema;

// TODO("Builder pattern")
public class Asset {

    public String majorCategory;
    public String minorCategory;

    public String name;
    public String buildDate;
    public int marketPrice;
    public boolean isOnChain;
    public String owner;
    public long timestamp;
    public String imageUrl;

    public Asset(String majorCategory, String minorCategory,
                 String name, String buildDate, int marketPrice, boolean isOnChain,
                 String owner, long timestamp, String imageUrl) {
        this.majorCategory = majorCategory;
        this.minorCategory = minorCategory;
        this.name = name;
        this.buildDate = buildDate;
        this.marketPrice = marketPrice;
        this.isOnChain = isOnChain;
        this.owner = owner;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
    }

    public Asset setMajorCategory(String category) {
        this.majorCategory = category;
        return this;
    }
}
