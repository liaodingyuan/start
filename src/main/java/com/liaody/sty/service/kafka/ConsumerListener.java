package com.liaody.sty.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerListener {




    @KafkaListener(topics = "my-topic")
    public void listen(ConsumerRecord<?, ?> record) throws Exception {

        System.out.printf("consumer topic = %s, offset = %s, value = %s \n", record.topic(), record.key(), record.value());
    }

}
