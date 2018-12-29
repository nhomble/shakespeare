package org.hombro.acting.shakespeare.example.spawnandwait;

import org.hombro.acting.shakespeare.Theatre;
import org.hombro.acting.shakespeare.messages.Message;
import org.hombro.acting.shakespeare.runtime.RootClientActor;
import org.hombro.acting.shakespeare.runtime.data.SpawnOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpawnWaitExample {

    private static final Logger log = LoggerFactory.getLogger(SpawnWaitExample.class);

    public static void main(String... args) {
        Theatre theatre = Theatre.builder()
                .setName("wait")
                .build()
                .start();

        theatre.spawn(WaitingActor.class, SpawnOptions.builder()
                .setAwaitConfirmation(true)
                .build());
        log.info("list={}", theatre.getActors());
        theatre.send(Message.builder()
                .setData("hello")
                .setTo(RootClientActor.REFERENCE.append(WaitingActor.class))
                .build());
        theatre.shutdown();
    }
}
