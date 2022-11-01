package com.zhongqijia.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.config.BusConfig;
import com.zhongqijia.pay.event.AppEventSender;
import com.zhongqijia.pay.utils.CertUtil;
import com.zhongqijia.pay.utils.CryptoUtil;
import com.zhongqijia.pay.utils.SDKConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;
import java.util.Map;

/**
 * @Author: LiaoSheng
 * @Date: 2022/9/21 16:41
 * @Description：
 */
@RestController
@RequestMapping("/sand")
@Slf4j
public class SandController {

    @Autowired
    private AppEventSender appEventSender;

    /**
     * 功能描述: 钱包回调地址
     *
     * @param:
     * @return: 处理结果
     * @auther: xy
     */
    @PostMapping(value = "/walletCallback")
    public String walletCallback(String response) {
        log.info("获取到sand walletCallback response为{}", response);


        /**
         * do something
         */
        // 收到天河链回调通知需回写大写“SUCCESS”，如没有回写则最多通知 9次，
        // 重试延迟时间分别为：5,5,20,270,600,900,1800,3600,14400（秒），9次后没有拿到回写则停止通知
        appEventSender.send(BusConfig.SAND_WALLET_CALLBACK_ROUTING_KEY, JSONObject.parseObject(response));
        return "SUCCESS";
    }
    // 默认配置的是UTF-8
    public static String encoding = "UTF-8";
    /**
     * 功能描述: 支付回调地址
     *
     * @param:
     * @return: 处理结果
     * @auther: xy
     */
    @PostMapping(value = "/payCallback")
    public String payCallback(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        log.info("payCallback 开始");
        Map<String, String[]> parameterMap = req.getParameterMap();
        log.info("获取到sand response为{}", JSON.toJSONString(parameterMap));
        if (parameterMap != null && !parameterMap.isEmpty()) {
            String data = req.getParameter("data");
            String sign = req.getParameter("sign");
            String signType = req.getParameter("signType");
            log.info("data:{}", data);
            log.info("sign:{}", sign);
            boolean valid = CryptoUtil.verifyDigitalSign(data.getBytes(encoding), Base64.decodeBase64(sign),
                    CertUtil.getPublicKey(), "SHA1WithRSA");
            if (!valid) {
                System.out.println("验签失败");
            } else {
                System.out.println("验签成功");
            }
            return "SUCCESS";
        }
        return null;
    }
}
