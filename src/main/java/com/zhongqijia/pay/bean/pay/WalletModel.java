package com.zhongqijia.pay.bean.pay;

public class WalletModel {
    private String certificateNo;
    private String certificateType;
    private String merchantNo;
    private String merchantUserNo;
    private String mobile;
    private String name;
    private String notifyUrl;
    private String requestNo;
    private String returnUrl;
    private String userMerchantNo;

    /**
     * 证件号码，证件号码（证件号码和手机号二选一必填）
     */
    public String getCertificateNo() { return certificateNo; }
    public void setCertificateNo(String value) { this.certificateNo = value; }

    /**
     * 证件类型，证件类型 (certificateNo有值时，该字段必填) 可选项如下: IDENTITY_CARD:身份证
     */
    public String getCertificateType() { return certificateType; }
    public void setCertificateType(String value) { this.certificateType = value; }

    /**
     * 商户编号， 易宝支付分配的的商户唯一标识
     */
    public String getMerchantNo() { return merchantNo; }
    public void setMerchantNo(String value) { this.merchantNo = value; }

    /**
     * 商户用户ID，用户在商户侧的用户id
     */
    public String getMerchantUserNo() { return merchantUserNo; }
    public void setMerchantUserNo(String value) { this.merchantUserNo = value; }

    /**
     * 手机号，手机号（证件号码和手机号二选一必填）
     */
    public String getMobile() { return mobile; }
    public void setMobile(String value) { this.mobile = value; }

    /**
     * 姓名
     */
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    /**
     * 服务器回调地址，服务器回调地址 开户完成后通知地址
     */
    public String getNotifyUrl() { return notifyUrl; }
    public void setNotifyUrl(String value) { this.notifyUrl = value; }

    /**
     * 商户请求流水号，商户请求流水号
     */
    public String getRequestNo() { return requestNo; }
    public void setRequestNo(String value) { this.requestNo = value; }

    /**
     * 页面重定向地址，页面重定向地址 钱包首页返回商户页面地址
     */
    public String getReturnUrl() { return returnUrl; }
    public void setReturnUrl(String value) { this.returnUrl = value; }

    /**
     * 易宝商户编号，用户在易宝的商户编号（存量用户迁移场景下）
     */
    public String getUserMerchantNo() { return userMerchantNo; }
    public void setUserMerchantNo(String value) { this.userMerchantNo = value; }
}
