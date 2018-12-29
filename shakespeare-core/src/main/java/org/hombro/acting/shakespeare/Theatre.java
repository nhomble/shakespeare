package org.hombro.acting.shakespeare;

import org.hombro.acting.shakespeare.actors.ActorInfo;
import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.messages.Message;
import org.hombro.acting.shakespeare.messages.MessageIdStrategy;
import org.hombro.acting.shakespeare.messages.UUIDMessageIdStrategy;
import org.hombro.acting.shakespeare.runtime.AdminActor;
import org.hombro.acting.shakespeare.runtime.InstanceLifecycleActor;
import org.hombro.acting.shakespeare.runtime.LocalTheatreOperations;
import org.hombro.acting.shakespeare.runtime.TheatreOperations;
import org.hombro.acting.shakespeare.runtime.data.*;
import org.hombro.acting.shakespeare.utils.Promise;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Theatre {

    private final TheatreOperations operations;

    private Theatre(TheatreOperations operations) {
        this.operations = operations;
    }

    public Theatre start() {
        operations.start();
        return this;
    }

    public Theatre shutdown() {
        Promise.that(operations.hasStarted()).isTrue();
        operations.send(Message.builder()
                .setTo(AdminActor.REFERENCE)
                .setData(new ShutdownData())
                .build());
        return this;
    }

    public SpawnResponse spawn(Class<?> actor) {
        return spawn(actor, SpawnOptions.builder().build());
    }

    public SpawnResponse spawn(Class<?> actor, SpawnOptions spawnOptions) {
        Promise.that(operations.hasStarted()).isTrue();
        Message msg = operations.send(Message.builder()
                .setData(new SpawnActorData(spawnOptions.getParent(), actor))
                .setTo(InstanceLifecycleActor.REFERENCE)
                .setConfirmAction(spawnOptions.shouldAwaitConfirmation())
                .build());
        return new SpawnResponse(spawnOptions.getParent().append(actor), msg);
    }

    public KillResponse kill(ActorReference reference) {
        Promise.that(operations.hasStarted()).isTrue();
        Message msg = operations.send(Message.builder()
                .setData(new KillActorData(reference))
                .setTo(InstanceLifecycleActor.REFERENCE)
                .build());
        return new KillResponse(msg);
    }

    public Collection<ActorInfo> getActors() {
        Promise.that(operations.hasStarted()).isTrue();
        return operations.allActors();
    }

    public Message send(Message message) {
        Promise.that(operations.hasStarted()).isTrue();
        return operations.send(message);
    }

    public static Theatre fromOperations(TheatreOperations operations) {
        return new Theatre(operations);
    }

    public static TheatreBuilder builder() {
        return new TheatreBuilder();
    }

    public static class TheatreBuilder {
        private String name;
        private MessageIdStrategy messageIdStrategy = new UUIDMessageIdStrategy();
        private int nThreads = 10;

        public TheatreBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public TheatreBuilder setMessageIdStrategy(MessageIdStrategy messageIdStrategy) {
            this.messageIdStrategy = messageIdStrategy;
            return this;
        }

        public TheatreBuilder setnThreads(int nThreads) {
            this.nThreads = nThreads;
            return this;
        }

        public Theatre build() {
            ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
            return new Theatre(
                    new LocalTheatreOperations(name, messageIdStrategy, executorService)
            );
        }
    }
}
