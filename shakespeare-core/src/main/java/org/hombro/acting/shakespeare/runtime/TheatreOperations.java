package org.hombro.acting.shakespeare.runtime;

import org.hombro.acting.shakespeare.actors.ActorInfo;
import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.messages.Message;

import java.util.Collection;

/**
 * Users cannot control the theatre beyond some thread configuration and new actors.
 * Configuration is driven at construction.
 * <p>
 * This serves as the main entry API for clients of the framework.
 */
public abstract class TheatreOperations {

    /**
     * Send a message to the system and return the MessageId for that message
     * @param message
     * @return
     */
    public abstract Message send(Message message);

    /**
     * List all of the known actors to this theatre
     *
     * @return
     */
    public abstract Collection<ActorInfo> allActors();

    public abstract void start();

    public abstract boolean hasStarted();

    /*
     * System operations the framework depends on that shouldn't be invoked directly by
     * clients
     *
     */

    /**
     * Create a new actor
     * @param child
     * @param parent
     */
    protected abstract ActorReference spawn(Class<?> child, ActorReference parent);

    /**
     * Kill the given actor, we will recursively kill orphans
     * @param actor
     */
    protected abstract void kill(ActorReference actor);

    /**
     * Shutdown the actual theatre
     */
    protected abstract void shutdown();
}
