package com.zhongqijia.pay.utils;

import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.bean.MyOrder;
import com.zhongqijia.pay.common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    public static String CHECK_ORDER_C2C_STATUS_LOCK_KEY = "CHECK_ORDER_C2C_STATUS_LOCK_KEY:LOCK_ORDER";
    public static String CHECK_ORDER_C2C_STATUS_LOCK_VALUE = "check_order_c2c_status_locked";

    // IP请求
    public static final String IP_REQUEST_PREFIX = "app:ip:request:";
    // IP违规次数
    public static final String IP_VIOLATION_TIMES_PREFIX = "app:ip:violation:times:";
    // IP小黑屋判断
    public static final String IP_BLACK_EXIST_PREFIX = "app:ip:black:exist:";

    /**
     * 获取缓存
     * @param redisUtil
     * @param userId
     * @param collid
     * @return
     */
    public static List<MyOrder> getMyOrder(RedisUtil redisUtil, Integer userId, Integer collid){
        String myOrdersRedisStr = (String)redisUtil.get(RedisHelp.MY_ORDER_KEY + userId + "_" + collid);
        List<MyOrder> myOrdersRedis = null;
        try {
            myOrdersRedis = JSONObject.parseArray(myOrdersRedisStr, MyOrder.class);
        } catch (Exception e) {
        }
        return myOrdersRedis==null?new ArrayList<>():myOrdersRedis;
    }

    /**
     * 刷新缓存
     * @param myOrder
     * @param redisUtil
     */
    public static void refreshMyOrder(MyOrder myOrder, RedisUtil redisUtil){
        String myOrdersRedisStr = (String)redisUtil.get(RedisHelp.MY_ORDER_KEY + myOrder.getUserid() + "_" + myOrder.getCollid());
        log.info("myOrdersRedisStr:{}",myOrdersRedisStr);
        List<MyOrder> myOrdersRedis = null;
        try {
            myOrdersRedis = JSONObject.parseArray(myOrdersRedisStr, MyOrder.class);
        } catch (Exception e) {
        }
        if (myOrdersRedis == null) {
            myOrdersRedis = new ArrayList<>();
        }
        myOrdersRedis.remove(myOrder);
        myOrdersRedis.add(myOrder);
        redisUtil.set(RedisHelp.MY_ORDER_KEY + myOrder.getUserid() + "_" + myOrder.getCollid(), JSONObject.toJSONString(myOrdersRedis));
    }
}
