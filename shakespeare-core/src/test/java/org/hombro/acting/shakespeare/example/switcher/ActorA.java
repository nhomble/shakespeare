package org.hombro.acting.shakespeare.example.switcher;

import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.Data;
import org.hombro.acting.shakespeare.annotations.OnMessage;
import org.hombro.acting.shakespeare.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Actor
public class ActorA {

    private static final Logger log = LoggerFactory.getLogger(ActorA.class);
    private int count = 0;

    @OnMessage
    void handle(Message message) {
        count++;
        log.info("count={}", count);
    }
}
