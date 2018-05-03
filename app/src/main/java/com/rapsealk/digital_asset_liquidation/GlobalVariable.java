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

    public static final int IMAGE_CAPTURE           = 0x0001;
    public static final int READ_STORAGE            = 0x0002;
}