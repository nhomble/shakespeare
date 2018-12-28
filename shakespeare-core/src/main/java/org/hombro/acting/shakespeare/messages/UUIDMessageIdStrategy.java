package org.hombro.acting.shakespeare.messages;

import java.util.UUID;

public class UUIDMessageIdStrategy implements MessageIdStrategy {
    @Override
    public String next() {
        return UUID.randomUUID().toString();
    }
}
