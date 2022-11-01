package com.zhongqijia.pay.event;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhongqijia.pay.bean.*;
import com.zhongqijia.pay.bean.paysand.SandPayCallBack;
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
            SandPayCallBack sandPayCallBack = JSONObject.parseObject(message.toJSONString(), SandPayCallBack.class);
            log.info("sand payCallback处理:{}",message.toJSONString());
            log.info("sand payCallback处理订单:{}",sandPayCallBack.getBody().getTradeNo());
            String orderNo = sandPayCallBack.getBody().getTradeNo();
        if (sandPayCallBack.getHead().getRespCode().equals("000000") &&
                sandPayCallBack.getHead().getRespMsg().equals("成功")) {
            YopEventConsumer.payCheck(redisUtil, orderNo, myOrderMapper, collectionMapper,
                    hideRecordMapper, blindboxMapper, issueMapper,
                    signupMapper, myboxMapper, userGrantMapper);
        }
    }
}
