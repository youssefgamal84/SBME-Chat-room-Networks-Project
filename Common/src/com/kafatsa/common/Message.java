package com.kafatsa.common;

import java.io.Serializable;

public class Message<T extends Serializable> implements Serializable {
    private T payload;

    public Message() {
        super();
    }

    public Message(T data) {
        super();
        setPayload(data);
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T aPayload) {
        payload = aPayload;
    }
}

