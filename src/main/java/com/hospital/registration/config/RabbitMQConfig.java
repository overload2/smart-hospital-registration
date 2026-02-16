package com.hospital.registration.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title: RabbitMQConfig
 * @author: Su
 * @date: 2026/2/14
 * @version: 1.0
 * @description: RabbitMQ配置类
 */
@Configuration
public class RabbitMQConfig {

    // 退款队列
    public static final String REFUND_QUEUE = "hospital.refund.queue";
    // 退款交换机
    public static final String REFUND_EXCHANGE = "hospital.refund.exchange";
    // 退款路由键
    public static final String REFUND_ROUTING_KEY = "hospital.refund";

    // 死信队列
    public static final String REFUND_DLX_QUEUE = "hospital.refund.dlx.queue";
    // 死信交换机
    public static final String REFUND_DLX_EXCHANGE = "hospital.refund.dlx.exchange";
    // 死信路由键
    public static final String REFUND_DLX_ROUTING_KEY = "hospital.refund.dlx";

    /**
     * 退款队列
     */
    @Bean
    public Queue refundQueue() {
        return QueueBuilder.durable(REFUND_QUEUE)
                // 配置死信交换机
                .deadLetterExchange(REFUND_DLX_EXCHANGE)
                .deadLetterRoutingKey(REFUND_DLX_ROUTING_KEY)
                // 消息TTL(可选,这里设置为1小时)
                .ttl(3600000)
                .build();
    }

    /**
     * 退款交换机
     */
    @Bean
    public DirectExchange refundExchange() {
        return new DirectExchange(REFUND_EXCHANGE, true, false);
    }

    /**
     * 绑定退款队列到交换机
     */
    @Bean
    public Binding refundBinding() {
        return BindingBuilder.bind(refundQueue())
                .to(refundExchange())
                .with(REFUND_ROUTING_KEY);
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue refundDlxQueue() {
        return QueueBuilder.durable(REFUND_DLX_QUEUE).build();
    }

    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange refundDlxExchange() {
        return new DirectExchange(REFUND_DLX_EXCHANGE, true, false);
    }

    /**
     * 绑定死信队列到死信交换机
     */
    @Bean
    public Binding refundDlxBinding() {
        return BindingBuilder.bind(refundDlxQueue())
                .to(refundDlxExchange())
                .with(REFUND_DLX_ROUTING_KEY);
    }

    /**
     * 配置消息转换器(使用JSON格式)
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}

