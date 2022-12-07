package com.zhongqijia.pay.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: LiaoSheng
 * @Date: 2022/9/20 16:16
 * @Description：
 */
@Configuration
@Slf4j
public class BusConfig {

    // mq路由 交换机名称
    public static final String RABBIT_APP_EXCHANGE = "rabbit.app.exchange";

    // Test queue/routingKey
    public static final String APP_TEST_QUEUE = "app.test.queue";
    public static final String APP_TEST_ROUTING_KEY = "app.test.routingkey";

    // yop queue/routingKey
    public static final String YOP_WALLET_CALLBACK_QUEUE = "yop.wallet.callback.queue";
    public static final String YOP_WALLET_CALLBACK_ROUTING_KEY = "yop.wallet.callback.routingkey";

    public static final String YOP_PAY_CALLBACK_QUEUE = "yop.pay.callback.queue";
    public static final String YOP_PAY_CALLBACK_ROUTING_KEY = "yop.pay.callback.routingkey";

    // sand queue/routingKey
    public static final String SAND_WALLET_CALLBACK_QUEUE = "sand.wallet.callback.queue";
    public static final String SAND_WALLET_CALLBACK_ROUTING_KEY = "sand.wallet.callback.routingkey";

    public static final String SAND_PAY_CALLBACK_QUEUE = "sand.pay.callback.queue";
    public static final String SAND_PAY_CALLBACK_ROUTING_KEY = "sand.pay.callback.routingkey";

    public static final String SAND_PAY_CALLBACK_C2C_QUEUE = "sand.pay.ctoc.callback.queue";
    public static final String SAND_PAY_CALLBACK_C2C_ROUTING_KEY = "sand.pay.ctoc.callback.routingkey";

    public static final String TT_PAY_CALLBACK_QUEUE = "tt.pay.callback.queue";
    public static final String TT_PAY_CALLBACK_ROUTING_KEY = "tt.pay.callback.routingkey";

