package org.hombro.acting.shakespeare.runtime;

import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.Data;
import org.hombro.acting.shakespeare.annotations.OnMessage;
import org.hombro.acting.shakespeare.runtime.data.ShutdownData;
import org.hombro.acting.shakespeare.utils.AnnotationHelpers;

@Actor(name = "admin")
public class AdminActor {

    public static final ActorReference REFERENCE = RootFrameworkActor.REFERENCE.append(AnnotationHelpers.determineActorName(AdminActor.class));

    @OnMessage
    void handle(@Data ShutdownData shutdownData, TheatreOperations theatreOperations) {
        theatreOperations.shutdown();
    }
}
