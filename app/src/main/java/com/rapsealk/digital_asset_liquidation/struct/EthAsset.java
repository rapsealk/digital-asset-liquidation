package com.rapsealk.digital_asset_liquidation.struct;

import java.util.Locale;

public class EthAsset {

    private long id;
    private String owner;
    private int price;
    private int totalShare;
    private int buyableShare;
    private int owningShare;

    public EthAsset(long id, String owner, int price,
                 int totalShare, int buyableShare, int owningShare) {
        this.id = id;
        this.owner = owner;
        this.price = price;
        this.totalShare = totalShare;
        this.buyableShare = buyableShare;
        this.owningShare = owningShare;
    }

    public long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public int getPrice() {
        return price;
    }

    public int getTotalShare() {
        return totalShare;
    }

    public int getBuyableShare() {
        return buyableShare;
    }

    public int getOwningShare() {
        return owningShare;
    }

    @Override
    public String toString() {
        return String.format(Locale.KOREA, "{ id: %d, owner: %s, price: %d, " +
                        "totalShare: %d, buyableShare: %d, owningShare: %d }",
                id, owner, price, totalShare, buyableShare, owningShare);
    }
}