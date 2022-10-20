package com.zhongqijia.pay.bean.pay;

import lombok.Data;

/**
 * {"merchantUserNo":"122751","businessNo":"fbbe5effd63b4e35a40a51bd3a4d8786","walletCategory":"TWO_CATEGORY","walletUserNo":"211431665814","requestNo":"2022101199","merchantNo":"10088821904"}
 */
@Data
public class OpenWalletEmpty {
    private String businessNo;
    private String merchantNo;
    private String merchantUserNo;
    private String walletCategory;
    private String walletUserNo;
}