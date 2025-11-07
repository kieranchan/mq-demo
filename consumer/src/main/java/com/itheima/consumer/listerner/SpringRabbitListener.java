package com.itheima.consumer.listerner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Date;
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

    // deadExchange與delayQueue
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "ttl.queue",
//                    durable = "true",
//                    arguments = {
//                            @Argument(name = "x-message-ttl", value = "5000",type = "java.lang.Integer"),
//                            @Argument(name = "x-dead-letter-exchange", value = "ttl.direct"),
//                            @Argument(name = "x-dead-letter-routing-key", value = "blue")
//                    }),
//            exchange = @Exchange(name = "ttl.fanout", type = "fanout")
//    ))
//    public void listenTTLMessage() {
//        log.info("死信交換機傳遞成功！");
//    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "ttl.queue1", durable = "true"),
            exchange = @Exchange(name = "ttl.direct"),
            key = {"blue"}
    ))
    public void listenTTLMessage2(String msg, Message message) {
//        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
//        log.info(msg);
        long now = System.currentTimeMillis();
        Map<String, Object> headers = message.getMessageProperties().getHeaders();

        Long publishedMs = null;
        Object h = headers.get("x-published-at-ms");
        if (h instanceof Number n) {
            publishedMs = n.longValue();
        } else if (h instanceof String s) {
            try {
                publishedMs = Long.parseLong(s);
            } catch (NumberFormatException ignore) {
            }
        }

        // 後備：用 AMQP 標準 timestamp（如果 publisher 有 set 過）
        Date ts = message.getMessageProperties().getTimestamp();

        Long delayMs = null;
        if (publishedMs != null) {
            delayMs = now - publishedMs;
        } else if (ts != null) {
            delayMs = now - ts.getTime();
        }

        // 你原本已有 log，呢度直接印延遲
        // 記得實戰用 logger（紀錄器），唔好長期用 System.out
        log.info("ttl.direct → ttl.queue1 delay = {} ms, body={}", delayMs, msg);
    }
}
