package com.rapsealk.digital_asset_liquidation.network.response;

/**
 * Created by rapsealk on 2018. 5. 26..
 */
public class DefaultResponse {

    private boolean succeed;

    public DefaultResponse(boolean succeed) {
        this.succeed = succeed;
    }

    public boolean isSucceed() {
        return this.succeed;
    }

    @Override
    public String toString() {
        return String.format("{ succeed: %s }", succeed);
    }
}
