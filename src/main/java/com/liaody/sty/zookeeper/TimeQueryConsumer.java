package com.liaody.sty.zookeeper;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 模拟zk客户端
 */
@Slf4j
public class TimeQueryConsumer {

    /**
     * 在线该服务列表
     */
    private volatile ArrayList<String> onlineServers = Lists.newArrayList();
    private String hosts;
    private ZooKeeper zooKeeper;
    private static final Integer SESSION_TIMEOUT = 2000;
  //  private CountDownLatch countDownLatch = new CountDownLatch(1);
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

                    // 客户端监听NodeChildrenChanged事件
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected
            && watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged){
                log.info("监听到子节点变化");
                try{
                    getOnlineServers();
                }catch (Exception e){
                    log.error("获取在线服务器失败,reson->{}",e);
                }
            }
        });

    }

    /**
     * 获取在线服务器列表
     */
    public void getOnlineServers() throws KeeperException, InterruptedException {

        List<String> children = zooKeeper.getChildren(PARENT_NODE, true);
        ArrayList<String> servers = Lists.newArrayList();
        for(String child:children){
            byte[] data = zooKeeper.getData(PARENT_NODE + "/" + child, false, null);
            String server = new String(data);
            servers.add(server);
        }
        onlineServers = servers;
        System.out.println("查询了一次zk，当前在线的服务器有：" + servers);
    }

    public void sendRequest(){
        Random random = new Random();
        // 挑选一台在线的服务器
        int nextInt = random.nextInt(onlineServers.size());
        String server = onlineServers.get(nextInt);
        String hostname = server.split(":")[0];
        int port = Integer.parseInt(server.split(":")[1]);

        System.out.println("本次请求挑选的服务器为：" + server);

    }
}
