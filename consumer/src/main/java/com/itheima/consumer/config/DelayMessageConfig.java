package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DelayMessageConfig {
    @Bean
    public DirectExchange delayExchange() {
        return ExchangeBuilder.directExchange("delay.direct")
                .delayed()
                .durable(true)
                .build();
    }

    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable("delay.queue").build();
    }

    @Bean
    public Binding bingDelayQueue() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with("delay");
    }

}
