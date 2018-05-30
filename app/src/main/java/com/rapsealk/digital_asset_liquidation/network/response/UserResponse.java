package com.rapsealk.digital_asset_liquidation.network.response;

/**
 * Created by rapsealk on 2018. 5. 31..
 */
public class UserResponse extends DefaultResponse {

    private String id;

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return String.format("{ succeed: %s, id: %s }", isSucceed(), id);
    }
}
