package org.hombro.acting.shakespeare.runtime;

import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.utils.AnnotationHelpers;

@Actor(name = "client")
public class RootClientActor {
    public static final ActorReference REFERENCE = ActorReference.offRoot(AnnotationHelpers.determineActorName(RootClientActor.class));

}
