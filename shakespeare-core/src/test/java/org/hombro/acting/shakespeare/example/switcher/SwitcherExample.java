package org.hombro.acting.shakespeare.example.switcher;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.hombro.acting.shakespeare.Theatre;
import org.hombro.acting.shakespeare.messages.Message;
import org.hombro.acting.shakespeare.runtime.RootClientActor;
import org.slf4j.LoggerFactory;

public class SwitcherExample {
    public static void main(String... args) throws InterruptedException {
        Logger root = (Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.ALL);
        Theatre theatre = Theatre.builder()
                .setName("switcher")
                .build()
                .start();
        theatre.spawn(SwitchActor.class);
        Thread.sleep(1_000);

        for (int i = 0; i < 200; i++)
            theatre.send(Message.builder()
                    .setTo(RootClientActor.REFERENCE.append(SwitchActor.class))
                    .setData("ping")
                    .build());
        theatre.shutdown();
    }
}
