package com.besheater.hearmycall;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public static final Parcelable.Creator<User> CREATOR = new MyCreator();
    public final int id;
    public final String name;
    public final double latitude;
    public final double longitude;
    public final int avatarImageNum;
    public final String callMessage;
    public final int[] connectedUsersId;
    public final long time;

    public User(int id, String name, double latitude, double longitude,
                int avatarImageNum, String callMessage, int[] connectedUsersId,
                long time) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.avatarImageNum = avatarImageNum;
        this.callMessage = callMessage;
        this.connectedUsersId = connectedUsersId;
        this.time = time;
    }

    public User(Parcel source) {
        // Reconstruct from the parcel
        id = source.readInt();
        name = source.readString();
        latitude = source.readDouble();
        longitude = source.readDouble();
        avatarImageNum = source.readInt();
        callMessage = source.readString();
        connectedUsersId = source.createIntArray();
        time = source.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(avatarImageNum);
        dest.writeString(callMessage);
        dest.writeIntArray(connectedUsersId);
        dest.writeLong(time);
    }

    public static class MyCreator implements Creator<User> {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }
        public User[] newArray(int size) {
            return new User[size];
        }
    }
}
