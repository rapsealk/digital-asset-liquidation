package com.rapsealk.digital_asset_liquidation.network.response;

import com.rapsealk.digital_asset_liquidation.struct.EthAsset;

import java.util.List;

public class AssetsResponse extends DefaultResponse {

    private List<EthAsset> assets;

    public AssetsResponse(boolean succeed, List<EthAsset> assets) {
        super(succeed);
        this.assets = assets;
    }

    public List<EthAsset> getAssets() {
        return this.assets;
    }

    /*
    @Override
    public String toString() {
        return String.format("{ succeed: %s, assets: }", isSucceed())
    }
    */
}