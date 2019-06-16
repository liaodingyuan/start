package com.liaody.sty.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resources;

/**
 * kafka生产者控制器
 */
@RestController
@RequestMapping("/kafka")
public class KafkaProducerController {

    protected final Logger logger = LoggerFactory.getLogger(KafkaProducerController.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String sendKafka(@RequestParam("message") String message) {
        try {
            logger.info("kafka的消息={}", message);
            ListenableFuture<SendResult<String, String>> future =  kafkaTemplate.send("my-topic", "key", message);
            future.addCallback(o -> System.out.println("send-消息发送成功：" + message), throwable -> System.out.println("消息发送失败：" + message));
            logger.info("发送kafka成功.");
            return "successs";
        } catch (Exception e) {
            logger.error("发送kafka失败", e);
            return "failure";
        }
    }

    @RequestMapping(value = "/send2", method = RequestMethod.GET)
    public void sendKafka2(@RequestParam("message") String message) {
            ListenableFuture<SendResult<String, String>> future =  kafkaTemplate.send("my-topic", "key", message);
            future.addCallback(o -> System.out.println("send-消息发送成功：" + message), throwable -> System.out.println("消息发送失败：" + message));

    }
}
