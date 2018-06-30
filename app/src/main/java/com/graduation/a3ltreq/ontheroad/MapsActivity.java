package com.graduation.a3ltreq.ontheroad;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.graduation.a3ltreq.ontheroad.model.Provider;

import static com.graduation.a3ltreq.ontheroad.Fragment.RescueFragment.PROVIDER_DETAILS;
import static com.graduation.a3ltreq.ontheroad.MainActivity.latitude;
import static com.graduation.a3ltreq.ontheroad.MainActivity.longitude;
import static com.graduation.a3ltreq.ontheroad.MainActivity.markerOptions;
import static com.graduation.a3ltreq.ontheroad.activity.LoginActivity.PREF_USER_NAME;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Provider provider;
    MarkerOptions mMarkerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        provider=getIntent().getParcelableExtra(PROVIDER_DETAILS);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMarkerOptions = new MarkerOptions();

        mMap = googleMap;

        LatLng latlng = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(latlng).title("ME").icon(BitmapDescriptorFactory.fromResource(R.drawable.man32)));

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(provider.getLat(), provider.getLng());

        mMap.addMarker(new MarkerOptions().position(sydney).title(provider.getS_name())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mec32)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));

    }
}
