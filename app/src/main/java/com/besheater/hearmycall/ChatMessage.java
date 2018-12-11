package com.besheater.hearmycall;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatMessage implements Parcelable {
    public static final Parcelable.Creator<ChatMessage> CREATOR = new MyCreator();

    private User user;
    private String text;
    private long time;

    public ChatMessage(User user, String text, long time) {
        this.user = user;
        this.text = text;
        this.time = time;
    }

    public ChatMessage(Parcel source) {
        // Reconstruct from the parcel
        user = source.readParcelable(null);
        text = source.readString();
        time = source.readLong();
    }

    public User getUser() {
        return user;
    }

    public String getAuthor() {
        return user.name;
    }

    public String getText() {
        return text;
    }

    public long getTime() {
        return time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, 0);
        dest.writeString(text);
        dest.writeLong(time);
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
