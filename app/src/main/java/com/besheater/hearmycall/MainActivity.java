package com.besheater.hearmycall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString("name");
            EditText text = findViewById(R.id.editText);
            text.setText(savedText);
        }

        int chosenImage = getIntent().getIntExtra("avatarImageId", R.drawable.emoji_slightly_smiling_face);
        ImageButton button = findViewById(R.id.imageButton);
        button.setImageResource(chosenImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            int chosenImage = data.getIntExtra("avatarImageId", R.drawable.emoji_slightly_smiling_face);
            ImageButton button = findViewById(R.id.imageButton);
            button.setImageResource(chosenImage);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        EditText text = findViewById(R.id.editText);
        savedInstanceState.putCharSequence("name", text.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onCallButtonClick(View view) {
        //Start map activity
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void onFaceButtonClick(View view) {
        Intent intent = new Intent(this, AvatarImageChoser.class);
        startActivityForResult(intent, 1);
    }
}
