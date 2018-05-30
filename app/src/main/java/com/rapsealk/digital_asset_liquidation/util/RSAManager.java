package com.rapsealk.digital_asset_liquidation.util;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.rapsealk.digital_asset_liquidation.GlobalVariable;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by rapsealk on 2018. 5. 29..
 */
public class RSAManager {

    public static String encrypt(@NonNull String message) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decode(GlobalVariable.RSA_PUBLIC_KEY, Base64.DEFAULT));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicKey = keyFactory.generatePublic(publicKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return new String(encryptedBytes);
    }
}
