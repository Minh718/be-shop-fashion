package com.shopro.shop1905.dtos;

public class Message {

    private String text;

    private String to;

    // No-arg constructor (Jackson needs this)
    public Message() {
    }

    // // Add a constructor if needed
    // public Message(String text, String to) {
    // this.text = text;
    // this.to = to;
    // }

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}