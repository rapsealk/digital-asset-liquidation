package com.rapsealk.digital_asset_liquidation.network.response;

public class AccountResponse extends DefaultResponse {

    private String address;

    public AccountResponse(boolean succeed, String address) {
        super(succeed);
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        return String.format("{ succeed: %s, address: %s }", isSucceed(), address);
    }
}
