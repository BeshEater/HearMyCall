package com.besheater.hearmycall;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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


public class MapFragment extends Fragment implements OnMapReadyCallback{

    private UserData userData;
    private GoogleMap mMap;
    private Marker userMarker;
    private Marker userAnimCircle;
    private int maxAnimRad = 100;
    private int currentAnimRad = 5;
    private boolean isAnimRunning = false;

    public MapFragment() {
        // Required empty public constructor
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        //Set UserData object reference
        MainActivity mainActivity = (MainActivity) getActivity();
        userData = mainActivity.getUserData();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Register listeners
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

                //Check if max amount of new lines is exceeded
                if (count > maxLineQty-1) {
                    //Delete last character
                    s.delete(s.length()-1, s.length());
                }

            }
        });

        //Buttons for calling
        final ImageView doneBtn = view.findViewById(R.id.call_message_done_button);
        final ImageView offBtn = view.findViewById(R.id.call_message_off_button);
        final ImageView callBtn = view.findViewById(R.id.call_button);
        //For call button
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Message field activation
                callMsg.setVisibility(View.VISIBLE);
                callMsg.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(callMsg, InputMethodManager.SHOW_IMPLICIT);

                //Change buttons
                //Hide Call button
                callBtn.setVisibility(View.GONE);
                //Show done and off buttons
                doneBtn.setVisibility(View.VISIBLE);
                offBtn.setVisibility(View.VISIBLE);

            }
        });

        //For off button
        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide keyboard and message field
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(callMsg.getWindowToken(), 0);
                callMsg.setVisibility(View.INVISIBLE);

                //Hide done and off buttons
                doneBtn.setVisibility(View.GONE);
                offBtn.setVisibility(View.GONE);

                //Show call button
                callBtn.setVisibility(View.VISIBLE);

            }
        });

        //For done button
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide keyboard and message field
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(callMsg.getWindowToken(), 0);
                callMsg.setVisibility(View.INVISIBLE);

                //Hide done and off buttons
                doneBtn.setVisibility(View.GONE);
                offBtn.setVisibility(View.GONE);

                //Show call button
                callBtn.setVisibility(View.VISIBLE);

                //Set call message
                String callMsgText = callMsg.getText().toString();
                userData.setCallMessage(callMsgText);

                //Show info window on user marker
                userMarker.setSnippet(userData.getCallMessage());
                userMarker.showInfoWindow();

                //Run animation if not already running
                if(!isAnimRunning) {
                    runAnimation();
                }
            }
        });


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Save GoogleMap object
        mMap = googleMap;
        //Set InfoWindowAdapter for all markers
        mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());

        doSomething();
    }

    public void updateFromUserData() {
        if (mMap != null && getContext() != null) {
            doSomething();
        }
    }

    public void doSomething() {
        //Clear all map
        mMap.clear();

        //Set User Interface for map
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setCompassEnabled(true);

        // Add a marker in Kostanay
        LatLng Kostanay = new LatLng(53.213619, 63.627738);
        //Create marker image from xml
        View markerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.marker_simple, null);
        //Set marker name
        TextView markerName = markerView.findViewById(R.id.marker_name);
        markerName.setText(userData.getName());
        //Set marker image
        ImageView markerImage = markerView.findViewById(R.id.marker_image);
        markerImage.setImageResource(userData.getAvatarImage().getAvatarImageLargeId());
        //Create Bitmap from view
        Bitmap bitmap = getBitmapFromView(markerView);
        BitmapDescriptor image = BitmapDescriptorFactory.fromBitmap(bitmap);
        //Add custom marker to map
        MarkerOptions markerOptions = new MarkerOptions()
                .position(Kostanay)
                .title("Call message")
                .snippet(userData.getCallMessage())
                .icon(image)
                .anchor(0.5f, 0.5f);
        Marker marker = mMap.addMarker(markerOptions);
        //Save marker to class variable
        userMarker = marker;
        userMarker.setZIndex(2.0f);

        //Add anim circle marker
        int radius = 1;
        int thickness = 0;
        int color = getResources().getColor(R.color.colorPrimary);

        image = getCircleBitmapDescr(radius, thickness, color);
        markerOptions = new MarkerOptions()
                .position(Kostanay)
                .icon(image)
                .anchor(0.5f, 0.5f);
        userAnimCircle = mMap.addMarker(markerOptions);
        userAnimCircle.setZIndex(0f);


        //Add second custom marker to map
        //Creating custom marker image
        image = BitmapDescriptorFactory.fromResource(AppData.getAvatarImageAtPos(8).getAvatarImageMarkerId());

        LatLng KostanayShift = new LatLng(53.214, 63.628);
        markerOptions = new MarkerOptions()
                .position(KostanayShift)
                .icon(image)
                .anchor(0.5f, 0.5f);
        mMap.addMarker(markerOptions);

        //Move camera to coordinates
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Kostanay));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Kostanay));


    }

    private void runAnimation() {
        //Set running
        isAnimRunning = true;
        //Set color
        final int circleColor = getResources().getColor(R.color.colorPrimary);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isAnimRunning) {
                    //Reset if maxed;
                    if (currentAnimRad > maxAnimRad) {
                        currentAnimRad = 5;
                    } else {
                        currentAnimRad = currentAnimRad + 8;
                    }
                    //draw circle
                    int radius = currentAnimRad;
                    int thickness = 5;
                    int color = circleColor;

                    BitmapDescriptor icon = getCircleBitmapDescr(radius, thickness, color);
                    userAnimCircle.setIcon(icon);
                    handler.postDelayed(this, 40);
                }
            }
        });
    }

    private BitmapDescriptor getCircleBitmapDescr(int radius, int thickness, int color) {
        //Creating coordinates for drawing
        int x = 0;
        int y = 0;
        int width = radius * 2;
        int height = radius * 2;

        //Objects for drawing
        ShapeDrawable mDrawable = new ShapeDrawable(new OvalShape());
        Bitmap bitmap= Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        //Draw big circle first
        mDrawable.getPaint().setColor(color);
        mDrawable.setBounds(x, y, width, height);
        mDrawable.draw(canvas);

        //Draw small inner circle that will cut clear inside first
        mDrawable.getPaint().setColor(Color.TRANSPARENT);
        mDrawable.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mDrawable.setBounds(thickness,thickness,width-thickness,height-thickness);
        mDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            //If marker has not have snippet than show nothing
            if (marker.getSnippet() == null) {
                return null;
            } else {
                //Inflate View from xml
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
