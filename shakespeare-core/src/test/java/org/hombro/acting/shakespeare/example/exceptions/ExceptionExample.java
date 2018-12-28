package org.hombro.acting.shakespeare.example.exceptions;

import org.hombro.acting.shakespeare.Theatre;
import org.hombro.acting.shakespeare.messages.Message;
import org.hombro.acting.shakespeare.runtime.RootClientActor;

public class ExceptionExample {
    public static void main(String... args) throws InterruptedException {
        Theatre theatre = Theatre.builder()
                .setName("exceptions")
                .build()
                .start();

        theatre.spawn(FailingActor.class);
        Thread.sleep(1_000);

        theatre.send(Message.builder()
                .setTo(RootClientActor.REFERENCE.append(FailingActor.class))
                .setData("foo")
                .build());
        theatre.shutdown();
    }
}
