package org.hombro.acting.shakespeare.example.exceptions;

import org.hombro.acting.shakespeare.Theatre;
import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.OnInit;
import org.hombro.acting.shakespeare.annotations.OnMessage;
import org.hombro.acting.shakespeare.messages.Message;

@Actor
public class FailingActor {

    @OnInit
    void init(Theatre theatre){
        theatre.spawn(InitFailingActor.class);
    }

    @OnMessage
    void handle(Message message){
        throw new RuntimeException("fail message");
    }
}
