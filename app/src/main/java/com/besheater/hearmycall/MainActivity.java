package com.besheater.hearmycall;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String REQUESTED_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 783;
    private final int LOC_NOT_GRANTED_NOTIFICATION_ID = 9023;
    public static final String USER_DATA = "userData";
    private UserData userData;
    private SettingsFragment settingsFragment;
    private MapFragment mapFragment;
    private ChatFragment chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Recreate previously saved state
        if(savedInstanceState != null) {
            // Recreate userData
            userData = savedInstanceState.getParcelable(USER_DATA);
        } else {
            // Create new userData object
            userData = new UserData();
            // Fill with test values
            userData.getChatHandler().fillWithTestValues();
        }


        // Attach SectionsPagerAdapter to the ViewPager
        final ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        // Attach ViewPager to the TabLayout
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        // Request location permission
        requestLocPermission();

        // Update tab when it's selected
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

    public void requestLocPermission() {
        // Requested permission
        String reqPerm = REQUESTED_LOCATION_PERMISSION;

        // Check if permission already granted
        if(ContextCompat.checkSelfPermission(this, reqPerm)
                != PackageManager.PERMISSION_GRANTED) {
            // If not granted request it
            ActivityCompat.requestPermissions(this,
                    new String[] {reqPerm},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // If already granted show quick info
            Toast toast = Toast.makeText(this,
                    "Location permission granted",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            // Check location permission request result
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If permission granted show quick info
                    Toast toast = Toast.makeText(this,
                            "Location permission granted",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    // If permission not granted show notification
                    // that app need location permission
                    notifyThatNeedLocPermission();
                }
        }
    }

    private void notifyThatNeedLocPermission() {
        // Configure notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.permission_denied))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{0, 500})
                .setAutoCancel(true);

        // Create action when clicked on notification
        Intent actionIntent = new Intent(this, MainActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(
                this,
                0,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(actionPendingIntent);

        // Build notification
        Notification notification = builder.build();

        // Issue the notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(LOC_NOT_GRANTED_NOTIFICATION_ID, notification);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UserData
        savedInstanceState.putParcelable(USER_DATA, userData);

        // Call supper
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

        //  Here we can finally safely save a reference to the created
        //  Fragment, no matter where it came from (either getItem() or
        //  FragmentManger). Simply save the returned Fragment from
        //  super.instantiateItem() into an appropriate reference depending
        //  on the ViewPager position.
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            //  save the appropriate reference depending on position
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
