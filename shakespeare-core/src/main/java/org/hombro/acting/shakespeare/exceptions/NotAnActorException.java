package org.hombro.acting.shakespeare.exceptions;

public class NotAnActorException extends RuntimeException {
    public NotAnActorException(Class<?> clazz){
        super("Provided class=" + clazz.getName() + " was not annotated as an actor!");
    }
}
