package org.hombro.acting.shakespeare.example.murder;

import org.hombro.acting.shakespeare.Theatre;
import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.OnInit;
import org.hombro.acting.shakespeare.runtime.data.SpawnOptions;

@Actor
public class OwnerActor {

    @OnInit
    void init(Theatre theatre, ActorReference self) {
        theatre.spawn(ChildActor.class, SpawnOptions.builder()
                .setParent(self)
                .build());
    }
}
