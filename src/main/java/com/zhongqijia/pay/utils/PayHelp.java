package com.zhongqijia.pay.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class PayHelp {
    //一手藏品
    public static String FIRST_ORDER = "FIT";
    //二手藏品
    public static String SECOND_ORDER = "SCD";

    //二手藏品
    public static BigDecimal SECOND_ORDER_PERCENT = new BigDecimal(0.955);
}
