package com.besheater.hearmycall;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback{

    private UserData userData;
    private GoogleMap mMap;
    private User thisUser;
    private Marker userMarker;
    private LatLng currentUserLocation = new LatLng(53.213619, 63.627738);
    private LocationListener listener;
    private LocationManager locManager;
    private int maxAnimRad = 100;
    private int currentAnimRad = 5;
    private boolean isAnimRunning = false;
    public static final float USER_MARKER_Z_INDEX = 2.0f;
    public static final float ANIM_MARKER_Z_INDEX = 1.0f;
    private List<User> allUsers = new ArrayList<>();
    private List<Marker> allAnimCircleMarkers = new ArrayList<>();

    public MapFragment() {
        //  Required empty public constructor
    }

    public static Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        view.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //  Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Set UserData object reference
        MainActivity mainActivity = (MainActivity) getActivity();
        userData = mainActivity.getUserData();

        //  Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Setup periodic location update
        setupUserLocationUpdates();

        // Register listeners
        final EditText callMsg = view.findViewById(R.id.call_message);
        callMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int maxLineQty = 10;
                String newLine = "\n";
                String text = s.toString();
                int count = text.length() - text.replace(newLine, "").length();

                // Check if max amount of new lines is exceeded
                if (count > maxLineQty-1) {
                    // Delete last character
                    s.delete(s.length()-1, s.length());
                }

            }
        });

        // Buttons for calling
        final ImageView doneBtn = view.findViewById(R.id.call_message_done_button);
        final ImageView offBtn = view.findViewById(R.id.call_message_off_button);
        final ImageView callBtn = view.findViewById(R.id.call_button);
        // For call button
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Message field activation
                callMsg.setVisibility(View.VISIBLE);
                callMsg.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(callMsg, InputMethodManager.SHOW_IMPLICIT);

                // Change buttons
                // Hide Call button
                callBtn.setVisibility(View.GONE);
                // Show done and off buttons
                doneBtn.setVisibility(View.VISIBLE);
                offBtn.setVisibility(View.VISIBLE);

            }
        });

        // For off button
        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide keyboard and message field
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(callMsg.getWindowToken(), 0);
                callMsg.setVisibility(View.INVISIBLE);

                // Hide done and off buttons
                doneBtn.setVisibility(View.GONE);
                offBtn.setVisibility(View.GONE);

                // Show call button
                callBtn.setVisibility(View.VISIBLE);

            }
        });

        // For done button
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide keyboard and message field
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(callMsg.getWindowToken(), 0);
                callMsg.setVisibility(View.INVISIBLE);

                // Hide done and off buttons
                doneBtn.setVisibility(View.GONE);
                offBtn.setVisibility(View.GONE);

                // Show call button
                callBtn.setVisibility(View.VISIBLE);

                // Set call message
                String callMsgText = callMsg.getText().toString();
                userData.setCallMessage(callMsgText);

                // Redraw map
                redrawMap();

                // Show info window on user marker
                userMarker.showInfoWindow();

            }
        });


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Save GoogleMap object
        mMap = googleMap;
        // Set InfoWindowAdapter for all markers
        mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());

        redrawMap();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Stop animation process
        isAnimRunning = false;
    }

    public void updateFromUserData() {
        if (mMap != null && getContext() != null) {
            redrawMap();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(locManager != null && listener != null && getContext() != null) {
            if (ContextCompat.checkSelfPermission(
                    getContext(),
                    MainActivity.REQUESTED_LOCATION_PERMISSION)
                    == PackageManager.PERMISSION_GRANTED) {
                locManager.removeUpdates(listener);
            }
            locManager = null;
            listener = null;
        }
    }

    private void populateUsersArray() {
        // Add this user first
        User thisUser = new User(1, userData.getName(), currentUserLocation.latitude,
                currentUserLocation.longitude, userData.getAvatarImage().getAvatarImageNum(),
                userData.getCallMessage());
        allUsers.add(thisUser);
        // Save thisUser to variable
        this.thisUser = thisUser;
        // This should be method for connection to server but for now
        // just populate with some fake users
        allUsers.add(new User(2, "Jack", currentUserLocation.latitude + 0.0003,
                currentUserLocation.longitude, 4, null));
        allUsers.add(new User(3,"Margaret", currentUserLocation.latitude,
                currentUserLocation.longitude + 0.0004, 7, null));
        allUsers.add(new User(4, "Omar", currentUserLocation.latitude - 0.0003,
                currentUserLocation.longitude - 0.0003, 10,
                "Who want to watch football and drink some beers?\n" +
                        "Come to me if you want, bring some chips with you though"));
    }

    private void placeMarkersOnMap() {
        for (User user : allUsers) {
            if (user.equals(thisUser)) {
                userMarker = placeMarkerOnMap(user);
            } else {
                placeMarkerOnMap(user);
            }
        }
    }

    private Marker placeMarkerOnMap(User user) {

        // Get user location
        LatLng userLoc = new LatLng(user.latitude, user.longitude);
        // Create marker image from xml
        View markerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.marker_simple, null);
        // Set marker name
        TextView markerName = markerView.findViewById(R.id.marker_name);
        markerName.setText(user.name);
        // Ser marker Call message
        String callMsg = user.callMessage;
        // Set marker image
        ImageView markerImage = markerView.findViewById(R.id.marker_image);
        AvatarImage avatarImage = AppData.getAvatarImageAtNum(user.avatarImageNum);
        markerImage.setImageResource(avatarImage.getAvatarImageLargeId());
        // Create Bitmap from view
        Bitmap bitmap = getBitmapFromView(markerView);
        BitmapDescriptor image = BitmapDescriptorFactory.fromBitmap(bitmap);
        // Add custom marker on map
        MarkerOptions markerOptions = new MarkerOptions()
                .position(userLoc)
                .snippet(callMsg)
                .icon(image)
                .anchor(0.5f, 0.5f);
        Marker marker = mMap.addMarker(markerOptions);
        // Set marker Z-index
        marker.setZIndex(USER_MARKER_Z_INDEX);
        // Add original User object as tag
        marker.setTag(user);

        // If user is calling create animation marker under this marker
        if (callMsg != null) {
            placeAnimMarkerOnMap(marker);
        }

        return marker;
    }

    private void placeAnimMarkerOnMap(Marker marker) {
        // Draw circle Bitmap image
        BitmapDescriptor image = getCircleBitmapDescr(currentAnimRad);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(marker.getPosition())
                .icon(image)
                .anchor(0.5f, 0.5f);
        Marker animMarker = mMap.addMarker(markerOptions);
        animMarker.setZIndex(ANIM_MARKER_Z_INDEX);
        // Add to animated Markers list
        allAnimCircleMarkers.add(animMarker);
    }


    private void setupUserLocationUpdates() {

        // Setup location listener
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                currentUserLocation = new LatLng(latitude, longitude);
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
        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Check if permission for location granted
        if (ContextCompat.checkSelfPermission(
                getContext(),
                MainActivity.REQUESTED_LOCATION_PERMISSION)
                == PackageManager.PERMISSION_GRANTED) {
            // Get best location provider
            String provider = locManager.getBestProvider(new Criteria(), true);
            if (provider != null) {
                // Set current location to last known
                Location loc = locManager.getLastKnownLocation(provider);
                currentUserLocation = new LatLng(loc.getLatitude(), loc.getLongitude());

                // Register listener for location change
                locManager.requestLocationUpdates(provider, 1000, 5, listener);
            }
        } else {
            // Show that location permission not granted
            Toast toast = Toast.makeText(
                    getContext(),
                    "Doesn't have location permission",
                    Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void redrawMap() {
        // Clear all map
        mMap.clear();

        // Clear all User references
        allUsers.clear();
        // Clear all marker references
        allAnimCircleMarkers.clear();

        // Set User Interface for map
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setCompassEnabled(true);

        // Populate Users
        populateUsersArray();

        // Place markers on map
        placeMarkersOnMap();

        // Run animation if not already running
        if(!isAnimRunning) {
            runAnimation();
        }

        // Move camera to user coordinates
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserLocation));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserLocation));

    }


    private void runAnimation() {
        // Set running
        isAnimRunning = true;

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isAnimRunning) {
                    // Change radius of circle
                    // Reset if maxed;
                    if (currentAnimRad > maxAnimRad) {
                        currentAnimRad = 5;
                    } else {
                        // Increase a little
                        currentAnimRad = currentAnimRad + 8;
                    }
                    // Create new circle Bitmap image with new radius
                    BitmapDescriptor icon = getCircleBitmapDescr(currentAnimRad);
                    // Update radius on all animated Markers
                    if (allAnimCircleMarkers != null && !allAnimCircleMarkers.isEmpty()) {
                        for (Marker animCircleMarker : allAnimCircleMarkers) {
                            animCircleMarker.setIcon(icon);
                        }
                    }
                    handler.postDelayed(this, 40);
                }
            }
        });
    }

    private BitmapDescriptor getCircleBitmapDescr(int radius) {
        // Animated circle settings
        int thickness = 5;
        int color = getResources().getColor(R.color.colorPrimary);
        // Creating coordinates for drawing
        int x = 0;
        int y = 0;
        int width = radius * 2;
        int height = radius * 2;

        // Objects for drawing
        ShapeDrawable mDrawable = new ShapeDrawable(new OvalShape());
        Bitmap bitmap= Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw big circle first
        mDrawable.getPaint().setColor(color);
        mDrawable.setBounds(x, y, width, height);
        mDrawable.draw(canvas);

        // Draw small inner circle that will cut clear inside first
        mDrawable.getPaint().setColor(Color.TRANSPARENT);
        mDrawable.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mDrawable.setBounds(thickness,thickness,width-thickness,height-thickness);
        mDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            // If marker has not have snippet than show nothing
            if (marker.getSnippet() == null) {
                return null;
            } else {
                // Inflate View from xml
                View view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.call_message_info_window, null);
                TextView callMsg = view.findViewById(R.id.call_message);
                callMsg.setText(marker.getSnippet());

                return view;
            }
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
}
