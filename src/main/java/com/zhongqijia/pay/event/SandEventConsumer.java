package com.zhongqijia.pay.event;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhongqijia.pay.bean.*;
import com.zhongqijia.pay.bean.payyop.LogYopCreatAccount;
import com.zhongqijia.pay.bean.payyop.LogYopPayCallBack;
import com.zhongqijia.pay.common.util.RedisUtil;
import com.zhongqijia.pay.config.BusConfig;
import com.zhongqijia.pay.mapper.*;
import com.zhongqijia.pay.utils.RedisHelp;
import com.zhongqijia.pay.utils.TiChainPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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
        log.info("YopEventConsumer message = {}", message);
        try {

        } catch (Exception e) {
            log.info("walletCallback error:{}", e.getMessage());
        }
    }

    @RabbitListener(queues = BusConfig.SAND_PAY_CALLBACK_QUEUE)
    public void payCallback(JSONObject message) {
        //锁一分钟,检查订单状态不允许回调修改订单
    }
}
