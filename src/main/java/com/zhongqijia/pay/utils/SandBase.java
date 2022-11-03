package com.zhongqijia.pay.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * @ClassName : DemoBase
 * @Description :
 * @Author : Wcl
 * @DateTime : 2022/8/29 11:55
 * @Since JDK1.8.251
 **/
public class SandBase {

    /**
     * 生成商户订单号
     * @return
     */
    public static String getCustomerOrderNo() {
        Random rand = new Random();
        int num=rand.nextInt(100)+1;
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+num;
    }

    //请求时间 格式:YYYYMMDDhhmmss
    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    //获取当前时间24小时后的时间
    public static String getNextDayTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return new SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime());
    }

    public static void main(String[] args) {
        System.out.println(getCustomerOrderNo());
    }
}
