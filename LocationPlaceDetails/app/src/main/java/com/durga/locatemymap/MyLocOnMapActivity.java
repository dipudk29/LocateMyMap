package com.durga.locatemymap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.durga.locatemymap.LocateDatabase.DatabaseHandlerNew;
import com.durga.locatemymap.utility.ConnectionDetector;
import com.durga.locatemymap.utility.MyLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocOnMapActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    double lat, lon;
    Button share;
    ConnectionDetector connectionDetector;
    boolean internet;
    MyLocation myLocation;
    double mLatitude = 21.85;
    double mLongitude = 85.988;
    EditText start;
    DatabaseHandlerNew databaseHandlerNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        myLocation = new MyLocation(this);
        share= (Button) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "http://maps.google.com/?q="+mLatitude+","+mLongitude;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Share Location");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share My Location Via"));
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();//finishing activity
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mLatitude = Double.parseDouble(getIntent().getStringExtra("Latitude"));
                mLongitude = Double.parseDouble(getIntent().getStringExtra("Longitude"));
                LatLng latLng = new LatLng(mLatitude, mLongitude);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                // Setting the title for the marker
                markerOptions.title(getIntent().getStringExtra("Name"));
                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));


            }

        }
    }
}