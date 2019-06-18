package com.liaody.sty.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * kafka自定义分区器
 */
public class BananaPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key,
                         byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 列出topic的所有分区
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        // 分区熟练
        int numPartitions = partitionInfos.size();
       if((keyBytes==null) || !(key instanceof String)){
           throw new IllegalArgumentException("消息必须设置key");
       }
       // 如果时供应商banana的消息，总是分配到最后一个分区，最后一个分区会为把男提供较大的磁盘空间
       if(((String)key).equalsIgnoreCase("Banana")){
          return numPartitions;
       }
       // 不是banana的记录，会被散列到其他分区
       return (Math.abs(Utils.murmur2(keyBytes)))/(numPartitions-1);
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