    public static final String TT_PAY_CALLBACK_C2C_QUEUE = "tt.pay.ctoc.callback.queue";
    public static final String TT_PAY_CALLBACK_C2C_ROUTING_KEY = "tt.pay.ctoc.callback.routingkey";
    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
    }

    /**
     * 注册交换机
     */
    @Bean(RABBIT_APP_EXCHANGE)
    TopicExchange exchangeApp () {
        log.info("exchange:{}", RABBIT_APP_EXCHANGE);
        return new TopicExchange(RABBIT_APP_EXCHANGE);
    }

    /**
     * 注册队列
     */
    @Bean(APP_TEST_QUEUE)
    Queue queueTest() {
        log.info("queue name:{}", APP_TEST_QUEUE);
        return new Queue(APP_TEST_QUEUE, false);
    }

    /**
     * 绑定: 队列--交换机--routingKey
     */
    @Bean
    Binding bindingTest(@Qualifier(APP_TEST_QUEUE) Queue queue, @Qualifier(RABBIT_APP_EXCHANGE) TopicExchange exchange) {
        log.info("binding {} to {} whit {}", queue, exchange, APP_TEST_ROUTING_KEY);
        return BindingBuilder.bind(queue).to(exchange).with(APP_TEST_ROUTING_KEY);
    }

    /**
     * 易宝开户队列
     */
    @Bean(YOP_WALLET_CALLBACK_QUEUE)
    Queue queueYopWalletCallback() {
        log.info("queue name:{}", YOP_WALLET_CALLBACK_QUEUE);
        return new Queue(YOP_WALLET_CALLBACK_QUEUE, false);
    }

    @Bean(YOP_WALLET_CALLBACK_ROUTING_KEY)
    Binding bindingYopWalletCallback(@Qualifier(YOP_WALLET_CALLBACK_QUEUE) Queue queue, @Qualifier(RABBIT_APP_EXCHANGE) TopicExchange exchange) {
        log.info("bindingYopCallback {} to {} with {}", queue, exchange, YOP_WALLET_CALLBACK_ROUTING_KEY);
        return BindingBuilder.bind(queue).to(exchange).with(YOP_WALLET_CALLBACK_ROUTING_KEY);
    }

    /**
     * 易宝支付队列
     */
    @Bean(YOP_PAY_CALLBACK_QUEUE)
    Queue queueYopPayCallback() {
        log.info("queue name:{}", YOP_PAY_CALLBACK_QUEUE);
        return new Queue(YOP_PAY_CALLBACK_QUEUE, false);
    }

    @Bean(YOP_PAY_CALLBACK_ROUTING_KEY)
    Binding bindingYopPayCallback(@Qualifier(YOP_PAY_CALLBACK_QUEUE) Queue queue, @Qualifier(RABBIT_APP_EXCHANGE) TopicExchange exchange) {
        log.info("bindingYopCallback {} to {} with {}", queue, exchange, YOP_PAY_CALLBACK_ROUTING_KEY);
        return BindingBuilder.bind(queue).to(exchange).with(YOP_PAY_CALLBACK_ROUTING_KEY);
    }

    /**
     * 杉德开户队列
     */
    @Bean(SAND_WALLET_CALLBACK_QUEUE)
    Queue queueSandWalletCallback() {
        log.info("queue name:{}", SAND_WALLET_CALLBACK_QUEUE);
        return new Queue(SAND_WALLET_CALLBACK_QUEUE, false);
    }

    @Bean(SAND_WALLET_CALLBACK_ROUTING_KEY)
    Binding bindingSandWalletCallback(@Qualifier(SAND_WALLET_CALLBACK_QUEUE) Queue queue, @Qualifier(RABBIT_APP_EXCHANGE) TopicExchange exchange) {
        log.info("bindingSandCallback {} to {} with {}", queue, exchange, SAND_WALLET_CALLBACK_ROUTING_KEY);
        return BindingBuilder.bind(queue).to(exchange).with(SAND_WALLET_CALLBACK_ROUTING_KEY);
    }

    /**
     * 杉德支付队列
     */
    @Bean(SAND_PAY_CALLBACK_QUEUE)
    Queue queueSandPayCallback() {
        log.info("queue name:{}", SAND_PAY_CALLBACK_QUEUE);
        return new Queue(SAND_PAY_CALLBACK_QUEUE, false);
    }

    @Bean(SAND_PAY_CALLBACK_ROUTING_KEY)
    Binding bindingSandPayCallback(@Qualifier(SAND_PAY_CALLBACK_QUEUE) Queue queue, @Qualifier(RABBIT_APP_EXCHANGE) TopicExchange exchange) {
        log.info("bindingSandCallback {} to {} with {}", queue, exchange, SAND_PAY_CALLBACK_ROUTING_KEY);
        return BindingBuilder.bind(queue).to(exchange).with(SAND_PAY_CALLBACK_ROUTING_KEY);
    }

    /**
     * 杉德支付队列C2C
     */
    @Bean(SAND_PAY_CALLBACK_C2C_QUEUE)
    Queue queueSandPayC2CCallback() {
        log.info("queue name:{}", SAND_PAY_CALLBACK_C2C_QUEUE);
        return new Queue(SAND_PAY_CALLBACK_C2C_QUEUE, false);
    }

    @Bean(SAND_PAY_CALLBACK_C2C_ROUTING_KEY)
    Binding bindingSandPayC2CCallback(@Qualifier(SAND_PAY_CALLBACK_C2C_QUEUE) Queue queue, @Qualifier(RABBIT_APP_EXCHANGE) TopicExchange exchange) {
        log.info("bindingSandCallback {} to {} with {}", queue, exchange, SAND_PAY_CALLBACK_C2C_ROUTING_KEY);
        return BindingBuilder.bind(queue).to(exchange).with(SAND_PAY_CALLBACK_C2C_ROUTING_KEY);
    }

    /**
     * 统统支付队列
     */
    @Bean(TT_PAY_CALLBACK_QUEUE)
    Queue queueTTPayCallback() {
        log.info("queue name:{}", TT_PAY_CALLBACK_QUEUE);
        return new Queue(TT_PAY_CALLBACK_QUEUE, false);
    }

    @Bean(TT_PAY_CALLBACK_ROUTING_KEY)
    Binding bindingTTPayCallback(@Qualifier(TT_PAY_CALLBACK_QUEUE) Queue queue, @Qualifier(RABBIT_APP_EXCHANGE) TopicExchange exchange) {
        log.info("bindingTTCallback {} to {} with {}", queue, exchange, TT_PAY_CALLBACK_ROUTING_KEY);
        return BindingBuilder.bind(queue).to(exchange).with(TT_PAY_CALLBACK_ROUTING_KEY);
    }

    /**
     * 统统支付队列C2C
     */
    @Bean(TT_PAY_CALLBACK_C2C_QUEUE)
    Queue queueTTPayC2CCallback() {
        log.info("queue name:{}", TT_PAY_CALLBACK_C2C_QUEUE);
        return new Queue(TT_PAY_CALLBACK_C2C_QUEUE, false);
    }

    @Bean(TT_PAY_CALLBACK_C2C_ROUTING_KEY)
    Binding bindingTTPayC2CCallback(@Qualifier(TT_PAY_CALLBACK_C2C_QUEUE) Queue queue, @Qualifier(RABBIT_APP_EXCHANGE) TopicExchange exchange) {
        log.info("bindingSandCallback {} to {} with {}", queue, exchange, TT_PAY_CALLBACK_C2C_ROUTING_KEY);
        return BindingBuilder.bind(queue).to(exchange).with(TT_PAY_CALLBACK_C2C_ROUTING_KEY);
    }

}
