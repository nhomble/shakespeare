package org.hombro.acting.shakespeare.example.switcher;

import org.hombro.acting.shakespeare.Theatre;
import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.OnInit;
import org.hombro.acting.shakespeare.annotations.OnMessage;
import org.hombro.acting.shakespeare.messages.Message;

@Actor
public class SwitchActor {

    private int counter = 0;
    private ActorReference a, b;

    @OnInit
    void init(Theatre theatre, ActorReference self) {
        a = theatre.spawn(ActorA.class, self).getReference();
        b = theatre.spawn(ActorB.class, self).getReference();
    }

    @OnMessage
    void handle(Message message, Theatre theatre) {
        if (counter % 2 == 0)
            theatre.send(Message.builder(message).setTo(a).build());
        else
            theatre.send(Message.builder(message).setTo(b).build());
        counter++;
    }
}
