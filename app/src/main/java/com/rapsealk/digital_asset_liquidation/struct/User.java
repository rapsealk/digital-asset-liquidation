package com.rapsealk.digital_asset_liquidation.struct;

import java.util.Locale;

/**
 * Created by rapsealk on 2018. 5. 10..
 */
public class User {

    private String uid;
    // private String public_key;
    private String name;
    private String birthdate;
    private String address;
    private boolean admin;

    public User() {
        this.uid = "no-uid";
        this.name = "no-name";
        this.birthdate = "1970-01-01";
        this.address = "null@null.null";
        this.admin = false;
    }

    public User(String uid, String name, String birthdate, String address, boolean admin) {
        this.uid = uid;
        this.name = name;
        this.birthdate = birthdate;
        this.address = address;
        this.admin = admin;
    }

    public String getUid() {
        return this.uid;
    }

    public User setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public User SetName(String name) {
        this.name = name;
        return this;
    }

    public String getBirthdate() {
        return this.birthdate;
    }

    public User setBirthdate(String birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public String getAddress() {
        return this.address;
    }

    public User setAddress(String address) {
        this.address = address;
        return this;
    }

    public boolean isAdmin() {
        return this.admin;
    }

    public User isAdmin(boolean admin) {
        this.admin = admin;
        return this;
    }

    @Override
    public String toString() {
        return String.format(Locale.KOREA, "{ uid: %s, name: %s, birthdate: %s, address: %s, admin: %s }",
                getUid(), getName(), getBirthdate(), getAddress(), isAdmin());
    }
}
