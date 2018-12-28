package org.hombro.acting.shakespeare.example.murder;

import org.hombro.acting.shakespeare.Theatre;
import org.hombro.acting.shakespeare.runtime.RootClientActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KillExample {

    private static final Logger log = LoggerFactory.getLogger(KillExample.class);

    public static void main(String... args) throws InterruptedException {
        Theatre theatre = Theatre.builder()
                .setName("killing")
                .build()
                .start();
        theatre.spawn(OwnerActor.class);
        Thread.sleep(1_000);
        log.info("list={}", theatre.getActors());

        theatre.kill(RootClientActor.REFERENCE.append(OwnerActor.class));
        Thread.sleep(1_000);
        log.info("list={}", theatre.getActors());
        theatre.shutdown();
    }
}
