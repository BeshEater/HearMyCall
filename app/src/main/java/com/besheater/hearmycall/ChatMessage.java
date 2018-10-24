package com.besheater.hearmycall;

import java.util.Date;

public class ChatMessage {

    private String author;
    private String messageText;
    private Date messageTime;
    private boolean isReceived;

    public ChatMessage(String author, String messageText, Date messageTime) {
        this.author = author;
        this.messageText = messageText;
        this.messageTime = messageTime;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessageText() {
        return messageText;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public boolean isReceived() {
        return isReceived;
    }


}
