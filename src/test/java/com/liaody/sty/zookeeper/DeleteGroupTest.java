package com.liaody.sty.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class DeleteGroupTest {
    private static final String HOSTS = "192.168.147.143";
    private static final String groupName = "myzoo0000000002";
    
    private DeleteGroup deleteGroup = null;
    
    @Before
    public void init() throws IOException, InterruptedException {
        deleteGroup = new DeleteGroup();
        deleteGroup.connection(HOSTS);
    }
    
    @Test
    public void testDelete() throws IOException, InterruptedException, KeeperException {
        deleteGroup.delete(groupName);
    }
    
    @After
    public void destroy() throws InterruptedException {
        if(null != deleteGroup){
            try {
                deleteGroup.close();
            } finally{
                deleteGroup = null;
                System.gc();
            }
        }
    }
}