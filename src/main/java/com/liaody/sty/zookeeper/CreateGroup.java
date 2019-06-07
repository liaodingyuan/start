package com.liaody.sty.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 创建zoo节点
 *
 */
@Slf4j
public class CreateGroup implements Watcher {
    /**
     * 设置会话超时
     */
    private static final int SESSION_TIMEOUT = 1000;
    /**
     * zk客户端
     */
    private ZooKeeper zk =null;
    /**
     * 同步计时器
     */
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 当zookeeper客户端的连接状态发生变更时，即KeeperState.Expired、KeeperState.Disconnected、KeeperState.SyncConnected、KeeperState.AuthFailed状态切换时，描述的事件类型为EventType.None
     * @param watchedEvent 监听事件
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        // 一旦客户端和服务器的某一个节点建立连接（注意，虽然集群有多个节点，但是客户端一次连接到一个节点就行了），
        // 并完成一次version、zxid的同步，这时的客户端和服务器的连接状态就是SyncConnected
        if(watchedEvent.getState()== Event.KeeperState.SyncConnected){
            log.info("zk syncConnected event");
            countDownLatch.countDown();
        }
        // zookeeper客户端进行连接认证失败时，发生该状态
        if(watchedEvent.getState() == Event.KeeperState.AuthFailed){
            log.info("zk auth fail event");
        }
        // 连接超时，客户端和服务器在ticktime的时间周期内，是要发送心跳通知的。这是租约协议的一个实现。客户端发送request，
        // 告诉服务器其上一个租约时间，服务器收到这个请求后，告诉客户端其下一个租约时间是哪个时间点。当客户端时间戳达到最后一个租约时间，
        // 而没有收到服务器发来的任何新租约时间，即认为自己下线（此后客户端会废弃这次连接，并试图重新建立连接）。这个过期状态就是Expired状态
        if(watchedEvent.getState() == Event.KeeperState.Expired){
            log.info("zk expired event");
        }
        // 就像上面那个状态所述，当客户端断开一个连接（可能是租约期满，也可能是客户端主动断开）这是客户端和服务器的连接就是Disconnected状态
        if(watchedEvent.getState() == Event.KeeperState.Disconnected){
            log.info("zk disconnected event");
        }
        if(watchedEvent.getState() == Event.KeeperState.SaslAuthenticated){
            log.info("zk SaslAuthenticated event");
        }
        // 连接只读事件
        if(watchedEvent.getState() == Event.KeeperState.ConnectedReadOnly){
            log.info("zk ConnectedReadOnly event");
        }

    }

    /**
     * 创建zk对象、当客户端连接上zookeeper时会执行procces里的countDownLatch.countDown
     * ，计数器会变为0，则await方法返回
     * @param hosts
     * @throws IOException
     * @throws InterruptedException
     */
    public void connection(String hosts) throws IOException, InterruptedException {
        zk = new ZooKeeper(hosts,SESSION_TIMEOUT,this);
        // 阻塞程序继续执行
        countDownLatch.await();
    }

    public void createGroup(String groupName) throws KeeperException, InterruptedException {
        String path = "/"+groupName;
        log.info("path->{}",path);
        String creatPath = zk.create(path,null,
                // 允许任何客户端对该node进行读写
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                // 持久化的node。get node 时，ephemeralOwner值不再是0，表示这个临时节点的版本号，如果是永久节点则其值为 0x0
                CreateMode.PERSISTENT_SEQUENTIAL);
      log.info("创建group完成");
    }

    /**
     * 关闭zk客户端
     */
    public void close(){
        if(zk!=null){
            try{
                zk.close();
            }catch (Exception e ){
                log.error("关闭zk客户端异常。");
            }finally {
                zk = null;
                System.gc();

            }
        }
    }

}
