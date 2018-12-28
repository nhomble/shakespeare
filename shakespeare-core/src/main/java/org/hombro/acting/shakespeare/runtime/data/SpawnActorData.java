package org.hombro.acting.shakespeare.runtime.data;

import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.messages.Message;

public class SpawnActorData {

    private final ActorReference parent;
    private final Class<?> target;

    public SpawnActorData(ActorReference parent, Class<?> target) {
        this.parent = parent;
        this.target = target;
    }

    public ActorReference getParent() {
        return parent;
    }

    public Class<?> getTarget() {
        return target;
    }
}
