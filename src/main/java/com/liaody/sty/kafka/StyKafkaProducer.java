package com.liaody.sty.kafka;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * kafka生产者.
 * Java客户端是线程安全的，多个线程可以共享同一个producer实例
 */
@Service
@Slf4j
public class StyKafkaProducer {

    /**
     * kafka producer时实例安全的。而且单个producer要比多实例的producer效率高
     */
    private Producer<String, String> kafkaProducer;

    /**
     * 初始化kafka配置
     */
    @PostConstruct
    public void initKafka() {
        /**
         * kafka配置
         */
        Properties props = new Properties();
        // 设置broker，尽量设置齐全
        props.put("bootstrap.servers", "192.168.147.140:9092,192.168.147.143:9092,192.168.147.146:9092");
        // 等待所有ISR应答，可以防止消息丢失。 acks=0表示不需要等待broker确认。
        // acks=1表示server端将消息保存后即可发送ack，而不必等到其他follower角色的都收到了该消息
        props.put("acks", "all");
        // 生产者发送失败后，重试的次数
        props.put("retries", 0);
        //当多条消息发送到同一个partition时，该值控制生产者批量发送消息的大小，批量发送可以减少生产者到服务端的请求数，有助于提高客户端和服务端的性能。
        props.put("batch.size", 16384);
        // 默认情况下缓冲区的消息会被立即发送到服务端，即使缓冲区的空间并没有被用完。可以将该值设置为大于0的值，
        // 这样发送者将等待一段时间后，再向服务端发送请求，以实现每次请求可以尽可能多的发送批量消息。
        props.put("linger.ms", 1);
        // 生产者缓冲区的大小，保存的是还未来得及发送到server端的消息，如果生产者的发送速度大于消息被提交到server端的速度，该缓冲区将被耗尽。
        props.put("buffer.memory", 33554432);
        // 消息序列化器（消费者与生产者应该时一样的，否则消费者无法解码）。broker接收到的键喝值都是字节数组。
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // batch.size和linger.ms是两种实现让客户端每次请求尽可能多的发送消息的机制，它们可以并存使用，并不冲突。

        kafkaProducer = new KafkaProducer<>(props);
    }

    /**
     * 发送Kafka消息。四个元素组成一个ProducerRecord.
     *
     * @param partition 分区
     * @param topic     主题
     * @param key       key
     * @param value     值
     */
    public void send(String topic, String partition, String key, String value) {

//        *
// *keySerializer:发送数据key值的序列化方法，该方法实现了Serializer接口
//                *valueSerializer:发送数据value值的序列化方法，该方法实现了Serializer接口
//                */
//public KafkaProducer(Map<String,Object> configs);
//public KafkaProducer(Map<String,Object> configs, Serializer<K> keySerializer,Serializer<V> valueSerializer);
//public KafkaProducer(Properties properties);
//public KafkaProducer(Properties properties, Serializer<K> keySerializer,Serializer<V> valueSerializer);

        // 创建kafka生产者

        // 组装消息
        // topic, partition, key, value 四个参数。也有如下的三个参数 topic,key,value。topic, value
        // 没有指定分区，分区选择器会根据ProducerRecord的key选择一个分区
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        // 回调函数和异步发送方式来确认消息发送的进度
        kafkaProducer.send(record, (metadata, e) -> {
            // 发送失败，有异常消息
            if (e != null) {
                e.printStackTrace();
                // 手动重发
                send(topic, null,key, value);
            }
            // 发送成功，返回RecordMetaData
            else {
                System.out.println("The offset of the record we just sent is: " + metadata.offset());
            }
        });
        // 强制消费者立即发送  kafkaProducer.flush();
        //获取指定topic的partition元数据信息
        List<PartitionInfo> partitions;
        partitions = kafkaProducer.partitionsFor(topic);
        for (PartitionInfo p : partitions) {
            System.out.println(p);
        }
        System.out.println("send message over.");
        // 如果在结束produce时，没有调用close()方法，那么这些资源会发生泄露。
       // kafkaProducer.close(100, TimeUnit.MILLISECONDS);
    }

    /**
     * 同步发送kafka消息
     *
     * @param topic
     * @param partition
     * @param key
     * @param value
     */
    public void sendMsgSyn(String topic, String partition, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        try {
            Future<RecordMetadata> send = kafkaProducer.send(record);
            RecordMetadata recordMetadata = send.get();
            log.info("发送消息成功->{}", JSON.toJSONString(recordMetadata));
        } catch (Exception e) {
            log.info("同步发送消息失败->{}", e);
        }
    }

    /**
     * 异步发送kafka消息
     *
     * @param topic
     * @param partition
     * @param key
     * @param value
     */
    public void sendMsgAsyn(String topic, String partition, String key, String value) {

    }

    /**
     * 发送消息回调，实现回调接口
     */
    private class StyProducerCallback implements Callback{
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            // 发送异常
            if(e!=null){
                log.error("回调失败->{}",e);
                // 可以写入文件或者私信队列或者数据库
            }
            // 发送成功
            else{
                log.info("回调->{}",JSON.toJSONString(recordMetadata));
            }
        }
    }
}
