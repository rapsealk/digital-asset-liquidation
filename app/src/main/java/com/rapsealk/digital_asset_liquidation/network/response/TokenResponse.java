package com.rapsealk.digital_asset_liquidation.network.response;

/**
 * Created by rapsealk on 2018. 5. 31..
 */
public class TokenResponse extends DefaultResponse {

    private String token;

    public String getToken() {
        return this.token;
    }

    @Override
    public String toString() {
        return String.format("{ succeed: %s, token: %s }", isSucceed(), token);
    }
}
