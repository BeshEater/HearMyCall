package com.besheater.hearmycall;

public class AvatarImage {

    private int avatarImageNum;
    private int avatarImageLargeId;
    private int avatarImageMarkerId;

    public AvatarImage(int avatarImageNum, int avatarImageLargeId, int avatarImageMarkerId) {
        this.avatarImageNum = avatarImageNum;
        this.avatarImageLargeId = avatarImageLargeId;
        this.avatarImageMarkerId = avatarImageMarkerId;
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

}
