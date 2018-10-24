package com.besheater.hearmycall;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private UserData userData = new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Attach SectionsPagerAdapter to the ViewPager
        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));


        //Attach ViewPager to the TabLayout
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    public UserData getUserData() {
        return this.userData;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //Acquiring data from Intent
                int avatarImageNum = intent.getIntExtra(AvatarImageChoser.AVATAR_IMAGE_NUM, 0);
                userData.setAvatarImage(avatarImageNum);

                //Display data
                ImageView imageView = findViewById(R.id.avatar_image);
                imageView.setImageResource(userData.getAvatarImage().getAvatarImageLargeId());

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int i) {
            switch (i) {
                case 0:
                    return getResources().getText(R.string.settings_tab);
                case 1:
                    return getResources().getText(R.string.map_tab);
                case 2:
                    return getResources().getText(R.string.chat_tab);

            }
            return null;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new SettingsFragment();
                case 1 :
                    return new MapFragment();
                case 2:
                    return new ChatFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
