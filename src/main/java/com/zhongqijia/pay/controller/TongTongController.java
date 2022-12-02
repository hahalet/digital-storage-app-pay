package com.zhongqijia.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.event.AppEventSender;
import com.zhongqijia.pay.service.PayTongTongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
    public String walletCallback(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String[]> parameterMap = req.getParameterMap();
        log.info("获取到walletCallback response为{}", JSON.toJSONString(parameterMap));
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
    public String payCallback(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String[]> parameterMap = req.getParameterMap();
        log.info("获取到payCallback response为{}", JSON.toJSONString(parameterMap));
        return "000000";
    }

    @GetMapping(value = "/payOrderFirst")
    public String payOrderFirst(Integer userId,Integer orderId,String payerClientIp) {
        return payTongTongService.payOrderFirst(userId,orderId,payerClientIp);
    }

    @GetMapping(value = "/walletLoginByUsers")
    public String walletLoginByUsers(Integer userId){
        return payTongTongService.walletLoginByUsers(userId);
    }

    @GetMapping(value = "/getWalletInfo")
    public JSONObject getWalletInfo(@RequestParam Integer userId) throws Exception {
        return payTongTongService.walletIsOpen(userId);
    }
}
