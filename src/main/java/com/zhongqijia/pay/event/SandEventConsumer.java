package com.zhongqijia.pay.event;

import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.bean.paysand.C2BSandPayCallBack;
import com.zhongqijia.pay.bean.paysand.C2CSandCallBack;
import com.zhongqijia.pay.common.util.RedisUtil;
import com.zhongqijia.pay.config.BusConfig;
import com.zhongqijia.pay.mapper.*;
import com.zhongqijia.pay.utils.TiChainPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SandEventConsumer {
    @Autowired(required = false)
    UsersMapper userMapper;
    @Autowired(required = false)
    MyOrderMapper myOrderMapper;
    @Autowired(required = false)
    CollectionMapper collectionMapper;
    @Autowired(required = false)
    HideRecordMapper hideRecordMapper;
    @Autowired(required = false)
    BlindboxMapper blindboxMapper;
    @Autowired(required = false)
    IssueMapper issueMapper;
    @Autowired(required = false)
    SignupMapper signupMapper;
    @Autowired(required = false)
    MyboxMapper myboxMapper;
    @Autowired(required = false)
    UserGrantMapper userGrantMapper;
    @Autowired
    private RedisUtil redisUtil;

    @RabbitListener(queues = BusConfig.SAND_WALLET_CALLBACK_QUEUE)
    public void walletCallback(JSONObject message) {
        log.info("SandEventConsumer message = {}", message);
        try {

        } catch (Exception e) {
            log.info("Sand walletCallback error:{}", e.getMessage());
        }
    }

    @RabbitListener(queues = BusConfig.SAND_PAY_CALLBACK_QUEUE)
    public void payCallback(JSONObject message) {
        C2BSandPayCallBack c2BSandPayCallBack = JSONObject.parseObject(message.toJSONString(), C2BSandPayCallBack.class);
        String orderNo = c2BSandPayCallBack.getBody().getTradeNo();
        if (orderNo == null || orderNo.length() == 0) {
            return;
        }
        boolean firstOrder = orderNo.startsWith(TiChainPayUtil.FIRST_ORDER);
        if (firstOrder && c2BSandPayCallBack.getHead().getRespCode().equals("000000") &&
                c2BSandPayCallBack.getHead().getRespMsg().equals("成功")) {
            YopEventConsumer.payCheckFirst(redisUtil, orderNo, myOrderMapper, collectionMapper,
                    hideRecordMapper, blindboxMapper, issueMapper,
                    signupMapper, myboxMapper, userGrantMapper);
        }
    }

    @RabbitListener(queues = BusConfig.SAND_PAY_CALLBACK_C2C_QUEUE)
    public void payCallbackC2C(JSONObject message) {
        C2CSandCallBack c2CSandCallBack = JSONObject.parseObject(message.toJSONString(), C2CSandCallBack.class);
        String orderNo = c2CSandCallBack.getOrderNo();
        YopEventConsumer.payCheckSecond(redisUtil, orderNo, myOrderMapper, collectionMapper,
                hideRecordMapper, blindboxMapper, issueMapper,
                signupMapper, myboxMapper, userGrantMapper);
    }
}
