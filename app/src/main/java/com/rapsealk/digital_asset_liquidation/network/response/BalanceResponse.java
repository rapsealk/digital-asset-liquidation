package com.rapsealk.digital_asset_liquidation.network.response;

import java.util.Locale;

public class BalanceResponse extends DefaultResponse {

    private int balance;

    public BalanceResponse(boolean succeed, int balance) {
        super(succeed);
        this.balance = balance;
    }

    public int getBalance() {
        return this.balance;
    }

    @Override
    public String toString() {
        return String.format(Locale.KOREA, "{ succeed: %s, balance: %d }", isSucceed(), balance);
    }
}
