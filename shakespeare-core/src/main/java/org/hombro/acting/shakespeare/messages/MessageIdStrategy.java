package org.hombro.acting.shakespeare.messages;

@FunctionalInterface
public interface MessageIdStrategy {
    String next();
}
