package com.rapsealk.digital_asset_liquidation.schema;

import android.os.Parcel;
import android.os.Parcelable;

public class AssetCategory implements Parcelable {

    public String major;
    public String minor;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<AssetCategory>() {
        @Override
        public AssetCategory createFromParcel(Parcel source) {
            return new AssetCategory(source);
        }

        @Override
        public AssetCategory[] newArray(int size) {
            return new AssetCategory[size];
        }
    };

    public AssetCategory() {

    }

    public AssetCategory(String major, String minor) {
        this.major = major;
        this.minor = minor;
    }

    public AssetCategory(Parcel parcel) {
        this.major = parcel.readString();
        this.minor = parcel.readString();
    }

    public AssetCategory setMajor(String major) {
        this.major = major;
        return this;
    }

    public AssetCategory setMinor(String minor) {
        this.minor = minor;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(major);
        dest.writeString(minor);
    }
}
