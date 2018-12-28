package org.hombro.acting.shakespeare.actors;

import org.hombro.acting.shakespeare.utils.ToStringHelper;

import java.util.Objects;

public final class ActorInfo {
    private final String name;
    private final String parent;

    public ActorInfo(String name, String parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }

    public String getFullName() {
        return new ActorReference(parent).append(name).getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorInfo actorInfo = (ActorInfo) o;
        return Objects.equals(name, actorInfo.name) &&
                Objects.equals(parent, actorInfo.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parent);
    }

    @Override
    public String toString() {
        return ToStringHelper.forClass(ActorInfo.class)
                .with("name", name)
                .with("parent", parent)
                .toString();
    }
}
