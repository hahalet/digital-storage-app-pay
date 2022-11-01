package com.zhongqijia.pay.bean.paysand;

import lombok.Data;

@Data
public class C2CSandCallBack
{
    private double amount;

    private double feeAmt;

    private String mid;

    private String orderNo;

    private String orderStatus;

    private C2CPayeeInfo payeeInfo;

    private C2CPayerInfo payerInfo;

    private String respCode;

    private String respMsg;

    private String respTime;

    private String sandSerialNo;

    private String transType;

    private double userFeeAmt;
}
