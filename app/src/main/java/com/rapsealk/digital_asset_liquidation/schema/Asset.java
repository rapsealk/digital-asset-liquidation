package com.rapsealk.digital_asset_liquidation.schema;

import android.os.Parcel;
import android.os.Parcelable;

// TODO("Builder pattern")
public class Asset implements Parcelable {

    public AssetCategory category;

    public String name;
    public String buildDate;
    public int price;
    public boolean isOnChain;
    public String owner;
    public long timestamp;
    public String imageUrl;

    public long orderKey;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Asset>() {
        @Override
        public Asset createFromParcel(Parcel source) {
            return new Asset(source);
        }

        @Override
        public Asset[] newArray(int size) {
            return new Asset[size];
        }
    };

    public Asset() {

    }

    public Asset(String owner, long timestamp, String imageUrl) {
        this.owner = owner;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.orderKey = -1 * timestamp;
    }

    public Asset(Parcel parcel) {
        category = parcel.readParcelable(AssetCategory.class.getClassLoader());
        name = parcel.readString();
        buildDate = parcel.readString();
        price = parcel.readInt();
        isOnChain = (parcel.readByte() == 0);
        owner = parcel.readString();
        timestamp = parcel.readLong();
        imageUrl = parcel.readString();
        orderKey = parcel.readLong();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(category, flags);
        dest.writeString(name);
        dest.writeString(buildDate);
        dest.writeInt(price);
        dest.writeByte((byte) (isOnChain ? 1 : 0));
        dest.writeString(owner);
        dest.writeLong(timestamp);
        dest.writeString(imageUrl);
        dest.writeLong(orderKey);
    }
}
