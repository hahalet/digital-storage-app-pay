package com.zhongqijia.pay.event;

import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.config.BusConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: LiaoSheng
 * @Date: 2022/9/20 16:43
 * @Description：
 */
@Component
@Slf4j
public class AppEventSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageConverter messageConverter;

    @PostConstruct
    public void init() {
        rabbitTemplate.setMessageConverter(messageConverter);
    }

    public void send(String routingKey, JSONObject object) {
        log.info("routingKey:{}=>message:{}", routingKey, object);
        rabbitTemplate.convertAndSend(BusConfig.RABBIT_APP_EXCHANGE, routingKey, object);
    }
}
