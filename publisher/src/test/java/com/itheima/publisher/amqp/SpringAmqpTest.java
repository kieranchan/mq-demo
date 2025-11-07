package com.itheima.publisher.amqp;

import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

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

    // Fanout類型交換機測試
    @Test
    public void testFanoutExchange() {
        String exchangeName = "hmall.fanout";
        String message = "hello,everyone!";
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }

    // Direct類型交換機測試
    @Test
    public void testDirectExchange() {
        String exchangeName = "hmall.direct";
        String msg = "RoutingKey=red";
        rabbitTemplate.convertAndSend(exchangeName, "red", msg);

        msg = "RoutingKey=blue";
        rabbitTemplate.convertAndSend(exchangeName, "blue", msg);

        msg = "RoutingKey=yellow";
        rabbitTemplate.convertAndSend(exchangeName, "yellow", msg);
    }

    // Topic類型交換機測試
    @Test
    public void testTopicExchange() {
        String exchangeName = "hmall.topic";
        String message = "RoutingKey=china.news";
        rabbitTemplate.convertAndSend(exchangeName, "china.news", message);

        message = "RoutingKey=china.news1";
        rabbitTemplate.convertAndSend(exchangeName, "china.news1", message);

        message = "RoutingKey=china1.news";
        rabbitTemplate.convertAndSend(exchangeName, "china1.news", message);
    }

    // 消息轉換器測試
    @Test
    public void testSendMap() {
        HashMap<String, Object> msg = new HashMap<>();
        msg.put("name", "張三");
        msg.put("age", 16);
        rabbitTemplate.convertAndSend("object.queue", msg);
    }

    @Test
    public void sentMessageToLazyQueue() {
        rabbitTemplate.convertAndSend("lazy.queue.direct", "pay", "1");
    }

    @Test
    public void sentDelayQueue() {
        rabbitTemplate.convertAndSend("ttl.fanout", "blue", "延遲消息測試！", message -> {
            // 設置時間
            var props = message.getMessageProperties();
            long now = System.currentTimeMillis();
            props.setHeader("x-published-at-ms", now);        // 自家頭：毫秒
            props.setTimestamp(new java.util.Date(now));       // AMQP 標準 timestamp（可作後備）
            // 如果用每條訊息 TTL，可以順手：
             props.setExpiration("5000"); // 設置超時時間，例：5秒（字串）
            return message;
        });
    }
}