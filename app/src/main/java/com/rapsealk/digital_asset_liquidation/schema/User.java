package com.rapsealk.digital_asset_liquidation.schema;

/**
 * Created by rapsealk on 2018. 5. 10..
 */
public class User {

    private String uid;
    // private String public_key;
    private String name;
    private String birthdate;
    private String address;

    public User(String uid, String name, String birthdate) {
        this.uid = uid;
        this.name = name;
        this.birthdate = birthdate;
        this.address = "";
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

    /*
    public String getPublicKey() {
        return this.public_key;
    }

    public User setPublicKey(String publicKey) {
        this.public_key = publicKey;
        return this;
    }
    */

    /*
    public void copy(User user) {
        this.uid = user.uid;
        this.public_key = user.public_key;
    }
    */
}
