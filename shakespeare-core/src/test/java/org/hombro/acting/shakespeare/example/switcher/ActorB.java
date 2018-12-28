package org.hombro.acting.shakespeare.example.switcher;

import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.OnMessage;
import org.hombro.acting.shakespeare.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Actor
public class ActorB {

    private static final Logger log = LoggerFactory.getLogger(ActorB.class);

    private int counter = 0;

    @OnMessage
    void handle(Message message){
        counter++;
        log.info("count={}", counter);
    }
}
