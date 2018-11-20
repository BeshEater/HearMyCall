package com.besheater.hearmycall;

public class User {
    public final int id;
    public final String name;
    public final double latitude;
    public final double longitude;
    public final int avatarImageNum;
    public final String callMessage;

    public User(int id, String name, double latitude, double longitude,
                int avatarImageNum, String callMessage) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.avatarImageNum = avatarImageNum;
        this.callMessage = callMessage;
    }
}
