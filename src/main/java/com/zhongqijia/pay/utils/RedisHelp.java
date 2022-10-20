package com.zhongqijia.pay.utils;

public class RedisHelp {
    public static String USER_KEY = "USER_KEY:";
    public static String ISSUE_KEY = "ISSUE_KEY:";
    public static String ISSUE_WHITE_USER_KEY = "ISSUE_WHITE_USER_KEY:";
    public static String COLLECTION_KEY = "COLLECTION_KEY:";
    public static String MY_ORDER_KEY = "MY_ORDER_KEY:";
    // 用户登录token
    public static String USER_LOGIN_TOKEN_KEY = "USER_LOGIN_TOKEN_KEY:";
    public static String USER_LOGIN_ID_KEY = "USER_LOGIN_ID_KEY:";

    //定时任务redis锁
    //checkorderStatus
    public static String CHECK_ORDER_STATUS_LOCK_KEY = "CHECK_ORDER_STATUS_LOCK_KEY:LOCK_ORDER";
    public static String CHECK_ORDER_STATUS_LOCK_VALUE = "check_order_status_locked";

    // IP请求
    public static final String IP_REQUEST_PREFIX = "app:ip:request:";
    // IP违规次数
    public static final String IP_VIOLATION_TIMES_PREFIX = "app:ip:violation:times:";
    // IP小黑屋判断
    public static final String IP_BLACK_EXIST_PREFIX = "app:ip:black:exist:";
}
