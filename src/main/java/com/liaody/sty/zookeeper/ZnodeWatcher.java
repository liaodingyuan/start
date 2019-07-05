package com.liaody.sty.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.stereotype.Component;

import javax.swing.event.HyperlinkEvent;
import javax.validation.constraints.Size;

@Slf4j
@Component
public class ZnodeWatcher implements Watcher {

    /**
     *  监听znode事件：znode被创建、删除或者数据更新、子节点的创建与删除
     * @param watchedEvent 监听事件
     */
    @Override
    public void process(WatchedEvent watchedEvent) {

        if(watchedEvent.getType()== Event.EventType.NodeCreated){
            log.info("实例xxx上线");
        }
        if(watchedEvent.getType()== Event.EventType.NodeDeleted){
            log.info("实例xxx下线");
        }
        if(watchedEvent.getType()== Event.EventType.NodeChildrenChanged){
            log.info("集群xxx下实例有变化");
        }
        if(watchedEvent.getType()== Event.EventType.NodeDataChanged){
            log.info("实例xxx数据变化");
        }
    }
}
