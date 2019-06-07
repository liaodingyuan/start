package com.liaody.sty.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;


/**
 * zk客户端拦截监听器
 */
@Slf4j
public class ConnectionWatcher implements Watcher {
    /**
     * ZX客户端连接server时候的超时啊时间
     */
    private static final int SESSTION_TIIMEOUT  = 1000;
    /**
     * 使用计数器避免客户端还没有连接上zk server就已经被使用。
     */
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    protected ZooKeeper zk = null;

    @Override
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
            countDownLatch.countDown();
        }
    }

    public void connection(String hosts) throws IOException, InterruptedException {
        if(StringUtils.isEmpty(hosts)){
            log.error("zk server hosts is null");
            return;
        }
        zk = new ZooKeeper(
                // zookeeper server host
                hosts,
                // connection time out
                SESSTION_TIIMEOUT,
                // watcher zk客户端向zk server注册自己干兴趣的事件
                this);
        // 阻塞当前线程的运行，如果已经连接上了server。zk server 会向Watcher发送回调事件，countDownLatch会减一，await会被唤醒
        countDownLatch.await();
    }

    /**
     * 关闭zk客户端
     */
    public void close(){

        if(zk !=null){
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                zk = null;
                System.gc();
            }
        }

    }
}
