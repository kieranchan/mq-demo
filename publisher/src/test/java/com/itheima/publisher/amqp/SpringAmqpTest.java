package com.itheima.publisher.amqp;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue() {
        String queueName = "simple.queue";
        String message = "Hello spring Amqp!";
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void testWorkQueue() throws InterruptedException {
        String queueName = "work.queue";
        String message = "hello,message_";
        for (int i = 1; i <= 50; i++) {
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }
    }

    @Test
    public void testFanoutExchange() {
        String exchangeName = "hmall.fanout";
        String message = "hello,everyone!";
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }

    @Test
    public void testDirectExchange() {
        String exchangeName = "hmall.direct";
//        String msg = "RoutingKey=Red";
//        rabbitTemplate.convertAndSend(exchangeName,"red",msg);
//        String msg = "RoutingKey=lue";
//        rabbitTemplate.convertAndSend(exchangeName,"blue",msg);
        String msg = "RoutingKey=yellow";
        rabbitTemplate.convertAndSend(exchangeName, "yellow", msg);
    }

    @Test
    public void testTopicExchange() {
        String exchangeName = "hmall.topic";
//        String message = "RoutingKey=china.news";
//        rabbitTemplate.convertAndSend(exchangeName, "china.news", message);
//        String message = "RoutingKey=china.news1";
//        rabbitTemplate.convertAndSend(exchangeName, "china.news1", message);
        String message = "RoutingKey=china1.news";
        rabbitTemplate.convertAndSend(exchangeName, "china1.news", message);
    }
}