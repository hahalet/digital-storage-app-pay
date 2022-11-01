package com.zhongqijia.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.bean.paysand.LogSandPayCallBack;
import com.zhongqijia.pay.bean.paysand.SandPayCallBack;
import com.zhongqijia.pay.bean.payyop.LogYopPayCallBack;
import com.zhongqijia.pay.config.BusConfig;
import com.zhongqijia.pay.event.AppEventSender;
import com.zhongqijia.pay.mapper.LogSandPayCallBackMapper;
import com.zhongqijia.pay.utils.DateUtils;
import com.zhongqijia.pay.utils.SandCertUtil;
import com.zhongqijia.pay.utils.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private LogSandPayCallBackMapper logSandPayCallBackMapper;

    /**
     * 功能描述: 钱包回调地址
     *
     * @param:
     * @return: 处理结果
     * @auther: xy
     */
    @PostMapping(value = "/walletCallback")
    public String walletCallback(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String[]> parameterMap = req.getParameterMap();
        log.info("获取到sand walletCallback response为{}", JSON.toJSONString(parameterMap));

        /**
         * do something
         */
        // 收到天河链回调通知需回写大写“SUCCESS”，如没有回写则最多通知 9次，
        // 重试延迟时间分别为：5,5,20,270,600,900,1800,3600,14400（秒），9次后没有拿到回写则停止通知
        //appEventSender.send(BusConfig.SAND_WALLET_CALLBACK_ROUTING_KEY, JSONObject.parseObject(response));
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
        Map<String, String[]> parameterMap = req.getParameterMap();
        log.info("获取到sand payCallback response为{}", JSON.toJSONString(parameterMap));
        if (parameterMap != null && !parameterMap.isEmpty()) {
            String data = req.getParameter("data");
            String sign = req.getParameter("sign");
            String signType = req.getParameter("signType");
            log.info("data:{}", data);
            log.info("sign:{}", sign);
            boolean valid = CryptoUtil.verifyDigitalSign(data.getBytes(encoding), Base64.decodeBase64(sign),
                    SandCertUtil.getPublicKey(), "SHA1WithRSA");
            if (!valid) {
                System.out.println("sand payCallback 验签失败");
            } else {
                System.out.println("sand payCallback 验签成功");
                SandPayCallBack sandPayCallBack = JSONObject.parseObject(data, SandPayCallBack.class);
                try{
                    LogSandPayCallBack logSandPayCallBack = sandPayCallBack.getBody();
                    logSandPayCallBack.setResponse(data);
                    logSandPayCallBack.setCreateTime(DateUtils.getCurrentTimeStamp());
                    logSandPayCallBackMapper.insert(logSandPayCallBack);
                }catch(Exception e){
                    log.info("保存sand payCallback response失败:{}", e.getMessage());
                }
                appEventSender.send(BusConfig.SAND_PAY_CALLBACK_ROUTING_KEY, JSONObject.parseObject(data));
            }
            return "SUCCESS";
        }
        return null;
    }
}
