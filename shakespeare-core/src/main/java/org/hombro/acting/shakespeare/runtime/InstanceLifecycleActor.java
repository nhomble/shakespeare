package org.hombro.acting.shakespeare.runtime;

import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.Data;
import org.hombro.acting.shakespeare.annotations.OnMessage;
import org.hombro.acting.shakespeare.runtime.data.KillActorData;
import org.hombro.acting.shakespeare.runtime.data.SpawnActorData;
import org.hombro.acting.shakespeare.utils.AnnotationHelpers;

@Actor(name = "lifecycle")
public class InstanceLifecycleActor {

    public static final ActorReference REFERENCE = RootFrameworkActor.REFERENCE.append(AnnotationHelpers.determineActorName(InstanceLifecycleActor.class));

    @OnMessage
    void handle(@Data SpawnActorData data, TheatreOperations theatreOperations) {
        theatreOperations.spawn(data.getTarget(), data.getParent());
    }

    @OnMessage
    void handle(@Data KillActorData data, TheatreOperations theatreOperations){
        theatreOperations.kill(data.getReference());
    }
}
