package org.hombro.acting.shakespeare.example.helloworld;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.hombro.acting.shakespeare.Theatre;
import org.hombro.acting.shakespeare.messages.Message;
import org.hombro.acting.shakespeare.runtime.RootClientActor;
import org.slf4j.LoggerFactory;

public class HelloWorldExample {
    public static void main(String... args) throws InterruptedException {
        Logger root = (Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.ALL);
        Theatre theatre = Theatre.builder()
                .setName("example")
                .build();
        theatre.start();
        System.out.println(theatre.getActors());

        theatre.spawn(MyActor.class);

        Thread.sleep(1000);
        theatre.send(Message.builder()
                .setData("hello world")
                .setTo(RootClientActor.REFERENCE.append("test"))
                .build());
        System.out.println(theatre.getActors());

        theatre.shutdown();

    }
}
