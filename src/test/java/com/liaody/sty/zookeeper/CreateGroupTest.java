package com.liaody.sty.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class CreateGroupTest {


    private static String hosts = "192.168.147.140";
    private static String groupName = "myzoo";
    private CreateGroup createGroup;
    @Before
    public void init () throws IOException, InterruptedException {
      createGroup = new CreateGroup();
        createGroup.connection(hosts);
    }
    @After
    public void destory(){
        createGroup.close();
        createGroup = null;
        System.gc();
    }

    @Test
    public void testCreateGroup() throws KeeperException, InterruptedException {
        createGroup.createGroup(groupName);
    }


}
