package org.hombro.acting.shakespeare.runtime.data;

import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.runtime.RootClientActor;
import org.hombro.acting.shakespeare.utils.Promise;

public class SpawnOptions {
    private final ActorReference parent;
    private final boolean awaitConfirmation;

    private SpawnOptions(ActorReference parent, boolean awaitConfirmation) {
        this.parent = parent;
        this.awaitConfirmation = awaitConfirmation;
    }

    public ActorReference getParent() {
        return parent;
    }

    public boolean shouldAwaitConfirmation(){
        return awaitConfirmation;
    }

    public static SpawnOptionsBuilder builder() {
        return new SpawnOptionsBuilder();
    }

    public static class SpawnOptionsBuilder {
        private ActorReference parent = RootClientActor.REFERENCE;
        private boolean awaitConfirmation = false;

        public ActorReference getParent() {
            return parent;
        }

        public boolean shouldAwaitConfirmation() {
            return awaitConfirmation;
        }

        public SpawnOptionsBuilder setAwaitConfirmation(boolean awaitConfirmation) {
            this.awaitConfirmation = awaitConfirmation;
            return this;
        }

        public SpawnOptionsBuilder setParent(ActorReference parent) {
            this.parent = parent;
            return this;
        }

        public SpawnOptions build() {
            Promise.that(parent).isNotNull();
            return new SpawnOptions(parent, awaitConfirmation);
        }
    }
}
