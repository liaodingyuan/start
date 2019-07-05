package com.liaody.sty.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Properties;

/**
 * kafka消费者
 */
@Service
@Slf4j
public class StyKafkaConsumer {

    /**
     * kafka消费者
     */
    private KafkaConsumer<String,String> consumer;

    @PostConstruct
    public void initConsumerPros(){
        Properties props = new Properties();
        // 者三者是创建一个消费者必须的。
        props.put("bootstrap.servers","192.168.147.140:9092,192.168.147.143:9092,192.168.147.146:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // 非必须，但是都应该显示进行设置的。
        props.put("group.id","countryCounter");
        // 禁止自动提交(如果设置为自动提交)
        props.put("auto.commit.offset","false");
        // 设置自动提交的时间间隔。默认是5秒。如果发生了再均衡，可能会产生重复消费的后果。
        // props.put("auto.commit.interval.ms","");

        consumer = new  KafkaConsumer<>(props);

        // 订阅topic，可以使用正则表达式匹配多个topic
        consumer.subscribe(Collections.singletonList("topic"));
    }


    /**
     * consumer轮询消费消息
     */
    public void consumerMsg(){

        // 轮询是kafka消费者的核心
        try
        {
            // 消费者必须轮询kafka数据请求，否则会被认为已经死掉，它的分区会被移交其他消费者
            while(true){
                // 超时间设置为100秒，poll的阻塞时间。队列中没有数据的时候会阻塞这一段书时间
                // 第一次调用该方法的时候，会查找GroupCoordinator，会加入组。接收再分区分配
                ConsumerRecords<String, String> records = consumer.poll(100);
                for(ConsumerRecord<String,String> record:records){
                    // 消息要素
                    log.debug("topic=%s,partition=%s,offset=%d,customer=%s,country=%s\n",
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value()
                            );
                    // 手动异步提交偏移量
                    try{
                        consumer.commitSync();
                    }catch (Exception e){

                    }

                }
              // 注意应该一个消费者使用一条线程，每个消费者运行再自己的线程上。消费者应该使用线程池
            }
        }catch (Exception e){
            log.error("consumer message success.");
        }finally {
            // 会立即触发一次再均衡，不会等到组协调发现它不再发送心跳二判断该消费者已经死亡。
            consumer.close();
        }


    }
}
