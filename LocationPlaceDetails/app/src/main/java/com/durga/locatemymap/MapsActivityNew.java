package com.durga.locatemymap;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivityNew extends FragmentActivity implements LoaderCallbacks<Cursor>, LocationListener{
    private LocationManager locMan;
    GoogleMap mGoogleMap;
    TextView search;
    Button add;
    String message="";
    double select_lat=0.0,select_lon=0.0;
    public static Location curLocation;
    private Boolean locationChanged;
    LocationListener gpsListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_map);

        search=(TextView) findViewById(R.id.textView1);
        add=(Button) findViewById(R.id.button1);


        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mGoogleMap = fragment.getMap();


        gpsListener = new LocationListener() {
            public void onLocationChanged(Location location) {


                if (curLocation == null) {
                    curLocation = location;
                    locationChanged = true;
                }else if (curLocation.getLatitude() == location.getLatitude()
                        && curLocation.getLongitude() == location.getLongitude()){
                    locationChanged = false;
                    return;
                }else
                    locationChanged = true;

                curLocation = location;

                if (locationChanged)
                    locMan.removeUpdates(gpsListener);

            }

            public void onProviderDisabled(String provider) {
            }


            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status,Bundle extras) {
                if (status == 0)// UnAvailable
                {
                } else if (status == 1)// Trying to Connect
                {
                } else if (status == 2) {// Available
                }
            }

        };


        moveMapToMyLocation();

        handleIntent(getIntent());

        onSearchRequested();

        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(message.equals("")){
                    Toast.makeText(MapsActivityNew.this, "Please Select Any Place.", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent=new Intent();
                    intent.putExtra("P",message);
                    intent.putExtra("Lat",select_lat);
                    intent.putExtra("Lon",select_lon);
                    setResult(5,intent);
                    finish();//finishing activity
                }
            }
        });
    }

    protected void getMyCurrentLocation() {


        Location gpslocation = null;
        Location networkLocation = null;

        if(locMan==null){
            locMan = (LocationManager) getApplicationContext() .getSystemService(Context.LOCATION_SERVICE);
        }

        try {
            if(locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,60*1000, 1, gpsListener);// here you can set the 2nd argument time interval also that after how much time it will get the gps location
                gpslocation = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if(locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,60*1000, 1, gpsListener);
                networkLocation = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (IllegalArgumentException e) {
            //Log.e(ErrorCode.ILLEGALARGUMENTERROR, e.toString());
            Log.e("error", e.toString());
        }

        if(gpslocation!=null && networkLocation!=null){
            if(gpslocation.getTime() < networkLocation.getTime()){

                gpslocation = null;
                getData(networkLocation);

            }else{

                networkLocation = null;
                getData(gpslocation);

            }

        }else if(gpslocation!=null){
            getData(gpslocation);

        }else if(networkLocation!=null){
            getData(networkLocation);
        }else{

        }





    }

    private void getData(Location location) {
        // TODO Auto-generated method stub
		/*String lon = Double.toString(location.getLongitude());
		String lat = Double.toString(location.getLatitude());*/

        //here you get current location of user
        select_lat=location.getLatitude();
        select_lon=location.getLongitude();


        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(select_lat, select_lon, 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();

        message=address+","+city+","+state+","+country;//total address of user

        Intent intent=new Intent();
        intent.putExtra("P",message);
        intent.putExtra("Lat",select_lat);
        intent.putExtra("Lon",select_lon);
        setResult(5,intent);
        finish();//finishing activity
    }

    /*private void getData(Location location) {


    }
*/
    private void handleIntent(Intent intent){

        if (intent != null && intent.getAction() != null) {
            if(intent.getAction().equals(Intent.ACTION_SEARCH)){
                doSearch(intent.getStringExtra(SearchManager.QUERY));
            }else if(intent.getAction().equals(Intent.ACTION_VIEW)){
                getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("P",message);
        intent.putExtra("Lat",select_lat);
        intent.putExtra("Lon",select_lon);
        setResult(5,intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void doSearch(String query){

        Bundle data = new Bundle();
        data.putString("query", query);
        getSupportLoaderManager().restartLoader(0, data, this);
    }

    private void getPlace(String query){

        Bundle data = new Bundle();
        data.putString("query", query);
        getSupportLoaderManager().restartLoader(1, data, this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_search:
                onSearchRequested();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle query) {
        CursorLoader cLoader = null;
        if(arg0==0)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.SEARCH_URI, null, null, new String[]{ query.getString("query") }, null);
        else if(arg0==1)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.DETAILS_URI, null, null, new String[]{ query.getString("query") }, null);
        return cLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
        showLocations(c);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
    }

    private void showLocations(Cursor c){
        MarkerOptions markerOptions = null;
        LatLng position = null;
        mGoogleMap.clear();
        while(c.moveToNext()){
            markerOptions = new MarkerOptions();
            select_lon=Double.parseDouble(c.getString(2));
            select_lat=Double.parseDouble(c.getString(1));
            message=c.getString(0);
            position = new LatLng(Double.parseDouble(c.getString(1)),Double.parseDouble(c.getString(2)));
            markerOptions.position(position);
            markerOptions.title(c.getString(0));
            mGoogleMap.addMarker(markerOptions);


            mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                    select_lat = cameraPosition.target.latitude;

                    select_lon = cameraPosition.target.longitude;


                }
            });
        }
        if(position!=null){
            CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(position);
            mGoogleMap.animateCamera(cameraPosition);
        }
    }
    private void moveMapToMyLocation() {

        LocationManager locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Criteria crit = new Criteria();

        Location loc = locMan.getLastKnownLocation(locMan.getBestProvider(crit, true));

        if(loc!=null){
            CameraPosition camPos = new CameraPosition.Builder()

                    .target(new LatLng(loc.getLatitude(), loc.getLongitude()))

                    .zoom(15)

                    .build();

            CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

            mGoogleMap.moveCamera(camUpdate);


            mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                    select_lat = cameraPosition.target.latitude;

                    select_lon = cameraPosition.target.longitude;


                }
            });

        }
        else{
            // new CustomDialogClass(SearchMapActivity.this, "Please Enable your GPS.");
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(12.9667, 77.5667)).zoom(5).build();

            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        select_lat=location.getLatitude();
        select_lon=location.getLongitude();


        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(select_lat, select_lon, 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

}

