package com.rapsealk.digital_asset_liquidation.network.body;

import java.util.Locale;

/**
 * Created by rapsealk on 2018. 09. 28..
 */
public class TradeAssetBody {

    private String address;
    private String owner;
    private long id;
    private int amount;

    public TradeAssetBody(String address, String owner, long id, int amount) {
        this.address = address;
        this.owner = owner;
        this.id = id;
        this.amount = amount;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format(Locale.KOREA, "{ address: %s, owner: %s, id: %d, amount: %d }", address, owner, id, amount);
    }
}
