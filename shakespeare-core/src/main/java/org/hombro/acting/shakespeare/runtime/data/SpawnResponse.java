package org.hombro.acting.shakespeare.runtime.data;

import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.messages.Message;
import org.hombro.acting.shakespeare.runtime.TheatreResponse;

public class SpawnResponse extends TheatreResponse {
    private final ActorReference reference;

    public SpawnResponse(ActorReference reference, Message message) {
        super(message);
        this.reference = reference;
    }

    public ActorReference getReference() {
        return reference;
    }
}
