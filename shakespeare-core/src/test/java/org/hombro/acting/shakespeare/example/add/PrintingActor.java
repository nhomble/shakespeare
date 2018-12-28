package org.hombro.acting.shakespeare.example.add;

import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.Data;
import org.hombro.acting.shakespeare.annotations.OnMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Actor
public class PrintingActor {

    private static final Logger log = LoggerFactory.getLogger(PrintingActor.class);

    @OnMessage
    void handle(@Data Object object) {
        log.info("{}", object);
    }
}
