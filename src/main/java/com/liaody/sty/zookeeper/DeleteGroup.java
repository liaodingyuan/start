package com.liaody.sty.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;

import java.util.List;

@Slf4j
public class DeleteGroup extends ConnectionWatcher {

    /**
     * 删除zk节点（包括其子节点）
     * @param groupName 节点名称
     */
    public void delete(String groupName){

        String path = "/" + groupName;
        // 获取节点下的所有子节点
        List<String> children = null;
        try {
            children = zk.getChildren(path, false);
            if (children == null || children.isEmpty()) {
                log.info("node has no child.");
                return;
            }
            // 先循环删除子节点。第二个参数是znode版本号，如果提供的版本号和znode版本号一致才会删除这个znode，这样可以检测出对znode的修改冲突。
            // 通过将版本号设置为-1，可以绕过这个版本检测机制，无论znode的版本号是什么，都会直接将其删除。
            for (String cNode : children) {
                zk.delete(path + "/" + cNode, -1);
            }
            // 删除节点
            zk.delete(path, -1);
        } catch (KeeperException | InterruptedException e) {
            log.error("删除节点失败，reason->{}",  e.getMessage());
        }
    }


}
