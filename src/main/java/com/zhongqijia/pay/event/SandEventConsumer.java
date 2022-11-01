package com.zhongqijia.pay.event;

import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.bean.paysand.C2BSandPayCallBack;
import com.zhongqijia.pay.bean.paysand.C2CSandCallBack;
import com.zhongqijia.pay.common.util.RedisUtil;
import com.zhongqijia.pay.config.BusConfig;
import com.zhongqijia.pay.mapper.*;
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
            //log.info("sand payCallback处理:{}",message.toJSONString());
            //log.info("sand payCallback处理订单:{}", c2BSandPayCallBack.getBody().getTradeNo());
            String orderNo = c2BSandPayCallBack.getBody().getTradeNo();
        if (c2BSandPayCallBack.getHead().getRespCode().equals("000000") &&
                c2BSandPayCallBack.getHead().getRespMsg().equals("成功")) {
            YopEventConsumer.payCheck(redisUtil, orderNo, myOrderMapper, collectionMapper,
                    hideRecordMapper, blindboxMapper, issueMapper,
                    signupMapper, myboxMapper, userGrantMapper);
        }
    }

    @RabbitListener(queues = BusConfig.SAND_PAY_CALLBACK_C2C_QUEUE)
    public void payCallbackC2C(JSONObject message) {
        //log.info("sand payCallbackC2C接收到数据:{}",JSONObject.toJSONString(message, true));
        C2CSandCallBack c2CSandCallBack = JSONObject.parseObject(message.toJSONString(), C2CSandCallBack.class);
        //log.info("sand payCallbackC2C处理:{}",message.toJSONString());
        //log.info("sand payCallbackC2C处理:{}", c2CSandCallBack.getOrderNo());
        String orderNo = c2CSandCallBack.getOrderNo();
        YopEventConsumer.payCheck(redisUtil, orderNo, myOrderMapper, collectionMapper,
                hideRecordMapper, blindboxMapper, issueMapper,
                signupMapper, myboxMapper, userGrantMapper);
    }
}
