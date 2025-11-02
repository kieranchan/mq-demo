package com.itheima.consumer.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {
    @Bean
    public Queue objectQueue() {
        return QueueBuilder.durable("object.queue").build();
    }
}
