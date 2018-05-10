package com.rapsealk.digital_asset_liquidation.schema;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by rapsealk on 2018. 5. 10..
 */
public class User extends RealmObject {

    private String uid;
    private String public_key;

    @Ignore
    private int sessionId;

    public User() {
        this.uid = "";
        this.public_key = "";
    }

    public User(String uid, String publicKey) {
        this.uid = uid;
        this.public_key = publicKey;
    }

    public String getUid() {
        return this.uid;
    }

    public User setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getPublicKey() {
        return this.public_key;
    }

    public User setPublicKey(String publicKey) {
        this.public_key = publicKey;
        return this;
    }
}
