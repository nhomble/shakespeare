package org.hombro.acting.shakespeare.runtime;

import org.hombro.acting.shakespeare.messages.Message;

public abstract class TheatreResponse {
    private final Message message;

    protected TheatreResponse(Message message) {
        this.message = message;
    }

    public Message getMessage(){
        return message;
    }
}
