package com.zhongqijia.pay.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CertCache {
    private static final Logger logger = LoggerFactory.getLogger(CertCache.class);
    public static final String PUBLIC_KEY = "public_key";
    public static final String PRIVATE_KEY = "private_key";
    private static final ConcurrentHashMap<String, Object> keys = new ConcurrentHashMap();
    private static CertCache certCache = new CertCache();

    private CertCache() {
        this.init();
    }

    public static CertCache getCertCache() {
        return certCache;
    }

    public void init() {
        this.initPulbicKey(ConfigLoader.getConfig().getSandCertPath());
        this.initPrivateKey(ConfigLoader.getConfig().getMerchantCertPath(), ConfigLoader.getConfig().getMerchantCertPwd());
    }

    public PublicKey getPublicKey() {
        return (PublicKey)keys.get("public_key");
    }

    public PrivateKey getPrivateKey() {
        return (PrivateKey)keys.get("private_key");
    }

    private void initPulbicKey(String publicKeyPath) {
        String classpathKey = "classpath:";
        if (publicKeyPath != null) {
            try {
                InputStream inputStream = null;
                if (publicKeyPath.startsWith(classpathKey)) {
                    inputStream = CertCache.class.getClassLoader().getResourceAsStream(publicKeyPath.substring(classpathKey.length()));
                } else {
                    inputStream = new FileInputStream(publicKeyPath);
                }

                PublicKey publicKey = this.getPublicKey((InputStream)inputStream);
                keys.put("public_key", publicKey);
            } catch (CertificateException | FileNotFoundException var5) {
                logger.error("无法加载公钥[{}]", new Object[]{publicKeyPath});
                logger.error(var5.getMessage(), var5);
            }
        }

    }

    private void initPrivateKey(String privateKeyPath, String keyPassword) {
        String classpathKey = "classpath:";

        try {
            InputStream inputStream = null;
            if (privateKeyPath.startsWith(classpathKey)) {
                inputStream = CertCache.class.getClassLoader().getResourceAsStream(privateKeyPath.substring(classpathKey.length()));
            } else {
                inputStream = new FileInputStream(privateKeyPath);
            }

            PrivateKey privateKey = this.getPrivateKey((InputStream)inputStream, keyPassword);
            keys.put("private_key", privateKey);
        } catch (NoSuchAlgorithmException | IOException | CertificateException var6) {
            logger.error("无法加载本地私钥[{}]", new Object[]{privateKeyPath});
            logger.error(var6.getMessage(), var6);
        }

    }

    private PublicKey getPublicKey(InputStream inputStream) throws CertificateException {
        PublicKey var5;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate oCert = (X509Certificate)cf.generateCertificate(inputStream);
            PublicKey publicKey = oCert.getPublicKey();
            var5 = publicKey;
        } catch (CertificateException var14) {
            throw new CertificateException("读取公钥异常");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException var13) {
                logger.error(var13.getMessage(), var13);
            }

        }

        return var5;
    }

    private PrivateKey getPrivateKey(InputStream inputStream, String password) throws IOException, NoSuchAlgorithmException, CertificateException {
        PrivateKey var7;
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            char[] nPassword = null;
            if (password != null && !password.trim().equals("")) {
                nPassword = password.toCharArray();
            } else {
                nPassword = null;
            }

            ks.load(inputStream, nPassword);
            Enumeration<String> enumas = ks.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()) {
                keyAlias = (String)enumas.nextElement();
            }

            var7 = (PrivateKey)ks.getKey(keyAlias, nPassword);
        } catch (FileNotFoundException var19) {
            throw new FileNotFoundException("私钥路径文件不存在");
        } catch (IOException var20) {
            throw new IOException(var20);
        } catch (NoSuchAlgorithmException var21) {
            throw new NoSuchAlgorithmException("生成私钥对象异常");
        } catch (UnrecoverableKeyException | KeyStoreException | CertificateException var22) {
            throw new CertificateException("生成私钥对象异常");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException var18) {
                logger.error(var18.getMessage(), var18);
            }

        }

        return var7;
    }
}
