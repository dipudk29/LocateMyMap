package com.durga.locatemymap.utility;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by admin on 16-04-2016.
 */
public class MyLocation {
    private LocationManager locMan;
    public static Location curLocation;
    private Boolean locationChanged;
    Activity activity;
    public MyLocation(Activity context) {

        this.activity = context;
    }


    public Location getBestLocation() {
        Location gpslocation = null;
        Location networkLocation = null;
        Location lastLocation = null;

        if(locMan==null){
            locMan = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        }

        try {
            if(locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,1, gpsListener);// here you can set the 2nd argument time interval also that after how much time it will get the gps location
                gpslocation = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if(locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100, 1, gpsListener);
                networkLocation = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if(locMan.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)){
                locMan.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,100, 1, gpsListener);
                lastLocation = locMan.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }


        } catch (IllegalArgumentException e) {
            //Log.e(ErrorCode.ILLEGALARGUMENTERROR, e.toString());
            Log.e("error", e.toString());
        }

        if(gpslocation!=null && networkLocation!=null){

            networkLocation = null;

            return gpslocation;


        }else if(gpslocation!=null){
            return gpslocation;

        }else if(networkLocation!=null){
            return networkLocation;
        }
        else if(lastLocation!=null){
            return lastLocation;
        }
        else{



        }
        return null;
    }
    LocationListener gpsListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (curLocation == null) {
                curLocation = location;
                locationChanged = true;
            }else if (curLocation.getLatitude() == location.getLatitude() && curLocation.getLongitude() == location.getLongitude()){
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
}
