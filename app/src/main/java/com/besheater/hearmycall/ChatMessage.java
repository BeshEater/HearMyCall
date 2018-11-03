package com.besheater.hearmycall;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class ChatMessage implements Parcelable {
    public static final Parcelable.Creator<ChatMessage> CREATOR = new MyCreator();

    private String author;
    private String messageText;
    private Date messageTime;
    private boolean isUserMessage;
    private boolean isReceived;

    public ChatMessage(String author, String messageText, Date messageTime,
                       boolean isUserMessage, boolean isReceived) {
        this.author = author;
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.isUserMessage = isUserMessage;
        this.isReceived = isReceived;
    }

    public ChatMessage(Parcel source) {
        //Reconstruct from the parcel
        author = source.readString();
        messageText = source.readString();
        messageTime = new Date(source.readLong());
        isUserMessage = (Boolean) source.readValue(null);
        isReceived = (Boolean) source.readValue(null);
    }

    public boolean isUserMessage() {
        return isUserMessage;
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

    public void setReceived(boolean received) {
        isReceived = received;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(messageText);
        dest.writeLong(messageTime.getTime());
        dest.writeValue(isUserMessage);
        dest.writeValue(isReceived);

    }

    public static class MyCreator implements Parcelable.Creator<ChatMessage> {
        public ChatMessage createFromParcel(Parcel source) {
            return new ChatMessage(source);
        }
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    }


}
