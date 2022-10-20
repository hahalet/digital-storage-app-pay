package com.zhongqijia.pay.event;

import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.config.BusConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: LiaoSheng
 * @Date: 2022/9/20 18:02
 * @Description：
 */
@Slf4j
@Component
public class AppEventConsumer {

    @RabbitListener(queues = BusConfig.APP_TEST_QUEUE)
    public void processTest(JSONObject message) {
        log.info("processTest message = {}", message);
    }
}
