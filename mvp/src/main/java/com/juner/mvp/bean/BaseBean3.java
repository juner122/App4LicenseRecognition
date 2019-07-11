package com.juner.mvp.bean;

import java.util.List;

public class BaseBean3<T> {

    Message message;

    List<T> cardsinfo;

    public class Message {

        String status;
        String value;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<T> getCardsinfo() {
        return cardsinfo;
    }

    public void setCardsinfo(List<T> cardsinfo) {
        this.cardsinfo = cardsinfo;
    }
}
