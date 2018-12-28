package org.hombro.acting.shakespeare.example.helloworld;

import org.hombro.acting.shakespeare.Theatre;
import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.OnMessage;
import org.hombro.acting.shakespeare.messages.Message;
import org.hombro.acting.shakespeare.runtime.TheatreOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Actor(name = "test")
public class MyActor {

    private static final Logger log = LoggerFactory.getLogger(MyActor.class);

    @OnMessage
    void handle(Message message){
        log.info("Just logging {}", message);
    }
}
