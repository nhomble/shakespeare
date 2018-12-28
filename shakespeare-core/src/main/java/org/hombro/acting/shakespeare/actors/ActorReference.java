package org.hombro.acting.shakespeare.actors;

import org.hombro.acting.shakespeare.runtime.RootFrameworkActor;
import org.hombro.acting.shakespeare.utils.AnnotationHelpers;
import org.hombro.acting.shakespeare.utils.ToStringHelper;

import java.util.Objects;

public class ActorReference {
    private static final String DELIM = "/";
    private final String name;

    public ActorReference(String name) {
        this.name = name;
    }

    public static ActorReference offRoot(String name) {
        return new ActorReference(DELIM + name);
    }

    public ActorReference append(String next){
        return new ActorReference(name + DELIM + next);
    }

    public ActorReference append(Class<?> actor){
        return append(AnnotationHelpers.determineActorName(actor));
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorReference that = (ActorReference) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString(){
        return ToStringHelper.forClass(ActorReference.class)
                .with("name", name)
                .toString();
    }

    public boolean isFramework(){
        return name.startsWith(RootFrameworkActor.REFERENCE.getName());
    }
}
