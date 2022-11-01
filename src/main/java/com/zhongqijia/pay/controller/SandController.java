package com.zhongqijia.pay.controller;

import cn.com.sand.ceas.sdk.config.ConfigLoader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.config.BusConfig;
import com.zhongqijia.pay.event.AppEventSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
     * 加载配置文件
     */
    public static void loadCertFromSrc() {
        ConfigLoader.getConfig().loadPropertiesFromSrc();
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
        log.info("获取到sand walletCallback response为{}", response);


        /**
         * do something
         */
        // 收到天河链回调通知需回写大写“SUCCESS”，如没有回写则最多通知 9次，
        // 重试延迟时间分别为：5,5,20,270,600,900,1800,3600,14400（秒），9次后没有拿到回写则停止通知
        appEventSender.send(BusConfig.SAND_WALLET_CALLBACK_ROUTING_KEY, JSONObject.parseObject(response));
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
    public String payCallback(HttpServletRequest req, HttpServletResponse resp) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        log.info("获取到sand response为{}", JSON.toJSONString(parameterMap));
        if(parameterMap != null && !parameterMap.isEmpty()) {
            String data=req.getParameter("data");
            String sign=req.getParameter("sign");
            String signType =req.getParameter("signType");
            // 验证签名
            boolean valid;
            //try {
                Class ceasClass = Class.forName("cn.com.sand.ceas.sdk.CeasHttpUtil");
                Object o = ceasClass.newInstance();
                Method method = ceasClass.getDeclaredMethod("verifySign", JSONObject.class);
                method.setAccessible(true);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data",data);
                jsonObject.put("sign",sign);
                jsonObject.put("signType",signType);
                //执行verifySign方法
                valid = (boolean) method.invoke(o, jsonObject);
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
                    return "respCode=000000";
                }
            /*} catch (Exception e){
                log.error(e.getMessage());
            }*/
        }
        //log.info("获取到sand response为{}", resp);

       // appEventSender.send(BusConfig.SAND_PAY_CALLBACK_ROUTING_KEY, JSONObject.parseObject(resp.));
        /**
         * do something
         */
        // 收到天河链回调通知需回写大写“SUCCESS”，如没有回写则最多通知 9次，
        // 重试延迟时间分别为：5,5,20,270,600,900,1800,3600,14400（秒），9次后没有拿到回写则停止通知
        //return "SUCCESS";
        return null;
    }
}
