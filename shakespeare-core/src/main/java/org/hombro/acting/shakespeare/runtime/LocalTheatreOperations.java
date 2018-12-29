package org.hombro.acting.shakespeare.runtime;


import org.hombro.acting.shakespeare.actors.ActorExecution;
import org.hombro.acting.shakespeare.actors.ActorInfo;
import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.messages.Message;
import org.hombro.acting.shakespeare.messages.MessageIdStrategy;
import org.hombro.acting.shakespeare.utils.AnnotationHelpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hombro.acting.shakespeare.actors.ActorExecution.executionFor;
import static org.hombro.acting.shakespeare.messages.Message.DEFERRED;

public class LocalTheatreOperations extends TheatreOperations {

    private static final Logger log = LoggerFactory.getLogger(LocalTheatreOperations.class);

    private final String name;
    private final ActorReference root;

    private final MessageIdStrategy messageIdStrategy;
    private final ConcurrentMap<ActorReference, Future<?>> runningTasks;
    private final ConcurrentMap<ActorReference, RegisteredActor> registry;
    private final ConcurrentMap<Message, Boolean> blockingMessages;
    private final ExecutorService executorService;

    private boolean hasStarted = false;

    // caches
    private final Map<Class<?>, String> actorNames = new ConcurrentHashMap<>();

    public LocalTheatreOperations(String name, MessageIdStrategy messageIdStrategy, ExecutorService executorService) {
        this.name = name;
        root = ActorReference.offRoot(name);
        this.messageIdStrategy = messageIdStrategy;
        this.executorService = executorService;
        registry = new ConcurrentHashMap<>();
        runningTasks = new ConcurrentHashMap<>();
        blockingMessages = new ConcurrentHashMap<>();
    }

    private String actorName(Class<?> clazz) {
        return actorNames.computeIfAbsent(clazz, AnnotationHelpers::determineActorName);
    }

    static class RegisteredActor {

        private final Object instance;
        private final String name;
        private final ActorReference parent;
        private final List<ActorReference> children;
        private final Queue<Message> inbox;
        private final ActorExecution execution;

        RegisteredActor(Object instance, String name, ActorReference parent, ActorExecution execution) {
            this.instance = instance;
            this.name = name;
            this.parent = parent;
            this.execution = execution;
            this.children = Collections.synchronizedList(new ArrayList<>());
            this.inbox = new ConcurrentLinkedQueue<>();

            if (execution != null) // TODO hacky
                execution.init(this.instance);
        }

        RegisteredActor receive(Message message) {
            inbox.offer(message);
            return this;
        }

        RegisteredActor adopt(ActorReference child) {
            children.add(child);
            return this;
        }

        void handle() {
            Message message = inbox.poll();
            execution.handle(instance, message);
            LocalTheatreOperations local = (LocalTheatreOperations) execution.getTheatreOperations();
            local.processedMessage(message);
        }
    }

    private void processedMessage(Message message) {
        blockingMessages.put(message, true);
        Set<Map.Entry<Message, Boolean>> s = blockingMessages.entrySet();
        s.stream().filter(Map.Entry::getValue).forEach(e -> blockingMessages.remove(e.getKey()));
    }

    private void expectMessage(Message message) {
        blockingMessages.put(message, false);
    }

    private boolean wasProcessed(Message message) {
        return blockingMessages.get(message);
    }

    @Override
    public Message send(Message message) {
        if (DEFERRED.equals(message.getMessageId())) {
            message = Message.builder(message)
                    .setMessageId(messageIdStrategy.next())
                    .build();
        }
        log.trace("Send {}", message);
        registry.get(message.getSendTo()).receive(message);
        if (message.shouldConfirmAction()) {
            expectMessage(message);
            while (!wasProcessed(message)) {
                // just spin
            }
        }
        return message;
    }

    @Override
    public Collection<ActorInfo> allActors() {
        return registry.values().stream()
                .map(register -> new ActorInfo(register.name, Optional.ofNullable(register.parent).map(ActorReference::getName).orElse("")))
                .collect(Collectors.toList());
    }

    @Override
    public void start() {
        log.trace("Inserting root actors");
        Stream.of(
                RootClientActor.class,
                RootFrameworkActor.class
        ).forEach(c -> {
            ActorReference reference = ActorReference.offRoot(actorName(c));
            // TODO may need to change null later
            try {
                registry.put(reference, new RegisteredActor(
                        c.newInstance(),
                        actorName(c),
                        null,
                        null
                ));
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        Stream.of(
                InstanceLifecycleActor.class,
                AdminActor.class,
                ExceptionalActor.class
        ).forEach(c -> spawn(c, RootFrameworkActor.REFERENCE));

        Thread t = new Thread(() -> {
            while (true) {
                if (runningTasks.isEmpty())
                    shutdown();
            }
        });
        try {
            t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        hasStarted = true;
    }

    @Override
    public boolean hasStarted() {
        return hasStarted;
    }

    @Override
    public void kill(ActorReference actor) {
        runningTasks.get(actor).cancel(false);
        runningTasks.remove(actor);

        registry.get(actor).children.forEach(this::kill);
        registry.remove(actor);
    }

    @Override
    public void shutdown() {
        runningTasks.values().forEach(t -> t.cancel(true));
        executorService.shutdown();
        registry.clear();
        runningTasks.clear();
    }

    @Override
    public ActorReference spawn(Class<?> child, ActorReference parent) {
        log.debug("Spawning child={} parent={}", child, parent);
        ActorReference next = parent.append(actorName(child));
        try {
            RegisteredActor registeredActor = new RegisteredActor(child.newInstance(), actorName(child), parent, executionFor(this, child, next));
            registry.put(next, registeredActor);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        registry.get(parent).adopt(next);

        runningTasks.put(next, executorService.submit(() -> {
            while (true) {
                if (!registry.get(next).inbox.isEmpty()) {
                    log.trace("Message needs to be handled {} {}", registry.get(next).inbox.peek(), next);
                    registry.get(next).handle();
                }
            }
        }));
        return next;
    }
}
