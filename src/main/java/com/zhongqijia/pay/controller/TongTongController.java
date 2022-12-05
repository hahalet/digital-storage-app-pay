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
     * 功能描述: 支付回调地址
     *
     * @param:
     * @return: 处理结果
     * @auther: xy
     */
    @PostMapping(value = "/payCallback")
    public String payCallback(String resp_code,
                              String resp_msg,
                              String sign_type,//	是	MAX(10)	暂只支持CERT；必须大写
                              String sign,//	是	MAX(255)	签名, 参照 签名机制
                              String mer_no,//	是	MAX(20)	唯一确定一个商户的代码，商户在商盟开户时，由商盟告知商户。
                              String order_no,//	是	MAX（30)	订单号码
                              String offset_amount,//	是	MAX(30)
                              String paid_amount,//	是	MAX(20)
                              String cash_amount,//	否	MAX(20)
                              String trade_no,//	是	MAX（30)
                              String order_time,//	是	MAX（14)	订单创建日期yyyymmddhhmmss
                              String status,//	是	MAX（1)	0：失败
                              String success_time,//	是	MAX（14)	如：20130520125022
                              String success_amount,//	是	MAX（10)	订单交易金额，保留两位小数50.01
                              String remark,//	否	MAX(100)
                              String ttfReturnCode,//	否	MAX(20)	失败的时候可能会返回
                              String ttfReturnMsg,//否	MAX(255)	失败的时候可能会返回
                              String trade_code,//	否	MAX(5)	T0001：担保交易
                              String pay_product_code,//	否	MAX(5)	参考5.12.13目录附录五中的支付产品码
                              String payment_type,//	否	MAX(2)	00：借记卡
                              String offset_detail,//	否	MAX(4000)
                              String channel_serial_no,//	否	MAX(30)	有则返回
                              String period_num,//	否	MAX(10)	信用卡分期支付必填，暂只有：3、6、9、12、18、24、36、60
                              String attach,//
                              HttpServletRequest req, HttpServletResponse resp) {
        log.info("获取到payCallback response为{}", mer_no+","+order_no);
        return "000000";
    }

    @GetMapping(value = "/payOrderFirst")
    public String payOrderFirst(Integer userId, Integer orderId, String payerClientIp) {
        return payTongTongService.payOrderFirst(userId, orderId, payerClientIp);
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
