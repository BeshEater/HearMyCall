package com.besheater.hearmycall;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class AvatarImageChoser extends AppCompatActivity {

    public static final String AVATAR_IMAGE_NUM = "avatarImageNum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_image_choser);


        //Bind adapter to AvatarImageRecycler
        AvatarImagesAdapter adapter = new AvatarImagesAdapter(new UserData().getListOfAvatarImages());
        RecyclerView avatarImageRecycler = findViewById(R.id.avatar_image_recycler);
        avatarImageRecycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        avatarImageRecycler.setLayoutManager(layoutManager);

        //Set listener to adapter sot it send back to MainActivity
        adapter.setListener(new AvatarImagesAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.putExtra(AVATAR_IMAGE_NUM, position);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }


}
