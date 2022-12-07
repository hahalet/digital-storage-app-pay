package com.zhongqijia.pay.event;

import com.alibaba.fastjson.JSONObject;
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
public class TongTongEventConsumer {
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

    @RabbitListener(queues = BusConfig.TT_PAY_CALLBACK_QUEUE)
    public void payCallback(JSONObject message) {
        String orderNo = (String)message.get("orderNo");
        if (orderNo == null || orderNo.length() == 0) {
            return;
        }
        boolean firstOrder = orderNo.startsWith(TiChainPayUtil.FIRST_ORDER);
        if (firstOrder) {
            YopEventConsumer.payCheckFirst(redisUtil, orderNo, myOrderMapper, collectionMapper,
                    hideRecordMapper, blindboxMapper, issueMapper,
                    signupMapper, myboxMapper);
        }
    }

    @RabbitListener(queues = BusConfig.TT_PAY_CALLBACK_C2C_QUEUE)
    public void payCallbackC2C(JSONObject message) {
        String orderNo = (String)message.get("orderNo");
        if (orderNo == null || orderNo.length() == 0) {
            return;
        }
        boolean secondOrder = orderNo.startsWith(TiChainPayUtil.SECOND_ORDER);
        if(secondOrder){
            YopEventConsumer.payCheckSecond(redisUtil, orderNo, userGrantMapper);
        }
    }
}
