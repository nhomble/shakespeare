package org.hombro.acting.shakespeare.exceptions;

public class ExistingActorException extends RuntimeException {
    public ExistingActorException(Class<?> clazz) {
        super("The current theatre already contains the actor=" + clazz.getName() + " if you want to have multiple instances of the actor, then consider an actor group");
    }
}
