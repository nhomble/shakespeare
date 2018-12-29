package org.hombro.acting.shakespeare.utils;

import org.hombro.acting.shakespeare.annotations.Actor;
import org.hombro.acting.shakespeare.annotations.OnInit;
import org.hombro.acting.shakespeare.annotations.OnMessage;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class AnnotationHelpers {
    private AnnotationHelpers() {
        super();
    }

    public static String determineActorName(Class<?> clazz) {
        Actor a = clazz.getAnnotation(Actor.class);
        Promise.that(a).isNotNull();
        return a.name().isEmpty() ? clazz.getCanonicalName() : a.name();
    }

    public static List<Method> findEventHandlingMethods(Class<?> clazz) {
        // TODO handle inheritance
        return Stream.of(clazz.getDeclaredMethods())
                .filter(m -> m.getAnnotation(OnMessage.class) != null)
                .collect(Collectors.toList());
    }

    public static Optional<Method> findInitActorMethod(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredMethods())
                .filter(m -> m.getAnnotation(OnInit.class) != null)
                .findFirst();
    }
}
