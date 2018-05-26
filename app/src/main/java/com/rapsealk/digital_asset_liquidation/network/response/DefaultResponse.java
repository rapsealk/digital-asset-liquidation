package com.rapsealk.digital_asset_liquidation.network.response;

/**
 * Created by rapsealk on 2018. 5. 26..
 */
public class DefaultResponse {

    private String message;

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return String.format("{ message: %s }", message);
    }
}
