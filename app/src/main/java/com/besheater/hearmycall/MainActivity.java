package com.besheater.hearmycall;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static final String USER_DATA = "userData";
    private UserData userData;
    private SettingsFragment settingsFragment;
    private MapFragment mapFragment;
    private ChatFragment chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recreate previously saved state
        if(savedInstanceState != null) {
            //Recreate userData
            userData = savedInstanceState.getParcelable(USER_DATA);
        } else {
            //Create new userData object
            userData = new UserData();
            //Fill with test values
            userData.getChatHandler().fillWithTestValues();
        }


        //Attach SectionsPagerAdapter to the ViewPager
        final ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        //Attach ViewPager to the TabLayout
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        //Update tab when it's selected
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        if (settingsFragment != null) settingsFragment.updateFromUserData();
                    case 1:
                        if (mapFragment != null) mapFragment.updateFromUserData();
                    case 2:
                        if (chatFragment != null) chatFragment.updateFromUserData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public UserData getUserData() {
        return this.userData;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //Save UserData
        savedInstanceState.putParcelable(USER_DATA, userData);

        //Call supper
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
                    SettingsFragment settingsFragment = new SettingsFragment();
                    return settingsFragment;
                case 1 :
                    MapFragment mapFragment = new MapFragment();
                    return mapFragment;
                case 2:
                    ChatFragment chatFragment = new ChatFragment();
                    return chatFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        // Here we can finally safely save a reference to the created
        // Fragment, no matter where it came from (either getItem() or
        // FragmentManger). Simply save the returned Fragment from
        // super.instantiateItem() into an appropriate reference depending
        // on the ViewPager position.
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    settingsFragment = (SettingsFragment) createdFragment;
                    break;
                case 1:
                    mapFragment = (MapFragment) createdFragment;
                    break;
                case 2:
                    chatFragment = (ChatFragment) createdFragment;
            }
            return createdFragment;
        }
    }
}
