package org.hombro.acting.shakespeare.actors;

import org.hombro.acting.shakespeare.messages.Message;
import org.hombro.acting.shakespeare.runtime.TheatreOperations;

public interface ArgProvider {
    Object apply(Message m, TheatreOperations t, ActorReference self);
}