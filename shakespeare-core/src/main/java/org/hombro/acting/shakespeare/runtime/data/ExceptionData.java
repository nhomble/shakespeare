package org.hombro.acting.shakespeare.runtime.data;

import org.hombro.acting.shakespeare.messages.Message;

public class ExceptionData {
    private final Message message;
    private final Throwable throwable;

    public ExceptionData(Message message, Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
    }

    public ExceptionData(Throwable throwable){
        this(null, throwable);
    }

    public Message getMessage(){
        return message;
    }

    public Throwable getThrowable(){
        return throwable;
    }
}
