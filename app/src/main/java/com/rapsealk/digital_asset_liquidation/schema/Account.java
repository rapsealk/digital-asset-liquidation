package com.rapsealk.digital_asset_liquidation.schema;

/**
 * Created by rapsealk on 2018. 7. 15..
 */
public class Account {

    private String address;
    private String privateKey;
    private int balance;

    public Account(String address, String privateKey, int balance) {
        this.address = address;
        this.privateKey = privateKey;
        this.balance = balance;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public int getBalance() {
        return this.balance;
    }

    public Account setBalance(int balance) {
        this.balance = balance;
        return this;
    }
}