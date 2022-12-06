package com.zhongqijia.pay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.bean.*;
import com.zhongqijia.pay.common.util.RedisUtil;
import com.zhongqijia.pay.config.BusConfig;
import com.zhongqijia.pay.event.AppEventSender;
import com.zhongqijia.pay.mapper.*;
import com.zhongqijia.pay.service.PayTongTongService;
import com.zhongqijia.pay.utils.PayTongTongUtils;
import com.zhongqijia.pay.utils.RedisHelp;
import com.zhongqijia.pay.utils.SandBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class PayTongTongServiceImpl implements PayTongTongService {
    @Value("${tongtongPay.walletLoginNotifyUrl}")
    private String walletLoginNotifyUrl;
    @Value("${tongtongPay.walletLoginReturnUrl}")
    private String walletLoginReturnUrl;
    @Value("${tongtongPay.domain}")
    private String domain;
    @Value("${tongtongPay.tongtongPayRoot}")
    private String tongtongPayRoot;
    @Value("${tongtongPay.payNotifyUrl}")
    private String payNotifyUrl;
    @Value("${tongtongPay.payCallbackUrl}")
    private String payCallbackUrl;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AppEventSender appEventSender;
    @Autowired(required = false)
    private UsersMapper userMapper;
    @Autowired(required = false)
    private MyOrderMapper myOrderMapper;
    @Autowired(required = false)
    private CollectionMapper collectionMapper;
    @Autowired(required = false)
    private UserGrantMapper userGrantMapper;

    @Override
    public String walletLoginByUsers(Integer userId) {
        Users users = userMapper.selectById(userId);
        return PayTongTongUtils.loginWallet(users, walletLoginNotifyUrl, domain, tongtongPayRoot);
    }

    @Override
    public JSONObject walletIsOpen(Integer userId) {
        Users users = userMapper.selectById(userId);
        JSONObject jsonObjectReturn = new JSONObject();
        Map<String, String> res = PayTongTongUtils.getWalletInfo(users.getUserId(), tongtongPayRoot);
        boolean isOpened = false;
        try {
            if (res != null && res.get("resp_code").equals("000000")) {
                String user_status_list = res.get("user_status_list");
                JSONArray jsa = JSONObject.parseArray(user_status_list);
                if (!jsa.isEmpty()) {
                    JSONObject js = (JSONObject) jsa.get(0);
                    String user_status = js.getString("user_status");
                    isOpened = user_status.equals("1");
                }
            }
        } catch (Exception e) {

        }
        jsonObjectReturn.put("isOpened", isOpened);
        return jsonObjectReturn;
    }

    @Override
    public String payOrderFirst(Integer userId, Integer orderId, String payerClientIp) {
        Users users = userMapper.selectById(userId);
        MyOrder myOrder = myOrderMapper.selectById(orderId);
        Collection collection = collectionMapper.selectById(myOrder.getCollid());
        return PayTongTongUtils.payFirst(users, tongtongPayRoot, myOrder, collection,
                payCallbackUrl, payNotifyUrl, domain, payerClientIp, redisUtil,walletLoginNotifyUrl);
    }

    @Override
    public String payOrderSecond(Integer grantId, String payerClientIp) {
        UserGrant userGrant = userGrantMapper.selectById(grantId);
        Users user = userMapper.selectById(userGrant.getOppositeuser());
        Collection collection = collectionMapper.selectById(userGrant.getCollid());
        return PayTongTongUtils.paySecond(user, userGrant, collection, payerClientIp, tongtongPayRoot, payCallbackUrl, payNotifyUrl, domain, redisUtil, walletLoginNotifyUrl);
    }

    /**
     * {"app_id":"200102239651","attach":"123","goods_name":"英灵殿-刺客信条","goods_num":"1","goods_type":"2","mer_logo_url":"/profile/upload/2022/12/05/00176945bb935891082a1abdda665497_20221205151005A014.jpeg","mer_no":"200102239651","need_notify":"1","need_return":"1","notify_url":"http://120.77.148.40/aries2/tongtong/payCallback","order_amount":"0.01","order_no":"SCD44900_1670317915000_3","order_time":"20221206171523","pay_channels":"","return_url":"http://120.77.148.40/aries2/tongtong/payCallbackUrl","service":"fosun.sumpay.cashier.wap.trade.order.apply","share_benefit_exp":"{\"share_type\":\"1\",\"prior\":\"1\",\"benefit_bean_list\":[{\"account_type\":\"1\",\"user_id\":\"100208624013\",\"share_type\":\"1\",\"prior\":\"1\",\"amount\":\"0.01\"}]}","share_benefit_flag":"1","sub_mer_no":"","terminal_type":"wap","timestamp":"20221206171523","user_id":"100208716536","user_id_type":"1","user_ip_addr":"183.46.73.47","version":"1.0"}
     * @param orderNo
     * @return
     */
    @Override
    public JSONObject getPayInfo(String orderNo) {
        log.info("getPayInfo:{}",orderNo);
        JSONObject jsonObjectReturn = new JSONObject();
        //try {
            JSONObject param = new JSONObject();
            param.put("customerOrderNo", SandBase.getCustomerOrderNo()); //商户订单号
            if (orderNo.length() <= 30) {
                Integer index = (Integer) redisUtil.get(RedisHelp.SAND_PAY_ORDER_KEY + orderNo);
                if (index == null) {
                    jsonObjectReturn.put("isPayed", false);
                    return jsonObjectReturn;
                }
                orderNo = orderNo + "_" + index;
            }
            param.put("oriCustomerOrderNo", orderNo);//原交易订单号
            JSONObject jsonObject = PayTongTongUtils.getOrderInfo(orderNo, domain, tongtongPayRoot);

            log.info("getPayInfo:{}",jsonObject.toJSONString());
            String oriResponseCode = jsonObject.getString("resp_code");
            String orirOderStatus = jsonObject.getString("status");//0失败1 成功 2 处理中
            String oriCustomerOrderNo = jsonObject.getString("order_no");
            if (oriResponseCode != null && oriResponseCode.equals("00000") &&
                    orirOderStatus != null && orirOderStatus.equals("1")) {
                JSONObject json = new JSONObject();
                json.put("orderNo", oriCustomerOrderNo);
                appEventSender.send(BusConfig.TT_PAY_CALLBACK_C2C_ROUTING_KEY, json);
            }

            if (oriCustomerOrderNo != null && oriCustomerOrderNo.length() > 0) {
                if (orirOderStatus != null && (orirOderStatus.equals("1") || orirOderStatus.equals("2"))) {
                    jsonObjectReturn.put("isPayed", true);
                } else {
                    jsonObjectReturn.put("isPayed", false);
                }
            } else {
                jsonObjectReturn.put("isPayed", false);
            }
        //} catch (Exception e) {
        //    log.info("getPayInfo error:{}", e.getMessage());
        //}
        return jsonObjectReturn;
    }
}
