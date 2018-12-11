package com.besheater.hearmycall;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class UserData implements Parcelable {
    public static final Parcelable.Creator<UserData> CREATOR = new MyCreator();

    private String name;
    private AvatarImage avatarImage;
    private boolean isVisible;
    private boolean isReceiveNotification;
    private int notificationRadius; // position
    private int chatChannelPos;
    private ChatHandler chatHandler;
    private String callMessage;
    private int id;
    private String uniqId;
    private double latitude;
    private double longitude;
    private int[] connectedUsersId;

    public UserData() {
        name = "";
        avatarImage = AppData.getAvatarImage(0);
        isVisible = true;
        isReceiveNotification = true;
        notificationRadius = 2;
        chatChannelPos = 0;
        chatHandler = new ChatHandler(this);
        uniqId = "testID_12345";
        latitude = 50.201035;
        longitude = -28.440404;
    }

    public UserData(Parcel source) {
        // Reconstruct from the parcel
        name = source.readString();
        avatarImage = source.readParcelable(null);
        isVisible = (Boolean) source.readValue(null);
        isReceiveNotification = (Boolean) source.readValue(null);
        notificationRadius = source.readInt();
        chatChannelPos = source.readInt();
        chatHandler = source.readParcelable(null);
        callMessage = source.readString();
        id = source.readInt();
        uniqId = source.readString();
        latitude = source.readDouble();
        longitude = source.readDouble();
    }

    public int[] getConnectedUsersId() {
        return connectedUsersId;
    }

    public void setConnectedUsersId(int[] connectedUsersId) {
        this.connectedUsersId = connectedUsersId;
    }

    public User getThisUserObject() {
        User thisUser = new User(id,
                name,
                latitude,
                longitude,
                avatarImage.getAvatarImageNum(),
                callMessage,
                connectedUsersId,
                new Date().getTime());
        return thisUser;
    }

    public String getUniqId() {
        return uniqId;
    }

    public void setUniqId(String uniqId) {
        this.uniqId = uniqId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCallMessage() {
        return callMessage;
    }

    public void setCallMessage(String callMessage) {
        this.callMessage = callMessage;
    }

    public ChatHandler getChatHandler() {
        return chatHandler;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AvatarImage getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(AvatarImage avatarImage) {
        this.avatarImage = avatarImage;
    }

    public int getNotificationRadius() {
        return notificationRadius;
    }

    public void setNotificationRadius(int notificationRadius) {
        this.notificationRadius = notificationRadius;
    }

    public boolean isReceiveNotification() {
        return isReceiveNotification;
    }

    public void setReceiveNotification(boolean receiveNotification) {
        isReceiveNotification = receiveNotification;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }


    public int getChatChannelPos() {
        return chatChannelPos;
    }

    public void setChatChannelPos(int chatChannelPos) {
        this.chatChannelPos = chatChannelPos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(avatarImage, 0);
        dest.writeValue(isVisible);
        dest.writeValue(isReceiveNotification);
        dest.writeInt(notificationRadius);
        dest.writeInt(chatChannelPos);
        dest.writeParcelable(chatHandler, 0);
        dest.writeString(callMessage);
        dest.writeInt(id);
        dest.writeString(uniqId);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static class MyCreator implements Parcelable.Creator<UserData> {
        public UserData createFromParcel(Parcel source) {
            return new UserData(source);
        }
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    }

}
