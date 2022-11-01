package com.zhongqijia.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.bean.paysand.C2BLogSandPayCallBack;
import com.zhongqijia.pay.bean.paysand.C2BSandPayCallBack;
import com.zhongqijia.pay.bean.paysand.C2CSandCallBack;
import com.zhongqijia.pay.config.BusConfig;
import com.zhongqijia.pay.event.AppEventSender;
import com.zhongqijia.pay.mapper.LogSandPayCallBackMapper;
import com.zhongqijia.pay.utils.*;
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
     * 功能描述: 支付回调地址C2B
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
                log.info("sand payCallback 验签失败");
            } else {
                log.info("sand payCallback 验签成功");
                C2BSandPayCallBack c2BSandPayCallBack = JSONObject.parseObject(data, C2BSandPayCallBack.class);
                try{
                    C2BLogSandPayCallBack c2BLogSandPayCallBack = c2BSandPayCallBack.getBody();
                    c2BLogSandPayCallBack.setResponse(data);
                    c2BLogSandPayCallBack.setCreateTime(DateUtils.getCurrentTimeStamp());
                    logSandPayCallBackMapper.insert(c2BLogSandPayCallBack);
                }catch(Exception e){
                    log.info("保存sand payCallback response失败:{}", e.getMessage());
                }
                appEventSender.send(BusConfig.SAND_PAY_CALLBACK_ROUTING_KEY, JSONObject.parseObject(data));
            }
            return "SUCCESS";
        }
        return null;
    }

    /**
     * 功能描述: 支付回调地址C2B
     *
     * @param:
     * @return: 处理结果
     * @auther: xy
     */
    @PostMapping(value = "/payCallbackC2C")
    public String payCallbackC2C(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Map<String, String[]> parameterMap = req.getParameterMap();
        if(parameterMap != null && !parameterMap.isEmpty()) {
            String data=req.getParameter("data");
            log.info("data:{}",data);
            String sign=req.getParameter("sign");
            String signType =req.getParameter("signType");
            // 验证签名
            boolean valid;
            //try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data",data);
                jsonObject.put("sign",sign);
                jsonObject.put("signType","SHA1WithRSA");
                //执行verifySign方法
                valid = CryptoUtil.verifySignC2C(jsonObject);
                if (!valid) {//如果验签失败
                    log.error("verify sign fail.");
                    log.error("验签失败的签名字符串(data)为："+ data);
                    log.error("验签失败的签名值(sign)为："+ sign);
                }else {//验签成功
                    log.info("verify sign success");
                    JSONObject dataJson = JSONObject.parseObject(data);
                    if (dataJson != null) {
                        log.info("接收到的异步通知数据为：\n"+ JSONObject.toJSONString(dataJson, true));
                    } else {
                        log.error("接收异步通知数据异常！！！");
                    }

                    try{
                        /*C2CSandCallBack c2CSandCallBack = JSONObject.parseObject(data, C2CSandCallBack.class);
                        c2BLogSandPayCallBack.setResponse(data);
                        c2BLogSandPayCallBack.setCreateTime(DateUtils.getCurrentTimeStamp());
                        logSandPayCallBackMapper.insert(c2BLogSandPayCallBack);*/
                    }catch(Exception e){
                        log.info("保存sand payCallback response失败:{}", e.getMessage());
                    }
                    appEventSender.send(BusConfig.SAND_PAY_CALLBACK_C2C_ROUTING_KEY, dataJson);
                    return "respCode=000000";
                }
            /*} catch (Exception e){
                log.error(e.getMessage());
            }*/
        }
        return null;
    }
}
