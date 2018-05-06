package com.rapsealk.digital_asset_liquidation.schema;

// TODO("Builder pattern")
public class Asset {

    public AssetCategory category;

    public String name;
    public String buildDate;
    public int price;
    public boolean isOnChain;
    public String owner;
    public long timestamp;
    public String imageUrl;

    public long orderKey;

    public Asset() {

    }

    public Asset(String owner, long timestamp, String imageUrl) {
        this.owner = owner;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.orderKey = -1 * timestamp;
    }

    public Asset setCategory(AssetCategory category) {
        this.category = category;
        return this;
    }

    public Asset setName(String name) {
        this.name = name;
        return this;
    }

    public Asset setBuildDate(String buildDate) {
        this.buildDate = buildDate;
        return this;
    }

    public Asset setPrice(int price) {
        this.price = price;
        return this;
    }

    public Asset setOnChain(boolean isOnChain) {
        this.isOnChain = isOnChain;
        return this;
    }

    /*
    public Asset setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public Asset setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Asset setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
    */
}
