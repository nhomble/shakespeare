package org.hombro.acting.shakespeare.runtime.data;

import org.hombro.acting.shakespeare.actors.ActorReference;

public class KillActorData {
    private final ActorReference reference;

    public KillActorData(ActorReference reference) {
        this.reference = reference;
    }

    public ActorReference getReference(){
        return reference;
    }
}
