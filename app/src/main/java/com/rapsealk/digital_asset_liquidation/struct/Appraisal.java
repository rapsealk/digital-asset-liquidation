package com.rapsealk.digital_asset_liquidation.struct;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * Created by rapsealk on 2018. 07. 24..
 */
public class Appraisal implements Parcelable {

    private String appraiser;
    private int value;
    private long timestamp;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Appraisal>() {
        @Override
        public Appraisal createFromParcel(Parcel source) {
            return new Appraisal(source);
        }

        @Override
        public Appraisal[] newArray(int size) {
            return new Appraisal[size];
        }
    };

    public Appraisal() {

    }

    public Appraisal(String appraiser, int value, long timestamp) {
        this.appraiser = appraiser;
        this.value = value;
        this.timestamp = timestamp;
    }

    public Appraisal(Parcel parcel) {
        this.appraiser = parcel.readString();
        this.value = parcel.readInt();
        this.timestamp = parcel.readLong();
    }

    public String getAppraiser() {
        return this.appraiser;
    }

    public Appraisal setAppraiser(String appraiser) {
        this.appraiser = appraiser;
        return this;
    }

    public int getValue() {
        return this.value;
    }

    public Appraisal setValue(int value) {
        this.value = value;
        return this;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public Appraisal setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @Override
    public String toString() {
        return String.format(Locale.KOREA, "{ appraiser: %s, value: %d, timestamp: %d }", appraiser, value, timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appraiser);
        dest.writeInt(this.value);
        dest.writeLong(this.timestamp);
    }
}