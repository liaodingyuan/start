package com.liaody.sty.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Properties;

/**
 * kafka消费者
 */
@Service
public class StyKafkaConsumer {

    /**
     * kafka消费者
     */
    private KafkaConsumer<String,String> consumer;

    @PostConstruct
    public void initConsumerPros(){
        Properties props = new Properties();
        props.put("bootstrap.servers","192.168.147.140:9092, 192.168.147.143:9092, 192.168.147.146:9092");
        props.put("group.id","countryCounter");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        consumer = new  KafkaConsumer<>(props);
    }

    public void achieve(){
        // 订阅topic
        consumer.subscribe(Collections.singletonList("topic"));
    }
}
