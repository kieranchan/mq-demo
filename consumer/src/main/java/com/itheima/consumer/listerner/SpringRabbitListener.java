package com.itheima.consumer.listerner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Map;

@Slf4j
@Component
public class SpringRabbitListener {

    // 消費者確認機制測試
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage(String msg) {
        log.info("spring 消費者接收到消息：【{}】", msg);
        throw new RuntimeException("故意的！");
//        throw new MessageConversionException("故意的！");
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消費者1接收到消息：" + "【" + msg + "】:" + LocalTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue2(String msg) throws InterruptedException {
        System.out.println("消費者2...接收到消息：" + "【" + msg + "】:" + LocalTime.now());
        Thread.sleep(200);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue3(String msg) throws InterruptedException {
        System.out.println("消費者3......接收到消息：" + "【" + msg + "】:" + LocalTime.now());
        Thread.sleep(2000);
    }

    // 監聽Fanout交換機的隊列
    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String msg) {
        System.out.println("消费者1接收到Fanout消息：【" + msg + "】");
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String msg) {
        System.out.println("消费者2接收到Fanout消息：【" + msg + "】");
    }

    // 監聽Direct交換機的隊列
//    @RabbitListener(queues = "direct.queue1")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "hmall.direct"),
            key = {"red", "blue"}
    ))
    public void listenDirectQueue1(String msg) {
        System.out.println("消费者1接收到direct.queue1的消息：【" + msg + "】");
    }

    //    @RabbitListener(queues = "direct.queue2")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "hmall.direct"),
            key = {"red", "yellow"}
    ))
    public void listenDirectQueue2(String msg) {
        System.out.println("消费者2接收到direct.queue2的消息：【" + msg + "】");
    }

    // 監聽Topic交換機的隊列
//    @RabbitListener(queues = "topic.queue1")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(name = "hmall.topic", type = ExchangeTypes.TOPIC),
            key = "china.#"
    ))
    public void listenTopicQueue1(String msg) {
        System.out.println("消費者1接收到topic.queue1的消息：【" + msg + "】");
    }

    //    @RabbitListener(queues = "topic.queue2")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue2"),
            exchange = @Exchange(name = "hmall.topic", type = ExchangeTypes.TOPIC),
            key = "#.news"
    ))
    public void listenTopicQueue2(String msg) {
        System.out.println("消費者2接收到topic.queue2的消息：【" + msg + "】");
    }

    // 監聽Map類型的數據
    @RabbitListener(queues = "object.queue")
    public void listenMapMessage(Map<String, Object> map) {
        System.out.println(map);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "test.lazyQueue",
                    durable = "true",
                    arguments = @Argument(name = "x-queue-mode", value = "lazy")
            ),
            exchange = @Exchange(name = "lazyQueue.direct"),
            key = {"pay"}
    ))
    public void listerLazyQueue() {
        System.out.println("監聽成功！");
    }
}
