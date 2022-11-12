package com.zhongqijia.pay.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtils {
    private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";
    private static final int KEY_LENGTH = 2048;
    private static final int RESERVE_SIZE = 11;

    public RSAUtils() {
    }

    public static byte[] encrypt(byte[] plainTextBytes, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        int nBlock = plainTextBytes.length / 245;
        if (plainTextBytes.length % 245 != 0) {
            ++nBlock;
        }

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(1, publicKey);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(nBlock * 256);

        for(int offset = 0; offset < plainTextBytes.length; offset += 245) {
            int inputLen = plainTextBytes.length - offset;
            if (inputLen > 245) {
                inputLen = 245;
            }

            byte[] decryptedBlock = cipher.doFinal(plainTextBytes, offset, inputLen);
            byteArrayOutputStream.write(decryptedBlock);
        }

        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] decrypt(byte[] cipherTextBytes, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        int nBlock = cipherTextBytes.length / 256;
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(2, privateKey);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(nBlock * 245);

        for(int offset = 0; offset < cipherTextBytes.length; offset += 256) {
            int inputLen = cipherTextBytes.length - offset;
            if (inputLen > 256) {
                inputLen = 256;
            }

            byte[] decryptedBlock = cipher.doFinal(cipherTextBytes, offset, inputLen);
            byteArrayOutputStream.write(decryptedBlock);
        }

        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}