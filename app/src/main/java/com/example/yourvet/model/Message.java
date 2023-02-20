package com.example.yourvet.model;

import java.util.UUID;

public class Message {
    private String senderId;
    private String reciverId;
    private String text;

    public Message(String senderId, String reciverId, String text) {
        this.senderId = senderId;
        this.reciverId = reciverId;
        this.text = text;
//        this.id= UUID.randomUUID().toString();
    }

    public Message() {
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/

    @Override
    public String toString() {
        return "Message{" +
                "senderId='" + senderId + '\'' +
                ", reciverId='" + reciverId + '\'' +
                ", text='" + text + '\'' +

                '}';
    }
}
