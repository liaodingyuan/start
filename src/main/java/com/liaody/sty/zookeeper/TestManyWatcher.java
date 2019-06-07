package com.liaody.sty.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * 验证一个znode注册多个watcher
 */
@Slf4j
public class TestManyWatcher implements Runnable {

    public static void main(String[] args) {
        TestManyWatcher testManyWatcher = new TestManyWatcher();

        new Thread(testManyWatcher).start();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        // 1.验证在一个znode上使用exists的方式注册多个监听器。在节点发生create事件时事件的通知情况。
        // 2.验证一个节点上使用getData方式注册多个监听器，在节点发生crate时的事件通知情况。
        /*

         * 验证过程如下：

         * 1、验证一个节点X上使用exist方式注册的多个监听器（ManyWatcherOne、ManyWatcherTwo），

         *      在节点X发生create事件时的事件通知情况

         * 2、验证一个节点Y上使用getDate方式注册的多个监听器（ManyWatcherOne、ManyWatcherTwo），

         *      在节点X发生create事件时的事件通知情况

         * */

        //默认监听：注册默认监听是为了让None事件都由默认监听处理，

        //不干扰ManyWatcherOne、ManyWatcherTwo的日志输出
        ManyWatcherDefault watcherDefault = new ManyWatcherDefault();
        ZooKeeper zkClient = null;
        try {
            zkClient = new ZooKeeper("192.168.147.143:2181", 120000, watcherDefault);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return;
        }
        //默认监听也可以使用register方法注册
        //zkClient.register(watcherDefault);
        //1、========================================================
        log.info("=================以下是第一个实验");
        String path = "/X";
        ManyWatcherOne watcherOneX = new ManyWatcherOne(zkClient, path);
        ManyWatcherTwo watcherTwoX = new ManyWatcherTwo(zkClient, path);
        //注册监听，注意，这里两次exists方法的执行返回都是null，因为“X”节点还不存在
        try {
            zkClient.exists(path, watcherOneX);
            zkClient.exists(path, watcherTwoX);
            //创建"X"节点，为了简单起见，我们忽略权限问题。
            //并且创建一个临时节点，这样重复跑代码的时候，不用去server上手动删除)
            zkClient.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }
        //TODO 注意观察日志，根据原理我们猜测理想情况下ManyWatcherTwo和ManyWatcherOne都会被通知。
        //2、========================================================
        log.info("=================以下是第二个实验");
        path = "/Y";
        ManyWatcherOne watcherOneY = new ManyWatcherOne(zkClient, path);
        ManyWatcherTwo watcherTwoY = new ManyWatcherTwo(zkClient, path);
        //注册监听，注意，这里使用两次getData方法注册监听，"Y"节点目前并不存在
        try {
            zkClient.getData(path, watcherOneY, null);
            zkClient.getData(path, watcherTwoY, null);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        //TODO 注意观察日志，因为"Y"节点不存在，所以getData就会出现异常。watcherOneY、watcherTwoY的注册都不起任何作用。
        //然后我们在报了异常的情况下，创建"Y"节点，根据原理，不会有任何watcher响应"Y"节点的create事件
        try {
            zkClient.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }
        //下面这段代码可以忽略，是为了观察zk的原理。让守护线程保持不退出

        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                System.exit(-1);
            }
        }
    }


    /**
     * 默认监听
     */
    class ManyWatcherDefault implements Watcher {
        private Log logger = LogFactory.getLog(ManyWatcherDefault.class);

        @Override
        public void process(WatchedEvent watchedEvent) {
            // 获取客户端与server的连接状态
            Event.KeeperState keeperState = watchedEvent.getState();
            // 获取事件的类型
            Event.EventType eventType = watchedEvent.getType();
            logger.info("=========默认监听到None事件：keeperState = "
                    + keeperState + "  :  eventType = " + eventType);

        }
    }

    /**
     * 第一种watch
     */
    class ManyWatcherOne implements Watcher {
        private Log logger = LogFactory.getLog(ManyWatcherOne.class);
        private ZooKeeper zkClient;
        private String watcherPath;

        public ManyWatcherOne(ZooKeeper zkCilent, String watcherPath) {
            this.zkClient = zkCilent;
            this.watcherPath = watcherPath;
        }

        @Override
        public void process(WatchedEvent watchedEvent) {
            try {
                this.zkClient.exists(watcherPath, this);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            Event.KeeperState keeperState = watchedEvent.getState();
            Event.EventType eventType = watchedEvent.getType();
            logger.info("=========ManyWatcherOne监听到" + watcherPath + "地址发生事件："
                    + "keeperState = " + keeperState + "  :  eventType = " + eventType);
        }
    }

    /**
     * 第二种Warch
     */

    class ManyWatcherTwo implements Watcher {

        private Log logger = LogFactory.getLog(ManyWatcherTwo.class);
        private ZooKeeper zkClient;
        private String watcherPath;

        public ManyWatcherTwo(ZooKeeper zkClient, String watcherPath) {
            this.zkClient = zkClient;
            this.watcherPath = watcherPath;
        }

        @Override
        public void process(WatchedEvent watchedEvent) {
            try {
                this.zkClient.exists(watcherPath, this);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            Event.KeeperState keeperState = watchedEvent.getState();
            Event.EventType eventType = watchedEvent.getType();
            logger.info("=========ManyWatcherOne监听到" + watcherPath + "地址发生事件："
                    + "keeperState = " + keeperState + "  :  eventType = " + eventType);
        }


    }
}
