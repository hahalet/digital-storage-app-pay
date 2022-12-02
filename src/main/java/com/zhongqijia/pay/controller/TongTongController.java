package com.zhongqijia.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.bean.payyop.LogYopCreatAccount;
import com.zhongqijia.pay.bean.payyop.LogYopPayCallBack;
import com.zhongqijia.pay.config.BusConfig;
import com.zhongqijia.pay.event.AppEventSender;
import com.zhongqijia.pay.mapper.LogYopCreatAccountMapper;
import com.zhongqijia.pay.mapper.LogYopPayCallBackMapper;
import com.zhongqijia.pay.service.PayTongTongService;
import com.zhongqijia.pay.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: LiaoSheng
 * @Date: 2022/9/21 16:41
 * @Description：
 */
@RestController
@RequestMapping("/tongtong")
@Slf4j
public class TongTongController {

    @Autowired
    private AppEventSender appEventSender;

    @Autowired
    private PayTongTongService payTongTongService;
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
        log.info("获取到tongtong walletCallback response为{}", response);
        return "000000";
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

        return "000000";
    }

    @GetMapping(value = "/walletLoginByUsers")
    public String walletLoginByUsers(Integer userId){
        return payTongTongService.walletLoginByUsers(userId);
    }
}