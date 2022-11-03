package com.zhongqijia.pay.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Objects;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.zhongqijia.pay.common.enums.SandEnum;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CeasHttpUtil {
    public static final Logger logger = LoggerFactory.getLogger(CeasHttpUtil.class);
    public static final String MID_KEY = "mid";
    public static final String DATA = "data";
    public static final String CUSTOMER_ORDER_NO = "customerOrderNo";
    public static final String ENCRYPTKEY_KEY = "encryptKey";
    public static final String ENCRYPTTYPE_KEY = "encryptType";
    public static final String ENCRYPTTYPE = "AES";
    public static final String SIGN_KEY = "sign";
    public static final String SIGNTYPE_KEY = "signType";
    public static final String SIGNTYPE = "SHA1WithRSA";
    public static final String RESPONSE_CODE = "responseCode";
    public static final String RESPONSE_DESC = "responseDesc";
    public static final String RESPONSE_STATUS = "responseStatus";
    public static final String RESPONSE_SUCCESS = "00000BCEAS";

    public CeasHttpUtil() {
    }

    public static JSONObject doPost(JSONObject paramsJson, SandEnum sandEnum) {
        try {
            String customerOrderNo = null;
            if (paramsJson.containsKey("customerOrderNo")) {
                customerOrderNo = paramsJson.getString("customerOrderNo");
            }

            encrypt(paramsJson);
            paramsJson.put("mid", ConfigLoader.getConfig().getMid());
            paramsJson.put("customerOrderNo", customerOrderNo);
            sign(paramsJson);
            String method = sandEnum.getBusinessType() + "/" + sandEnum.getMethodCode();
            logger.info("请求方法：{}", method);
            logger.info("请求报文：{}", JSON.toJSONString(paramsJson));
            String url = ConfigLoader.getConfig().getUrl() + "/" + ConfigLoader.getConfig().getVersion() + "/" + method;
            String resp = HttpClientUtils.sendPost(url, paramsJson.toJSONString(), ConfigLoader.getConfig().getConnectTimeout(), ConfigLoader.getConfig().getReadTimeout());
            JSONObject dataJson = JSON.parseObject(resp);
            logger.info("响应报文：{}", JSON.toJSONString(dataJson));
            if (!Objects.isNull(dataJson) && dataJson.containsKey("data")) {
                if (!verifySign(dataJson)) {
                    logger.error("签名验证失败");
                    return null;
                } else {
                    dataJson = decrypt(dataJson);
                    dataJson.remove("responseStatus");
                    return dataJson;
                }
            } else {
                return dataJson;
            }
        } catch (Exception var7) {
            logger.error("服务器请求异常", var7);
            return null;
        }
    }

    private static void encrypt(JSONObject paramsJson) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, IOException {
        String aesKey = RandomUtils.genRandomStringByLength(16);
        byte[] aesKeyBytes = aesKey.getBytes(StandardCharsets.UTF_8);
        logger.info("生成加密随机数：{}", aesKey);
        String plainValue = paramsJson.toJSONString();
        logger.info("AES加密前值：{}", plainValue);
        byte[] encryptValueBytes = AESUtils.encrypt(plainValue.getBytes(StandardCharsets.UTF_8), aesKeyBytes, (String)null);
        String encryptValue = new String(Base64.encodeBase64(encryptValueBytes));
        logger.info("AES加密后值：{}", encryptValue);
        paramsJson.clear();
        paramsJson.put("data", encryptValue);
        byte[] encryptKeyBytes = RSAUtils.encrypt(aesKeyBytes, CertCache.getCertCache().getPublicKey());
        String sandEncryptKey = new String(Base64.encodeBase64(encryptKeyBytes));
        paramsJson.put("encryptKey", sandEncryptKey);
        paramsJson.put("encryptType", "AES");
    }

    private static void sign(JSONObject paramsJson) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String plainText = paramsJson.getString("data");
        logger.info("待签名报文：{}", plainText);
        paramsJson.put("sign", SignatureUtils.sign(plainText, "SHA1WithRSA", CertCache.getCertCache().getPrivateKey()));
        paramsJson.put("signType", "SHA1WithRSA");
    }

    private static boolean verifySign(JSONObject dataJson) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String sign = dataJson.getString("sign");
        String signType = dataJson.getString("signType");
        dataJson.remove("sign");
        dataJson.remove("signType");
        String plainText = dataJson.getString("data");
        return SignatureUtils.verify(plainText, sign, signType, CertCache.getCertCache().getPublicKey());
    }

    private static JSONObject decrypt(JSONObject dataJson) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        String decryptKey = dataJson.getString("encryptKey");
        dataJson.remove("encryptKey");
        byte[] decryptKeyBytes = Base64.decodeBase64(decryptKey);
        decryptKey = new String(RSAUtils.decrypt(decryptKeyBytes, CertCache.getCertCache().getPrivateKey()));
        logger.info("RSA解密后随机数：{}", decryptKey);
        String encryptValue = dataJson.getString("data");
        logger.info("AES解密前值：{}", encryptValue);
        byte[] decryptDataBase64 = Base64.decodeBase64(encryptValue);
        byte[] decryptDataBytes = AESUtils.decrypt(decryptDataBase64, decryptKey.getBytes(StandardCharsets.UTF_8), (String)null);
        String decryptData = new String(decryptDataBytes);
        logger.info("AES解密后值：{}", decryptData);
        return JSON.parseObject(decryptData);
    }
}