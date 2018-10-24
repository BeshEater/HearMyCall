package com.besheater.hearmycall;

import java.util.ArrayList;
import java.util.List;

public class UserData {

    public static final String USER_DATA="userData";

    private List<AvatarImage> avatarImages;
    private String name;
    private AvatarImage avatarImage;
    boolean isVisible = true;
    private String chatChannel;

    public UserData() {
        setListOfImages();
        avatarImage = avatarImages.get(0);
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

    public void setAvatarImage(int position) {
        this.avatarImage = avatarImages.get(position);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getChatChannel() {
        return chatChannel;
    }

    public void setChatChannel(String chatChannel) {
        this.chatChannel = chatChannel;
    }

    public List<AvatarImage> getListOfAvatarImages() {
        return avatarImages;
    }

    private void setListOfImages() {
        List<AvatarImage> list = new ArrayList<>();

        list.add(new AvatarImage(0, R.drawable.avatar_image_large_0, R.drawable.avatar_image_marker_0));
        list.add(new AvatarImage(1, R.drawable.avatar_image_large_1, R.drawable.avatar_image_marker_1));
        list.add(new AvatarImage(2, R.drawable.avatar_image_large_2, R.drawable.avatar_image_marker_2));
        list.add(new AvatarImage(3, R.drawable.avatar_image_large_3, R.drawable.avatar_image_marker_3));
        list.add(new AvatarImage(4, R.drawable.avatar_image_large_4, R.drawable.avatar_image_marker_4));

        this.avatarImages = list;
    }
}
