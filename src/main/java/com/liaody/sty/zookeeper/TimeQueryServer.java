package com.liaody.sty.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Service
public class TimeQueryServer {

    private String hosts;
    private ZooKeeper zooKeeper;
    private static final Integer SESSION_TIMEOUT = 2000;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String PARENT_NODE = "/servers";

    @PostConstruct
    public void init() {
        hosts  = "192.168.147.140:2181,192.168.147.143:2181,192.168.147.146:2181";
    }

    /**
     * 连接zk server
     * @throws IOException io异常
     * @throws InterruptedException 中断异常
     */
    public void connectionToZk() throws IOException, InterruptedException {
        zooKeeper = new ZooKeeper(
                // zookeeper server host
                hosts,
                // connection time out
                SESSION_TIMEOUT, watchedEvent -> {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected){
                log.info("连接zookeeper完毕");
                countDownLatch.countDown();
            }
        });
        // 连接zk server设计一个异步调用的过程，这里确保连接成功之后才能够使用zk客户端
        countDownLatch.wait();
    }

    /**
     * 服务注册（服务上线）
     * @param host 服务主机
     * @param port 服务端口
     */
    public void registerServer(String host,String port) throws KeeperException, InterruptedException {

        // 首先判断父节点是否存在，不存在的话先创建父节点
        Stat stat = zooKeeper.exists("/servers", false);
        // 如果父节点为空，则创建永久型父节点
        if(stat==null){
            zooKeeper.create("/servers","server".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        }
        // 注册服务数据到zk的约定的节点下（临时节点，服务掉线之后会删除这一个临时节点，从而得知服务下线）
        String create = zooKeeper.create(PARENT_NODE+"/server",(host+":"+port).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(host+" 服务器向zk注册信息成功，注册的节点为：" + create);
    }

}
