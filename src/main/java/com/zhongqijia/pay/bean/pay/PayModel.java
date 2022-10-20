package com.zhongqijia.pay.bean.pay;

public class PayModel {
    private String businessInfo;
    private String csUrl;
    private String expiredTime;
    private String fundProcessType;
    private String goodsName;
    private String memo;
    private String merchantNo;
    private String notifyUrl;
    private String orderAmount;
    private String orderId;
    private String redirectUrl;
    private String walletMemberNo;

    /**
     * 自定义参数信息，自定义参数信息
     */
    public String getBusinessInfo() { return businessInfo; }
    public void setBusinessInfo(String value) { this.businessInfo = value; }

    /**
     * 清算回调地址，清算回调地址 清算成功服务器回调地址，不传则不通知
     */
    public String getcsUrl() { return csUrl; }
    public void setcsUrl(String value) { this.csUrl = value; }

    /**
     * 订单过期时间，订单过期时间，格式为yyyy-MM-dd HH:mm:ss，为空时默认在请求下单120分钟后失效，最长支持30天
     */
    public String getExpiredTime() { return expiredTime; }
    public void setExpiredTime(String value) { this.expiredTime = value; }

    /**
     * 分账标识，分账标识。不传，默认不分账 DELAY_SETTLE：分账 合单收款场景中，请在子单域信息subOrderDetail里提供
     */
    public String getFundProcessType() { return fundProcessType; }
    public void setFundProcessType(String value) { this.fundProcessType = value; }

    /**
     * 商品名称，商品名称
     * 简单描述订单信息或商品简介，用于展示在收银台页面或者支付明细中。单笔收款必传，合单收款场景中请在子单域信息subOrderDetail里提供(*当商品名称超过85个字符时，易宝会默认保留前85个字符)
     */
    public String getGoodsName() { return goodsName; }
    public void setGoodsName(String value) { this.goodsName = value; }

    /**
     * 对账备注，对账备注。商户自定义参数，会展示在交易对账单中,支持85个字符（中文或者英文字母）
     */
    public String getMemo() { return memo; }
    public void setMemo(String value) { this.memo = value; }

    /**
     * 商户编号，商户编号 收款商户商编。单笔收款必传，合单收款场景中请在子单域信息subOrderDetail里提供
     */
    public String getMerchantNo() { return merchantNo; }
    public void setMerchantNo(String value) { this.merchantNo = value; }

    /**
     * 接收支付结果的通知地址，接收支付结果的通知地址,
     */
    public String getNotifyUrl() { return notifyUrl; }
    public void setNotifyUrl(String value) { this.notifyUrl = value; }

    /**
     * 订单金额，订单金额。单位为元，精确到小数点后两位
     */
    public String getOrderAmount() { return orderAmount; }
    public void setOrderAmount(String value) { this.orderAmount = value; }

    /**
     * 商户收款请求号，商户收款请求号。可包含字母、数字、下划线；需保证在商户端不重复。合单收款场景中，此参数为合单收款请求号
     */
    public String getOrderId() { return orderId; }
    public void setOrderId(String value) { this.orderId = value; }

    /**
     * 支付成功后跳转的URL，redirectUrl 支付成功后跳转的URL，如商户指定页面回调地址，
     * 支付完成后会从易宝的支付成功页跳转至商家指定页面，只有走标准收银台的订单此地址才有作用。注意：最大长度200个字符。
     */
    public String getRedirectUrl() { return redirectUrl; }
    public void setRedirectUrl(String value) { this.redirectUrl = value; }

    /**
     * 钱包会员标识，钱包会员标识,若支付方式（directPayType）使用钱包支付，则该参数必填
     */
    public String getWalletMemberNo() { return walletMemberNo; }
    public void setWalletMemberNo(String value) { this.walletMemberNo = value; }
}
