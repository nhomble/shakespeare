package org.hombro.acting.shakespeare.actors;

import org.hombro.acting.shakespeare.Theatre;
import org.hombro.acting.shakespeare.annotations.Data;
import org.hombro.acting.shakespeare.messages.Message;
import org.hombro.acting.shakespeare.runtime.ExceptionalActor;
import org.hombro.acting.shakespeare.runtime.TheatreOperations;
import org.hombro.acting.shakespeare.runtime.data.ExceptionData;
import org.hombro.acting.shakespeare.utils.AnnotationHelpers;
import org.hombro.acting.shakespeare.utils.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.hombro.acting.shakespeare.actors.ActorExecution.ActorMessageInvocation.forMethod;

public class ActorExecution {
    private static final Logger log = LoggerFactory.getLogger(ActorExecution.class);

    private final TheatreOperations theatreOperations;
    private final ActorReference reference;
    private final Collection<ActorMessageInvocation> handlers;
    private final ActorInitInvocation initInvocation;

    ActorExecution(TheatreOperations theatreOperations, ActorReference reference, Collection<ActorMessageInvocation> handlers, ActorInitInvocation initInvocation) {
        this.theatreOperations = theatreOperations;
        this.reference = reference;
        this.handlers = handlers;
        this.initInvocation = initInvocation;
    }

    public static ActorExecution executionFor(TheatreOperations theatreOperations, Class<?> actor, ActorReference reference) {
        return new ActorExecution(
                theatreOperations,
                reference,
                AnnotationHelpers.findEventHandlingMethods(actor).stream()
                        .map(m -> forMethod(m, reference.isFramework()))
                        .collect(Collectors.toList()),
                AnnotationHelpers.findInitActorMethod(actor)
                        .map(m -> ActorInitInvocation.forMethod(m, reference.isFramework()))
                        .orElse(ActorInitInvocation.defaultInvocation())
        );
    }

    public void handle(Object instance, Message message) {
        log.trace("handling {}", message);
        handlers.stream()
                .filter(h -> h.matches(message))
                .forEach(h -> h.invoke(instance, message, theatreOperations, reference));
    }

    public void init(Object instance) {
        initInvocation.invoke(instance, theatreOperations, reference);
    }

    static class DefaultArgProvider implements ArgProvider {

        @Override
        public Object apply(Message m, TheatreOperations t, ActorReference reference) {
            return null;
        }
    }

    static abstract class ActorInvocation {
        private final Method method;
        private final ArgProvider[] argList;

        protected ActorInvocation(Method method, ArgProvider[] argList) {
            this.method = method;
            this.argList = argList;
        }

        public void invoke(Object instance, TheatreOperations theatreOperations, ActorReference reference) {
            Object[] args = new Object[argList.length];
            for (int i = 0; i < args.length; i++) {
                args[i] = argList[i].apply(null, theatreOperations, reference);
            }
            try {
                method.invoke(instance, args);
            } catch (Throwable e) {
                theatreOperations.send(Message.builder()
                        .setData(new ExceptionData(e))
                        .setTo(ExceptionalActor.REFERENCE)
                        .build());
            }
        }

        public void invoke(Object instance, Message message, TheatreOperations theatreOperations, ActorReference reference) {
            Object[] args = new Object[argList.length];
            for (int i = 0; i < args.length; i++) {
                args[i] = argList[i].apply(message, theatreOperations, reference);
            }
            try {
                method.invoke(instance, args);
            } catch (Throwable e) {
                theatreOperations.send(Message.builder()
                        .setData(new ExceptionData(message, e))
                        .setTo(ExceptionalActor.REFERENCE)
                        .build());
            }
        }
    }

    static class ActorInitInvocation extends ActorInvocation {

        private ActorInitInvocation(Method method, ArgProvider[] argList) {
            super(method, argList);
        }

        public static ActorInitInvocation forMethod(Method method, boolean isFramework) {
            method.setAccessible(true);
            Parameter[] params = method.getParameters();
            ArgProvider[] argProviders = new ArgProvider[params.length];
            for (int i = 0; i < params.length; i++) {
                argProviders[i] = new DefaultArgProvider();
                Class<?> type = params[i].getType();
                if (Message.class.isAssignableFrom(type)) {
                    argProviders[i] = (m, t, r) -> m;
                } else if (Theatre.class.isAssignableFrom(type)) {
                    argProviders[i] = (m, t, r) -> Theatre.fromOperations(t);
                } else if (TheatreOperations.class.isAssignableFrom(type) && isFramework) {
                    argProviders[i] = (m, t, r) -> t;
                } else if (ActorReference.class.isAssignableFrom(type)) {
                    argProviders[i] = (m, t, r) -> r;
                }
            }
            return new ActorInitInvocation(method, argProviders);
        }

        public static ActorInitInvocation defaultInvocation() {
            return new DefaultActorInitInvocation();
        }
    }

    static class DefaultActorInitInvocation extends ActorInitInvocation {

        protected DefaultActorInitInvocation() {
            super(null, null);
        }

        @Override
        public void invoke(Object instance, TheatreOperations theatreOperations, ActorReference reference) {
            // do nothing
        }

        @Override
        public void invoke(Object instance, Message msg, TheatreOperations theatreOperations, ActorReference reference) {
            throw new UnsupportedOperationException();
        }
    }

    static class ActorMessageInvocation extends ActorInvocation {

        private final Class<?> type;
        private final boolean hasMessage;

        ActorMessageInvocation(Method method, Class<?> type, ArgProvider[] argList, boolean hasMessage) {
            super(method, argList);
            this.type = type;
            this.hasMessage = hasMessage;
        }

        public static ActorMessageInvocation forMethod(Method method, boolean isFramework) {
            method.setAccessible(true);
            Parameter[] params = method.getParameters();
            ArgProvider[] argProviders = new ArgProvider[params.length];
            Class<?> theType = null;
            boolean hasMessage = false;
            for (int i = 0; i < params.length; i++) {
                argProviders[i] = new DefaultArgProvider();
                Class<?> type = params[i].getType();
                if (Message.class.isAssignableFrom(type)) {
                    argProviders[i] = (m, t, r) -> m;
                    hasMessage = true;
                } else if (Theatre.class.isAssignableFrom(type)) {
                    argProviders[i] = (m, t, r) -> Theatre.fromOperations(t);
                } else if (TheatreOperations.class.isAssignableFrom(type) && isFramework) {
                    argProviders[i] = (m, t, r) -> t;
                } else if (ActorReference.class.isAssignableFrom(type)) {
                    argProviders[i] = (m, t, r) -> r;
                } else {
                    Annotation[] annotations = params[i].getAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof Data) {
                            theType = type;
                            argProviders[i] = (m, t, r) -> m.getData();
                            break;
                        }
                    }
                }
            }
            Promise.that(theType).when(!hasMessage).isNotNull();
            return new ActorMessageInvocation(method, theType, argProviders, hasMessage);
        }

        public boolean matches(Message message) {
            return hasMessage || type.isAssignableFrom(message.getData().getClass());
        }
    }
}
