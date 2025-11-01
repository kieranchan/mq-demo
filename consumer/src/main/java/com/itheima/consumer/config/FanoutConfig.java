package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfig {
    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder.fanoutExchange("hmall.fanout").build();
    }

    @Bean
    public Queue fanoutQueue1() {
        return QueueBuilder.durable("fanout.queue1").build();
    }

    @Bean
    public Binding bindingQueue1() {
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }

    @Bean
    public Queue fanoutQueue2() {
        return QueueBuilder.durable("fanout.queue2").build();
    }

    @Bean
    public Binding bindingQueue2() {
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }
}

