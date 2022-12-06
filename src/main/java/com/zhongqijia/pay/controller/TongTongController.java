package com.zhongqijia.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.bean.paytongtong.LogTongtongPayCallBack;
import com.zhongqijia.pay.config.BusConfig;
import com.zhongqijia.pay.event.AppEventSender;
import com.zhongqijia.pay.mapper.LogTongtongPayCallBackMapper;
import com.zhongqijia.pay.service.PayTongTongService;
import com.zhongqijia.pay.utils.DateUtils;
import com.zhongqijia.pay.utils.PayHelp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    @Autowired
    private LogTongtongPayCallBackMapper logTongtongPayCallBackMapper;
    @Value("${tongtongPay.walletLoginReturnUrl}")
    private String walletLoginReturnUrl;
    @Value("${tongtongPay.payReturnUrl}")
    private String payReturnUrl;
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
        log.info("获取到walletCallback response为{}", JSON.toJSONString(parameterMap));
        return "000000";
    }

    @PostMapping(value = "/walletCallbackUrl")
    public String walletCallbackUrl(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String[]> parameterMap = req.getParameterMap();
        log.info("获取到walletCallback response为{}", JSON.toJSONString(parameterMap));
        try {
            resp.sendRedirect(walletLoginReturnUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "000000";
    }

    /**
     * 功能描述: 支付回调地址
     *
     * @param:
     * @return: 处理结果
     * @auther: xy
     */
    @PostMapping(value = "/payCallback")
    public String payCallback(@RequestBody LogTongtongPayCallBack logTongtongPayCallBack,
                              HttpServletRequest req, HttpServletResponse resp) {
        log.info("获取到payCallback response为{}", logTongtongPayCallBack);
        if(1>0){
            return "000000";
        }
        try{
            logTongtongPayCallBack.setCreate_time(DateUtils.getCurrentTimeStamp());
            logTongtongPayCallBackMapper.insert(logTongtongPayCallBack);
        }catch(Exception e){
            log.info("payCallback error:{}",e.getMessage());
        }
        if(logTongtongPayCallBack.getResp_code().equals("000000") &&
                logTongtongPayCallBack.getResp_msg().equals("success")){
            String orderNo = logTongtongPayCallBack.getOrder_no();
            JSONObject json = new JSONObject();
            json.put("orderNo",orderNo);
            if(orderNo.startsWith(PayHelp.FIRST_ORDER)){
                appEventSender.send(BusConfig.TT_PAY_CALLBACK_ROUTING_KEY, json);
            }else if(orderNo.startsWith(PayHelp.SECOND_ORDER)){
                appEventSender.send(BusConfig.TT_PAY_CALLBACK_C2C_ROUTING_KEY, json);
            }
        }

        return "000000";
    }

    @PostMapping(value = "/payCallbackUrl")
    public String payCallbackUrl(HttpServletRequest req, HttpServletResponse resp) {
        log.info("payCallbackUrl response");
        try {
            resp.sendRedirect(payReturnUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "000000";
    }

    @GetMapping(value = "/payOrderFirst")
    public String payOrderFirst(Integer userId, Integer orderId, String payerClientIp) {
        return payTongTongService.payOrderFirst(userId, orderId, payerClientIp);
    }

    @GetMapping(value = "/payOrderSecond")
    public String payOrderSecond(Integer grantId, String payerClientIp) {
        return payTongTongService.payOrderSecond(grantId, payerClientIp);
    }

    @GetMapping(value = "/getPayInfo")
    public JSONObject getPayInfo(@RequestParam String orderNo) throws Exception {
        return payTongTongService.getPayInfo(orderNo);
    }
    @GetMapping(value = "/walletLoginByUsers")
    public String walletLoginByUsers(Integer userId) {
        return payTongTongService.walletLoginByUsers(userId);
    }

    @GetMapping(value = "/getWalletInfo")
    public JSONObject getWalletInfo(@RequestParam Integer userId) throws Exception {
        return payTongTongService.walletIsOpen(userId);
    }
}
