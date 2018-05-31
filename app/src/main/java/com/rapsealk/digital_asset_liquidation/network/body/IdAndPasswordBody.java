package com.rapsealk.digital_asset_liquidation.network.body;

/**
 * Created by rapsealk on 2018. 5. 31..
 */
public class IdAndPasswordBody {

    private String id;
    private String password;

    public IdAndPasswordBody(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return this.id;
    }

    public String getPassword() {
        return this.password;
    }
}
