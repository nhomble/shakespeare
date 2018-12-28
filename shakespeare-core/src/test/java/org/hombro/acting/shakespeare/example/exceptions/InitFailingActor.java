package org.hombro.acting.shakespeare.example.exceptions;

import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.OnInit;

@Actor
public class InitFailingActor {

    @OnInit
    void fail() {
        throw new RuntimeException("no init");
    }
}
