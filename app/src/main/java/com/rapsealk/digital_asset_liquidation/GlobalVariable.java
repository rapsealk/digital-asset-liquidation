package com.rapsealk.digital_asset_liquidation;

import android.Manifest;

public class GlobalVariable {

    public static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static final int REQUEST_CODE_ALL        = 0x0001;
    public static final int REQUEST_CODE_CAMERA     = 0x0002;
    public static final int REQUEST_CODE_GALLERY    = 0x0003;
    public static final int REQUEST_CODE_SIGN_UP    = 0x1000;
    public static final int REQUEST_CODE_SIGN_IN    = 0x1001;
    public static final int REQUEST_CODE_UPDATE     = 0x8001;

    public static final int IMAGE_CAPTURE           = 0x2001;
    public static final int READ_STORAGE            = 0x2002;

    public static final String DATABASE_ON_APPRAISE = "onAppraise";
    public static final String DATABASE_ASSET       = "asset";
    public static final String DATABASE_USERS       = "users";

    public static final String API_SERVER_URL       = "http://192.168.0.54:3000/";
    public static final String ETH_SERVER_URL       = "http://192.168.35.9:8545/";  // "http://14.63.193.8:625/";

    public static final String RSA_PUBLIC_KEY       = // "-----BEGIN RSA PUBLIC KEY-----\\n"
                "MIIBCgKCAQEAl7GaxlF3WQhXlC3pRHsUrqjODoiBSA0YxfZPlgDcdJd6in/GhM5XoAyTjbOp" +
                "blwl6VH9MWsz3smhFqCVFn/UA6m4RWmNb2gXj4nUnUS7Z1DG+tdu+MGmHql30QSe5/ikCrF/" +
                "HN4lajDIbqeCK/j3rcXqaOVvGjJyHYVhqThFmvzkehCchRQONAOcnSNox8hUSPfsgf//+3Hh" +
                "ZX7xUmWXbGkELXhS0n0pXIsA2ZUGdwZBnCfitf0eckhNvtN5OimVT/UOJms70+AmLlYs0JXK" +
                "FnVvrfskYOu/Tou0TykS/yl1DBr6f5wB+onJPgFRTfxQHU4teVsjXsRgGZit806LFwIDAQAB";
                // -----END RSA PUBLIC KEY-----\\n";
}
