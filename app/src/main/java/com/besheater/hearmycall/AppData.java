package com.besheater.hearmycall;

import java.util.ArrayList;
import java.util.List;

public class AppData {
    private static List<AvatarImage> avatarImages;

    static {
        List<AvatarImage> list = new ArrayList<>();

        list.add(new AvatarImage(0, R.drawable.avatar_image_large_0, R.drawable.avatar_image_marker_0));
        list.add(new AvatarImage(1, R.drawable.avatar_image_large_1, R.drawable.avatar_image_marker_1));
        list.add(new AvatarImage(2, R.drawable.avatar_image_large_2, R.drawable.avatar_image_marker_2));
        list.add(new AvatarImage(3, R.drawable.avatar_image_large_3, R.drawable.avatar_image_marker_3));
        list.add(new AvatarImage(4, R.drawable.avatar_image_large_4, R.drawable.avatar_image_marker_4));
        list.add(new AvatarImage(5, R.drawable.avatar_image_large_5, R.drawable.avatar_image_marker_5));
        list.add(new AvatarImage(6, R.drawable.avatar_image_large_6, R.drawable.avatar_image_marker_6));
        list.add(new AvatarImage(7, R.drawable.avatar_image_large_7, R.drawable.avatar_image_marker_7));
        list.add(new AvatarImage(8, R.drawable.avatar_image_large_8, R.drawable.avatar_image_marker_8));
        list.add(new AvatarImage(9, R.drawable.avatar_image_large_9, R.drawable.avatar_image_marker_9));
        list.add(new AvatarImage(10, R.drawable.avatar_image_large_10, R.drawable.avatar_image_marker_10));
        list.add(new AvatarImage(11, R.drawable.avatar_image_large_11, R.drawable.avatar_image_marker_11));
        list.add(new AvatarImage(12, R.drawable.avatar_image_large_12, R.drawable.avatar_image_marker_12));
        list.add(new AvatarImage(13, R.drawable.avatar_image_large_13, R.drawable.avatar_image_marker_13));
        list.add(new AvatarImage(14, R.drawable.avatar_image_large_14, R.drawable.avatar_image_marker_14));
        list.add(new AvatarImage(15, R.drawable.avatar_image_large_15, R.drawable.avatar_image_marker_15));
        list.add(new AvatarImage(16, R.drawable.avatar_image_large_16, R.drawable.avatar_image_marker_16));
        list.add(new AvatarImage(17, R.drawable.avatar_image_large_17, R.drawable.avatar_image_marker_17));
        list.add(new AvatarImage(18, R.drawable.avatar_image_large_18, R.drawable.avatar_image_marker_18));
        list.add(new AvatarImage(19, R.drawable.avatar_image_large_19, R.drawable.avatar_image_marker_19));
        list.add(new AvatarImage(20, R.drawable.avatar_image_large_20, R.drawable.avatar_image_marker_20));

        avatarImages = list;
    }

    public static List<AvatarImage> getListOfAvatarImages() {
        return avatarImages;
    }

    public static AvatarImage getAvatarImageAtNum(int position) {
        return avatarImages.get(position);
    }
}
