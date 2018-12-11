package com.besheater.hearmycall;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
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
    public static final String USER_DATA = "userData";
    private final int LOCATION_PERMISSION_REQUEST_CODE = 783;
    private final int LOC_NOT_GRANTED_NOTIFICATION_ID = 9023;
    private LocationListener listener;
    private LocationManager locManager;
    private UserData userData;
    private SettingsFragment settingsFragment;
    private MapFragment mapFragment;
    private ChatFragment chatFragment;
    private WebServerHandler webServerHandler;
    private boolean bound = false;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Save service reference for later use
            WebServerHandler.WebServerBinder webServerBinder =
                    (WebServerHandler.WebServerBinder) service;
            webServerHandler = webServerBinder.getWebServerHandlerService();
            bound = true;

            // Start requesting server for information
            webServerHandler.setupRepeatedRequestsInBackground(userData);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            webServerHandler.stopPeriodicUsersUpdates();
            webServerHandler.stopPeriodicMessagesUpdates();
            bound = false;
        }
    };

    public WebServerHandler getWebServerHandler() {
        return webServerHandler;
    }

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
        }

        // Setup periodic location update
        setupUserLocationUpdates();

        // bind service for web connections
        Intent intent = new Intent(this, WebServerHandler.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unbound service
        if (bound) {
            unbindService(connection);
            bound = false;
        }

        // Stop location updates

        if(locManager != null && listener != null) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    MainActivity.REQUESTED_LOCATION_PERMISSION)
                    == PackageManager.PERMISSION_GRANTED) {
                locManager.removeUpdates(listener);
            }
            locManager = null;
            listener = null;
        }
    }

    private void setupUserLocationUpdates() {

        // Setup location listener
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                userData.setLatitude(latitude);
                userData.setLongitude(longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        // Setup location manager
        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Check if permission for location granted
        if (ContextCompat.checkSelfPermission(
                this,
                MainActivity.REQUESTED_LOCATION_PERMISSION)
                == PackageManager.PERMISSION_GRANTED) {
            // Get best location provider
            String provider = locManager.getBestProvider(new Criteria(), true);
            if (provider != null) {
                // Set current location to last known
                Location loc = locManager.getLastKnownLocation(provider);
                // if location exist then update user data
                if (loc != null) {
                    userData.setLatitude(loc.getLatitude());
                    userData.setLongitude(loc.getLongitude());
                }

                // Register listener for location change
                locManager.requestLocationUpdates(provider, 1000, 5, listener);
            }
        } else {
            // Show that location permission not granted
            Toast toast = Toast.makeText(
                    this,
                    "Doesn't have location permission",
                    Toast.LENGTH_LONG);
            toast.show();
        }
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
