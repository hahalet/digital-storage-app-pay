package com.zhongqijia.pay.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLoader {
    public static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    public static final String FILE_NAME = "sandpay_ceas.properties";
    private String url;
    private String version;
    private String mid;
    private String merchantCertPath;
    private String merchantCertPwd;
    private String sandCertPath;
    private int connectTimeout = 30000;
    private int readTimeout = 30000;
    public static final String SDK_URL = "sand.ceas.sdk.api.url";
    public static final String VERSION = "sand.ceas.sdk.api.version";
    public static final String SDK_MID = "sand.ceas.sdk.mid";
    public static final String SDK_MERCHANT_CERT_PATH = "sand.ceas.sdk.merchant.cert.path";
    public static final String SDK_MERCHANT_CERT_PWD = "sand.ceas.sdk.merchant.cert.pwd";
    public static final String SDK_SNAD_CERT_PATH = "sand.ceas.sdk.sand.cert.path";
    public static final String SDK_URL_CONNECT_TIMEOUT = "sand.ceas.sdk.url.connect.timeout";
    public static final String SDK_URL_READ_TIMEOUT = "sand.ceas.sdk.url.read.timeout";
    private static ConfigLoader config = new ConfigLoader();
    private Properties properties;

    private ConfigLoader() {
        this.loadPropertiesFromSrc();
    }

    public static ConfigLoader getConfig() {
        return config;
    }

    public void loadPropertiesFromSrc() {
        InputStream in = null;

        try {
            logger.info("从classpath: {} 获取属性文件 {}", ConfigLoader.class.getClassLoader().getResource("").getPath(), "sandpay_ceas.properties");
            in = ConfigLoader.class.getClassLoader().getResourceAsStream("sandpay_ceas.properties");
            if (null != in) {
                this.properties = new Properties();

                try {
                    this.properties.load(in);
                } catch (IOException var13) {
                    throw var13;
                }

                this.loadProperties(this.properties);
                return;
            }

            logger.error("{}属性文件未能在classpath指定的目录下 {} 找到!", "sandpay_ceas.properties", ConfigLoader.class.getClassLoader().getResource("").getPath());
        } catch (IOException var14) {
            logger.error(var14.getMessage(), var14);
            return;
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException var12) {
                    logger.error(var12.getMessage(), var12);
                }
            }

        }

    }

    public void loadProperties(Properties pro) {
        logger.info("开始从属性文件中加载配置项");
        String value = null;
        value = pro.getProperty("sand.ceas.sdk.api.url");
        if (!StringUtils.isEmpty(value)) {
            this.url = value.trim();
            logger.info("配置项：通讯地址==>{}==>{} 已加载", "sand.ceas.sdk.api.url", value);
        }

        value = pro.getProperty("sand.ceas.sdk.api.version");
        if (!StringUtils.isEmpty(value)) {
            this.version = value.trim();
            logger.info("配置项：版本==>{}==>{} 已加载", "sand.ceas.sdk.api.version", value);
        }

        value = pro.getProperty("sand.ceas.sdk.mid");
        if (!StringUtils.isEmpty(value)) {
            this.mid = value.trim();
            logger.info("配置项：商户号==>{}==>{}已加载", "sand.ceas.sdk.mid", value);
        }

        value = pro.getProperty("sand.ceas.sdk.merchant.cert.path");
        if (!StringUtils.isEmpty(value)) {
            this.merchantCertPath = value.trim();
            logger.info("配置项：商户私钥证书路径==>{}==>{}已加载", "sand.ceas.sdk.merchant.cert.path", value);
        }

        value = pro.getProperty("sand.ceas.sdk.merchant.cert.pwd");
        if (!StringUtils.isEmpty(value)) {
            this.merchantCertPwd = value.trim();
            logger.info("配置项：商户私钥证书密码==>{}==>{}已加载", "sand.ceas.sdk.merchant.cert.pwd", value);
        }

        value = pro.getProperty("sand.ceas.sdk.sand.cert.path");
        if (!StringUtils.isEmpty(value)) {
            this.sandCertPath = value.trim();
            logger.info("配置项：杉德公钥证书路径==>{}==>{}已加载", "sand.ceas.sdk.sand.cert.path", value);
        }

        value = pro.getProperty("sand.ceas.sdk.url.connect.timeout");
        if (!StringUtils.isEmpty(value)) {
            this.connectTimeout = Integer.parseInt(value);
            logger.info("配置项：http连接超时时间==>{}==>{}已加载", "sand.ceas.sdk.url.connect.timeout", value);
        }

        value = pro.getProperty("sand.ceas.sdk.url.read.timeout");
        if (!StringUtils.isEmpty(value)) {
            this.readTimeout = Integer.parseInt(value);
            logger.info("配置项：http响应超时时间==>{}==>{}已加载", "sand.ceas.sdk.url.read.timeout", value);
        }

    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMid() {
        return this.mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMerchantCertPath() {
        return this.merchantCertPath;
    }

    public void setMerchantCertPath(String merchantCertPath) {
        this.merchantCertPath = merchantCertPath;
    }

    public String getMerchantCertPwd() {
        return this.merchantCertPwd;
    }

    public void setMerchantCertPwd(String merchantCertPwd) {
        this.merchantCertPwd = merchantCertPwd;
    }

    public String getSandCertPath() {
        return this.sandCertPath;
    }

    public void setSandCertPath(String sandCertPath) {
        this.sandCertPath = sandCertPath;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
