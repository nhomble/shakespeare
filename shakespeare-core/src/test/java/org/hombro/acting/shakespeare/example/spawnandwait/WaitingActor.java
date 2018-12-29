package org.hombro.acting.shakespeare.example.spawnandwait;

import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.Data;
import org.hombro.acting.shakespeare.annotations.OnInit;
import org.hombro.acting.shakespeare.annotations.OnMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Actor
public class WaitingActor {

    private static final Logger log = LoggerFactory.getLogger(WaitingActor.class);

    @OnInit
    void init() throws InterruptedException {
        log.info("start init");
        Thread.sleep(1_000);
        log.info("end init");
    }

    @OnMessage
    void handle(@Data String msg) {
        log.info("message={}", msg);
    }
}
