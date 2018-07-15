package com.rapsealk.digital_asset_liquidation.schema;

/**
 * Created by rapsealk on 2018. 7. 15..
 */
public class Account {

    private String address;
    private String privateKey;

    public Account(String address, String privateKey) {
        this.address = address;
        this.privateKey = privateKey;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }
}