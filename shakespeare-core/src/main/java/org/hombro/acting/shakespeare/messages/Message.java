package org.hombro.acting.shakespeare.messages;

import org.hombro.acting.shakespeare.actors.ActorReference;
import org.hombro.acting.shakespeare.utils.Promise;
import org.hombro.acting.shakespeare.utils.ToStringHelper;

import java.util.Objects;

public class Message {
    // when the creator doesn't have an opinion on the id
    // it is up sendTo the Operations sendTo assign an id
    public static final String DEFERRED = "";

    private final ActorReference sendTo;
    private final String messageId;
    private final Object data;
    private final Class<?> clazz;

    public Message(ActorReference sendTo, String messageId, Object data, Class<?> clazz) {
        this.sendTo = sendTo;
        this.messageId = messageId;
        this.data = data;
        this.clazz = clazz;
    }

    public Message(ActorReference sendTo, Object data, Class<?> clazz) {
        this(sendTo, DEFERRED, data, clazz);
    }

    public ActorReference getSendTo() {
        return sendTo;
    }

    public String getMessageId() {
        return messageId;
    }

    public Object getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(sendTo, message.sendTo) &&
                Objects.equals(messageId, message.messageId) &&
                Objects.equals(data, message.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sendTo, messageId, data);
    }

    public static MessageBuilder builder(){
        return new MessageBuilder();
    }

    public static MessageBuilder builder(Message message){
        return new MessageBuilder(message);
    }

    public static class MessageBuilder {
        private ActorReference to;
        private Object data;
        private String messageId = DEFERRED;

        public MessageBuilder() {
        }

        public MessageBuilder(Message message) {
            to = message.getSendTo();
            data = message.getData();
            messageId = message.getMessageId();
        }

        public MessageBuilder setTo(ActorReference to) {
            this.to = to;
            return this;
        }

        public MessageBuilder setData(Object data) {
            this.data = data;
            return this;
        }

        public MessageBuilder setMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public Message build() {
            Promise.that(data).isNotNull();
            return new Message(to, messageId, data, data.getClass());
        }
    }

    @Override
    public String toString(){
        return ToStringHelper.forClass(Message.class)
                .with("messageId", messageId)
                .with("sendTo", sendTo)
                .with("data", data)
                .toString();
    }
}