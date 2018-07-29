package com.rapsealk.digital_asset_liquidation.network.response;

import java.util.List;
import java.util.Locale;

public class AssetsResponse extends DefaultResponse {

    private List<Asset> assets;

    public AssetsResponse(boolean succeed, List<Asset> assets) {
        super(succeed);
        this.assets = assets;
    }

    public List<Asset> getAssets() {
        return this.assets;
    }

    /*
    @Override
    public String toString() {
        return String.format("{ succeed: %s, assets: }", isSucceed())
    }
    */
}

class Asset {

    private long id;
    private String owner;
    private int price;
    private int totalShare;
    private int buyableShare;
    private int owningShare;

    public Asset(long id, String owner, int price,
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