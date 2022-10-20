package com.zhongqijia.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.bean.pay.LogYopCreatAccount;
import com.zhongqijia.pay.bean.pay.LogYopPayCallBack;
import com.zhongqijia.pay.config.BusConfig;
import com.zhongqijia.pay.event.AppEventSender;
import com.zhongqijia.pay.mapper.LogYopCreatAccountMapper;
import com.zhongqijia.pay.mapper.LogYopPayCallBackMapper;
import com.zhongqijia.pay.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: LiaoSheng
 * @Date: 2022/9/21 16:41
 * @Description：
 */
@RestController
@RequestMapping("/yop")
@Slf4j
public class YopController {

    @Autowired
    private AppEventSender appEventSender;

    @Autowired
    private LogYopCreatAccountMapper logYopCreatAccountMapper;
    @Autowired
    private LogYopPayCallBackMapper logYopPayCallBackMapper;

    @PostMapping("/demo/testYopControllerMQ")
    public String testMQ() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("消息", "这是一条yop回调测试消息");
        appEventSender.send(BusConfig.YOP_WALLET_CALLBACK_ROUTING_KEY, jsonObject);
        return "200";
    }

    /**
     *
     * 功能描述: 钱包回调地址
     *
     * @param:
     * @return: 处理结果
     * @auther: xy
     */
    @PostMapping(value = "/walletCallback")
    public String walletCallback(String response) {
        log.info("获取到walletCallback response为{}", response);
        LogYopCreatAccount logYopCreatAccount = JSONObject.parseObject(response, LogYopCreatAccount.class);
        try{
            logYopCreatAccount.setResponse(response);
            logYopCreatAccount.setCreateTime(DateUtils.getCurrentTimeStamp());
            logYopCreatAccountMapper.insert(logYopCreatAccount);
        }catch(Exception e){
            log.info("保存walletCallback response失败:{}", e.getMessage());
        }

        /**
         * do something
         */
        // 收到天河链回调通知需回写大写“SUCCESS”，如没有回写则最多通知 9次，
        // 重试延迟时间分别为：5,5,20,270,600,900,1800,3600,14400（秒），9次后没有拿到回写则停止通知
        appEventSender.send(BusConfig.YOP_WALLET_CALLBACK_ROUTING_KEY, JSONObject.parseObject(response));
        return "SUCCESS";
    }

    /**
     *
     * 功能描述: 支付回调地址
     *
     * @param:
     * @return: 处理结果
     * @auther: xy
     */
    @PostMapping(value = "/payCallback")
    public String payCallback(String response) {
        log.info("获取到response为{}", response);
        LogYopPayCallBack logYopPayCallBack = JSONObject.parseObject(response, LogYopPayCallBack.class);
        try{
            logYopPayCallBack.setResponse(response);
            logYopPayCallBack.setCreateTime(DateUtils.getCurrentTimeStamp());
            logYopPayCallBackMapper.insert(logYopPayCallBack);
        }catch(Exception e){
            log.info("保存payCallback response失败:{}", e.getMessage());
        }

        appEventSender.send(BusConfig.YOP_PAY_CALLBACK_ROUTING_KEY, JSONObject.parseObject(response));
        /**
         * do something
         */
        // 收到天河链回调通知需回写大写“SUCCESS”，如没有回写则最多通知 9次，
        // 重试延迟时间分别为：5,5,20,270,600,900,1800,3600,14400（秒），9次后没有拿到回写则停止通知
        return "SUCCESS";
    }
}
