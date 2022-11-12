package com.zhongqijia.pay.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    public AESUtils() {
    }

    public static byte[] encrypt(byte[] plainBytes, byte[] keyBytes, String IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        if (StringUtils.isNotBlank(IV)) {
            IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
            cipher.init(1, secretKey, ips);
        } else {
            cipher.init(1, secretKey);
        }

        return cipher.doFinal(plainBytes);
    }

    public static byte[] decrypt(byte[] encryptedBytes, byte[] keyBytes, String IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        if (StringUtils.isNotBlank(IV)) {
            IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
            cipher.init(2, secretKey, ips);
        } else {
            cipher.init(2, secretKey);
        }

        return cipher.doFinal(encryptedBytes);
    }
}