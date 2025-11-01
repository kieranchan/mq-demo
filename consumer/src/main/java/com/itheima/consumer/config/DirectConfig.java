package com.itheima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectConfig {
    @Bean
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange("hmall.direct").build();
    }

    @Bean
    public Queue directQueue1() {
        return QueueBuilder.durable("direct.queue1").build();
    }

    @Bean
    public Binding bindingQueue1WithRed() {
        return BindingBuilder.bind(directQueue1()).to(directExchange()).with("red");
    }

    @Bean
    public Binding bindingQueue1WithBlue() {
        return BindingBuilder.bind(directQueue1()).to(directExchange()).with("blue");
    }

    @Bean
    public Queue directQueue2() {
        return QueueBuilder.durable("direct.queue2").build();
    }

    @Bean
    public Binding bindingQueue2WithRed() {
        return BindingBuilder.bind(directQueue2()).to(directExchange()).with("red");
    }

    @Bean
    public Binding bindingQueue2WithYellow() {
        return BindingBuilder.bind(directQueue2()).to(directExchange()).with("yellow");
    }
}
