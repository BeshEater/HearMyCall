package com.besheater.hearmycall;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap mMap;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Set User Interface for map
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setCompassEnabled(true);

        // Add a marker in Kostanay
        LatLng Kostanay = new LatLng(53.213619, 63.627738);
        //Creating custom marker image
        BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.drawable.avatar_image_marker_1);
        //Add custom marker to map
        MarkerOptions markerOptions = new MarkerOptions()
                .position(Kostanay)
                .title("BeInZONe")
                .snippet("Android god for YOU!")
                .icon(image)
                .anchor(0.5f, 0.5f);
        Marker marker = mMap.addMarker(markerOptions);
        //Show marker info Window
        marker.showInfoWindow();

        //Add second custom marker to map
        LatLng KostanayShift = new LatLng(53.214, 63.628);
        markerOptions = new MarkerOptions()
                .position(KostanayShift)
                .title("BeshEater")
                .snippet("Android prince")
                .icon(image)
                .anchor(0.5f, 0.5f);
        Marker marker2 = mMap.addMarker(markerOptions);
        //Show marker info Window
        marker2.showInfoWindow();

        //Move camera to coordinates
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Kostanay));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Kostanay));

        //Creating circle around marker
        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(Kostanay)
                .strokeWidth(3.0f)
                .strokeColor(Color.argb(255,33, 198,196))
                .fillColor(Color.argb(50, 125, 51, 175))
                .radius(50); // In meters

        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);

    }
}
