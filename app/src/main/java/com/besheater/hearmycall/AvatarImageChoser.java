package com.besheater.hearmycall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AvatarImageChoser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_image_choser);
    }


    public void onFaceButtonClick(View view) {
        //Default imageId
        int imageId = R.drawable.emoji_slightly_smiling_face;

        //Find user chosen resource for button image
        String imageName = view.getTag().toString();
        if (imageName.equals("emoji_dizzy_face")) imageId = R.drawable.emoji_dizzy_face;
        if (imageName.equals("emoji_slightly_smiling_face"))
            imageId = R.drawable.emoji_slightly_smiling_face;
        if (imageName.equals("emoji_face_with_thermometer"))
            imageId = R.drawable.emoji_face_with_thermometer;
        if (imageName.equals("emoji_heart_eyes")) imageId = R.drawable.emoji_heart_eyes;
        if (imageName.equals("emoji_loudly_crying_face"))
            imageId = R.drawable.emoji_loudly_crying_face;
        if (imageName.equals("emoji_nerd_with_glasses"))
            imageId = R.drawable.emoji_nerd_with_glasses;
        if (imageName.equals("emoji_smiling_face_with_blushed_cheeks"))
            imageId = R.drawable.emoji_smiling_face_with_blushed_cheeks;
        if (imageName.equals("emoji_smiling_with_eyes_opened"))
            imageId = R.drawable.emoji_smiling_with_eyes_opened;
        if (imageName.equals("emoji_tongue_out_with_winking_eye"))
            imageId = R.drawable.emoji_tongue_out_with_winking_eye;
        if (imageName.equals("emoji_upside_down_face")) imageId = R.drawable.emoji_upside_down_face;


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("avatarImageId", imageId);
        setResult(1, intent);
        finish();
    }

}
