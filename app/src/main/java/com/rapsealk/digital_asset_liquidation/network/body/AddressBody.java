package com.rapsealk.digital_asset_liquidation.network.body;

public class AddressBody {

    private String address;

    public AddressBody(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        return String.format("{ address: %s }", address);
    }
}
