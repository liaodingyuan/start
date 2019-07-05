package com.liaody.sty.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Size;

/**
 * @author yuanhaha
 */
@Service
@Slf4j
public class TimeQueryService extends Thread {

    @Override
    public void run() {

        // 模拟kafka消费者轮询
        while (true) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("service TimeQueryService is  running");
        }
    }
}
