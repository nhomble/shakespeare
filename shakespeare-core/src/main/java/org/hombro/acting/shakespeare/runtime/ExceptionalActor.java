package org.hombro.acting.shakespeare.runtime;

import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.Data;
import org.hombro.acting.shakespeare.annotations.OnMessage;
import org.hombro.acting.shakespeare.runtime.data.ExceptionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Actor(name = "exceptions")
public class ExceptionalActor {

    private static final Logger log = LoggerFactory.getLogger(ExceptionalActor.class);

    public static final ActorReference REFERENCE = RootFrameworkActor.REFERENCE.append(ExceptionalActor.class);

    @OnMessage
    void handle(@Data ExceptionData exceptionData) {
        log.error(String.format("%s", exceptionData.getMessage().toString()), exceptionData.getThrowable());
    }
}
