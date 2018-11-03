package com.besheater.hearmycall;

import android.os.Parcel;
import android.os.Parcelable;

public class AvatarImage implements Parcelable {
    public static final Parcelable.Creator<AvatarImage> CREATOR = new MyCreator();

    private int avatarImageNum;
    private int avatarImageLargeId;
    private int avatarImageMarkerId;

    public AvatarImage(int avatarImageNum, int avatarImageLargeId, int avatarImageMarkerId) {
        this.avatarImageNum = avatarImageNum;
        this.avatarImageLargeId = avatarImageLargeId;
        this.avatarImageMarkerId = avatarImageMarkerId;
    }

    public AvatarImage(Parcel source) {
        //Reconstruct from the parcel
        avatarImageNum = source.readInt();
        avatarImageLargeId = source.readInt();
        avatarImageMarkerId = source.readInt();

    }

    public int getAvatarImageNum() {
        return avatarImageNum;
    }

    public int getAvatarImageLargeId() {
        return avatarImageLargeId;
    }

    public int getAvatarImageMarkerId() {
        return avatarImageMarkerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(avatarImageNum);
        dest.writeInt(avatarImageLargeId);
        dest.writeInt(avatarImageMarkerId);

    }

    public static class MyCreator implements Parcelable.Creator<AvatarImage> {
        public AvatarImage createFromParcel(Parcel source) {
            return new AvatarImage(source);
        }
        public AvatarImage[] newArray(int size) {
            return new AvatarImage[size];
        }
    }
}
