package org.hombro.acting.shakespeare.example.add;

import org.hombro.acting.shakespeare.Theatre;
import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.Data;
import org.hombro.acting.shakespeare.annotations.OnInit;
import org.hombro.acting.shakespeare.annotations.OnMessage;
import org.hombro.acting.shakespeare.messages.Message;
import org.hombro.acting.shakespeare.runtime.data.SpawnOptions;

@Actor(name = "summing")
public class SummingActor {

    private ActorReference printer;

    @OnInit
    void init(Theatre theatre, ActorReference self) {
        printer = theatre.spawn(PrintingActor.class, SpawnOptions.builder()
                .setParent(self)
                .build()).getReference();
    }

    @OnMessage
    void handle(@Data AddData data, Theatre operations) {
        operations.send(Message.builder()
                .setData(data.getA() + data.getB())
                .setTo(printer)
                .build());
    }
}
