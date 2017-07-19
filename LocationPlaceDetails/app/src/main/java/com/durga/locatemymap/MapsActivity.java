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

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    double lat, lon;
    ConnectionDetector connectionDetector;
    boolean internet;
    MyLocation myLocation;
    double mLatitude=21.85;
    double mLongitude=85.988;
    EditText start;
    DatabaseHandlerNew databaseHandlerNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        myLocation=new MyLocation(this);
        databaseHandlerNew=new DatabaseHandlerNew(this);


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
             //   Location location = myLocation.getBestLocation();
                mLatitude =  getIntent().getDoubleExtra("MyLat",0.0);
                mLongitude =  getIntent().getDoubleExtra("MyLng",0.0);
                LatLng latLng = new LatLng(mLatitude, mLongitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        mMap.clear();
                        lat = latLng.latitude;
                        lon = latLng.longitude;


                        MarkerOptions markerOptions = new MarkerOptions();


                        String name = String.valueOf(lat) + " " + String.valueOf(lon);

                        LatLng latLng3 = new LatLng(lat, lon);

                        // Setting the position for the marker
                        markerOptions.position(latLng3);

                        // Setting the title for the marker
                        markerOptions.title(name);
                        markerOptions.draggable(true);
                        // Placing a marker on the touched position
                        mMap.addMarker(markerOptions);

                        // Locate the first location

                        // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));


                    }
                });

mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
    @Override
    public boolean onMarkerClick(Marker marker) {
        showDial();
        return true;
    }
});
                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        mMap.clear();
                        lat = marker.getPosition().latitude;
                        lon=marker.getPosition().longitude;

                        MarkerOptions markerOptions = new MarkerOptions();

                        String name = String.valueOf(lat)+" "+String.valueOf(lon);

                        LatLng latLng3 = new LatLng(lat, lon);

                        // Setting the position for the marker
                        markerOptions.position(latLng3);

                        // Setting the title for the marker
                        markerOptions.title(name);
                        markerOptions.draggable(true);
                        // Placing a marker on the touched position
                        mMap.addMarker(markerOptions);

                        // Locate the first location

                        //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng3, 15.0f));
                    }
                });
            }
        }
    }

    private void showDial() {

        {
            final Dialog dialog = new Dialog(MapsActivity.this);
            //animation for alert dialog
            //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            // hide to default title for Dialog
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            // inflate the layout dialog_layout.xml and set it as contentView
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.add_place_layout, null, false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(view);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            // Retrieve views from the inflated dialog layout and update their
            // values

            start= (EditText) dialog.findViewById(R.id.editText);

            Button navigate = (Button) dialog.findViewById(R.id.btn_find);

            Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

            navigate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close the dialog
                    databaseHandlerNew.AddPlaceData(start.getText().toString(), String.valueOf(lat), String.valueOf(lon));
                    dialog.dismiss();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close the dialog

                    dialog.dismiss();
                }
            });

            // Display the dialog
            dialog.show();

        }


    }


}

