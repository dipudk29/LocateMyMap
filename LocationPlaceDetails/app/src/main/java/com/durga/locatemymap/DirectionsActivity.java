package com.durga.locatemymap;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionsActivity extends AppCompatActivity {

    private GoogleMap map;
    private MapView mapView;
    private TextView distance_time;
    private ImageButton ibDrive,ibWalking;
    double start_lat,end_lat;
    double end_lon,start_lon;
    double theCurLat;
    double theCurLang;
    private String Mod="driving";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        distance_time= (TextView) findViewById(R.id.distance_time);
        ibDrive= (ImageButton) findViewById(R.id.ibDrive);
        ibWalking= (ImageButton) findViewById(R.id.ibWalking);

        start_lat=getIntent().getDoubleExtra("Start_Lat", 0.0);
        start_lon=getIntent().getDoubleExtra("Start_Lng",0.0);
        end_lat=getIntent().getDoubleExtra("End_Lat",0.0);
        end_lon=getIntent().getDoubleExtra("End_Lng",0.0);

        //lat=22.567;
        //lon=85.678;
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        MapsInitializer.initialize(this);
        mapView.onResume();
        map = mapView.getMap();
        if (map != null) {



            LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (location != null) {
                theCurLat=location.getLatitude();
                theCurLang=location.getLongitude();
            }

            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.setMyLocationEnabled(true);
            map.addMarker(new MarkerOptions().position(new LatLng(start_lat, start_lon)).title(getIntent().getStringExtra("Title")));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(start_lat,start_lon))
                    .tilt(45)
                    .zoom((float) 12)
                    .build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            String url = getDirectionsUrl(start_lat,start_lon,end_lat, end_lon,Mod);

            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);

        }

        ibDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mod="driving";
                String url = getDirectionsUrl(start_lat,start_lon,end_lat, end_lon,Mod);

                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            }
        });
        ibWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mod="walking";
                String url = getDirectionsUrl(start_lat,start_lon,end_lat, end_lon,Mod);

                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            }
        });
    }

    private String getDirectionsUrl(double theCurLat, double theCurLang, double lat, double lon,String mode) {

        // Origin of route
        String str_origin = "origin="+theCurLat+","+theCurLang;

        // Destination of route
        String str_dest = "destination="+lat+","+lon;


        // Sensor enabled
        String sensor = "sensor=false";
        String _mode = "mode="+mode;

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+_mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_booking_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.action_home:
                startActivity(new Intent(DirectionsActivity.this, MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";
map.clear();


            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }


            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){	// Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                map.addMarker(new MarkerOptions().position(new LatLng(start_lat, start_lon)).title(getIntent().getStringExtra("Title")));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(start_lat,start_lon))
                        .tilt(45)
                        .zoom((float) 12)
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(3);
                lineOptions.color(Color.BLUE);

            }

            distance_time.setText("Distance:"+distance + ", Duration:"+duration+" by"+" "+Mod);

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }
}
