package com.liaody.sty.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;
import org.redisson.config.TransportMode;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Redisson客户端
 */
@Configuration
public class RedissonConfig {
    /**
     * 主从模式
     * @return RedissonClient
     */
    @Bean("redissonClient")
    public RedissonClient masterSlaveMode(){
        Config config = new Config();
        config
              .setCodec(new org.redisson.client.codec.StringCodec())
               //  .setTransportMode(TransportMode.EPOLL)

                .useMasterSlaveServers()
                // 设置主节点
                .setMasterAddress("redis://192.168.147.140:6379")
                // 设置从节点
                .addSlaveAddress("redis://192.168.147.143:6379", "redis://192.168.147.146:6379")
                // 设置从节点为只读模式
                .setReadMode(ReadMode.SLAVE)
                // 设置负载均衡算法
                .setLoadBalancer(new RoundRobinLoadBalancer());
        return Redisson.create(config);
    }
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

}
