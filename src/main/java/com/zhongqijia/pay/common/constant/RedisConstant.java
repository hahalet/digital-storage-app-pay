package com.zhongqijia.pay.common.constant;

/**
 * @author 陌上丶天琊
 * 描述： redis相关常量
 */
public final class RedisConstant {
    
    public static long ONE_DAY_SECOND = 86400L;
    public static long THREE_DAY_SECOND =259200L;
    public static long SEVEN_DAY_SECOND =604800L;
    public static long THIRTY_DAY_SECOND =2592000L;

    public static final String UPDATE_AUDIT_LOCK_KEY = "update:audit:lock:key:";

    public static final String ROBOT_INFO_AUDIT_NOTIFY_LOCK_KEY = "capricorn:robot:info:audit:notify:lock:key:";
}
