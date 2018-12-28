package org.hombro.acting.shakespeare.example.add;

import org.hombro.acting.shakespeare.Theatre;
import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.messages.Message;
import org.hombro.acting.shakespeare.runtime.RootClientActor;

public class SummingExample {
    public static void main(String... args) throws InterruptedException {
        Theatre theatre = Theatre.builder()
                .setName("summing")
                .build();
        theatre.start();
        theatre.spawn(SummingActor.class);
        Thread.sleep(1_000);
        theatre.send(Message.builder()
                .setTo(RootClientActor.REFERENCE.append("summing"))
                .setData(new AddData(1, 2))
                .build());
        theatre.shutdown();
    }
}
