package com.durga.locatemymap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.durga.locatemymap.utility.MyLocation;
import com.durga.locatemymap.utility.SharedPreferenceClass;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements LocationListener{
	
	GoogleMap mGoogleMap;	
	Spinner mSprPlaceType;	
	
	String[] mPlaceType=null;
	String[] mPlaceTypeName=null;
	String[] mRadiusType=null;
	String[] mRadiusTypeName=null;
	double mLatitude=21.85;
	double mLongitude=85.988;//
	private Toolbar toolbar;
	HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();
	ImageView navi,my_loc,search;
	EditText start,end,place_to_search;
	double start_lati,start_longi,end_lati,end_longi;
	MyLocation myLocation;
	SharedPreferenceClass sharedPreferenceClass;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharedPreferenceClass =new SharedPreferenceClass(this);
		toolbar = (Toolbar) findViewById(R.id.app_bar);
		myLocation=new MyLocation(this);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle("Search Place");

		}
		place_to_search= (EditText) findViewById(R.id.place_to_search);
		navi= (ImageView) findViewById(R.id.navi);
		my_loc= (ImageView) findViewById(R.id.my_loc);
		search= (ImageView) findViewById(R.id.search);

       showMyLoc();



		place_to_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(MainActivity.this, MapsActivityNew.class);
				int requestCode = 3;
				startActivityForResult(intent1, requestCode);
			}
		});



		navi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				{
					final Dialog dialog = new Dialog(MainActivity.this);
					//animation for alert dialog
					//dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					// hide to default title for Dialog
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

					// inflate the layout dialog_layout.xml and set it as contentView
					LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(R.layout.navigation_layout, null, false);
					dialog.setCanceledOnTouchOutside(false);
					dialog.setContentView(view);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
					dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);

					// Retrieve views from the inflated dialog layout and update their
					// values

					start= (EditText) dialog.findViewById(R.id.editText);
					end= (EditText) dialog.findViewById(R.id.editText1);


					start.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							sharedPreferenceClass.setValue_string("Status","start");
							Intent intent1 = new Intent(MainActivity.this, MapsActivityNew.class);
							int requestCode = 2;
							startActivityForResult(intent1, requestCode);
						}
					});
					end.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							sharedPreferenceClass.setValue_string("Status","end");
							Intent intent1 = new Intent();
							intent1.setClass(MainActivity.this, MapsActivityNew.class);
							int requestCode = 2;
							startActivityForResult(intent1, requestCode);
						}
					});



					Button navigate = (Button) dialog.findViewById(R.id.btn_find);

					Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);




					navigate.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							Intent intent=new Intent(MainActivity.this,DirectionsActivity.class);
							intent.putExtra("Start_Lat",start_lati);
							intent.putExtra("Start_Lng",start_longi);
							intent.putExtra("End_Lat",end_lati);
							intent.putExtra("End_Lng", end_longi);
							startActivity(intent);
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
		});
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				{
					final Dialog dialog = new Dialog(MainActivity.this);
					//animation for alert dialog
					//dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					// hide to default title for Dialog
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

					// inflate the layout dialog_layout.xml and set it as contentView
					LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(R.layout.search_layout, null, false);
					dialog.setCanceledOnTouchOutside(false);
					dialog.setContentView(view);
					dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
					dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);

					// Retrieve views from the inflated dialog layout and update their
					// values

					mSprPlaceType= (Spinner) dialog.findViewById(R.id.spr_place_type);
					final Spinner end= (Spinner) dialog.findViewById(R.id.spr_radius_type);


					Button btnFind = (Button) dialog.findViewById(R.id.btn_find);

					Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);


// Array of place types
					mPlaceType = getResources().getStringArray(R.array.place_type);

					// Array of place type names
					mPlaceTypeName = getResources().getStringArray(R.array.place_type_name);

					// Creating an array adapter with an array of Place types
					// to populate the spinner
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, mPlaceTypeName);

					// Setting adapter on Spinner to set place types
					mSprPlaceType.setAdapter(adapter);


					mRadiusType = getResources().getStringArray(R.array.radius_type);

					// Array of place type names


					// Creating an array adapter with an array of Place types
					// to populate the spinner
					ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, mRadiusType);

					// Setting adapter on Spinner to set place types
					end.setAdapter(adapter1);

					// Setting click event lister for the find button
					btnFind.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							dialog.dismiss();
							int selectedPosition = mSprPlaceType.getSelectedItemPosition();
							String type = mPlaceType[selectedPosition];

							int selected = end.getSelectedItemPosition();
							String _type = mRadiusType[selected];
							StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
							sb.append("location="+mLatitude+","+mLongitude);
							sb.append("&radius="+_type);
							sb.append("&types="+type);
							sb.append("&sensor=true");
							sb.append("&key=AIzaSyBRdLRUPE9FE75Q7gmikJeMbOK3FOPBFlY");


							// Creating a new non-ui thread task to download Google place json data
							PlacesTask placesTask = new PlacesTask();

							// Invokes the "doInBackground()" method of the class PlaceTask
							placesTask.execute(sb.toString());


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
		});
		my_loc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showMyLoc();
			}
		});





		

		
		
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

        	int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available
        	
	    	// Getting reference to the SupportMapFragment
	    	SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	    			
	    	// Getting Google Map
	    	mGoogleMap = fragment.getMap();
	    			
	    	// Enabling MyLocation in Google Map
	    	mGoogleMap.setMyLocationEnabled(true);
	    	
	    	
	    	
	    	// Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location From GPS
            Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
                    onLocationChanged(location);
            }

            locationManager.requestLocationUpdates(provider, 20000, 0, this);
                        
            mGoogleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker arg0) {
					Intent intent = new Intent(getBaseContext(), PlaceDetailsActivity.class);
					String reference = mMarkerPlaceLink.get(arg0.getId());
					intent.putExtra("reference", reference);
					intent.putExtra("myLat", start_lati);
					intent.putExtra("myLng", start_longi);

					// Starting the Place Details Activity
					startActivity(intent);
				}
			});

        }		
 		
	}

	private void showMyLoc() {
		SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

		// Getting Google Map
		mGoogleMap = fragment.getMap();
		Location location = myLocation.getBestLocation();
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();
		LatLng latLng = new LatLng(mLatitude, mLongitude);

		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));


		mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition cameraPosition) {

				mLatitude = cameraPosition.target.latitude;

				mLongitude = cameraPosition.target.longitude;


			}
		});

	}

	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
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

	
	/** A class, to download Google Places */
	private class PlacesTask extends AsyncTask<String, Integer, String>{

		String data = null;
		
		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try{
				data = downloadUrl(url[0]);
			}catch(Exception e){
				 Log.d("Background Task",e.toString());
			}
			return data;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result){			
			ParserTask parserTask = new ParserTask();
			
			// Start parsing the Google places in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			parserTask.execute(result);
		}
		
	}
	
	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

		JSONObject jObject;
		
		// Invoked by execute() method of this object
		@Override
		protected List<HashMap<String,String>> doInBackground(String... jsonData) {
		
			List<HashMap<String, String>> places = null;			
			PlaceJSONParserArea placeJsonParser = new PlaceJSONParserArea();
        
	        try{
	        	jObject = new JSONObject(jsonData[0]);
	        	
	            /** Getting the parsed data as a List construct */
	            places = placeJsonParser.parse(jObject);
	            
	        }catch(Exception e){
	                Log.d("Exception",e.toString());
	        }
	        return places;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String,String>> list){			
			
			// Clears all the existing markers 
			mGoogleMap.clear();
			
			for(int i=0;i<list.size();i++){
			
				// Creating a marker
	            MarkerOptions markerOptions = new MarkerOptions();
	            
	            // Getting a place from the places list
	            HashMap<String, String> hmPlace = list.get(i);
	
	            // Getting latitude of the place
	            double lat = Double.parseDouble(hmPlace.get("lat"));	            
	            
	            // Getting longitude of the place
	            double lng = Double.parseDouble(hmPlace.get("lng"));
	            
	            // Getting name
	            String name = hmPlace.get("place_name");
	            
	            // Getting vicinity
	            String vicinity = hmPlace.get("vicinity");
	            
	            LatLng latLng = new LatLng(lat, lng);
	            
	            // Setting the position for the marker
	            markerOptions.position(latLng);
	
	            // Setting the title for the marker. 
	            //This will be displayed on taping the marker
	            markerOptions.title(name + " : " + vicinity);	            
	
	            // Placing a marker on the touched position
	            Marker m = mGoogleMap.addMarker(markerOptions);	            

	            // Linking Marker id and place reference
	            mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));


			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.finish();
				break;

			case R.id.near_by:
				startActivity(new Intent(MainActivity.this, MainActivity.class));
				break;
			case R.id.my_loc:
				startActivity(new Intent(MainActivity.this, My_Loc.class)
				.putExtra("MyLat",mLatitude)
				.putExtra("MyLng",mLongitude)
				);
				break;
			case R.id.about:
				startActivity(new Intent(MainActivity.this, About_us.class));
				break;
			case R.id.share:
				Location location = myLocation.getBestLocation();
				mLatitude = location.getLatitude();
				mLongitude = location.getLongitude();
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = "http://maps.google.com/?q="+mLatitude+","+mLongitude;
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Share Location");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(sharingIntent, "Share My Location Via"));
				break;
			case R.id.rate:
              rateUs();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void rateUs() {

		{
			final Dialog dialog = new Dialog(MainActivity.this);
			//animation for alert dialog
			//dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
			// hide to default title for Dialog
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

			// inflate the layout dialog_layout.xml and set it as contentView
			LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.rate_us, null, false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setContentView(view);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			// Retrieve views from the inflated dialog layout and update their
			// values



			Button navigate = (Button) dialog.findViewById(R.id.btn_find);

			Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);




			navigate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
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

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

	@Override
	public void onLocationChanged(Location location) {

		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==5 && requestCode==2) {

			String place=data.getStringExtra("P");

			if(sharedPreferenceClass.getValue_string("Status").equals("start")){
				start_lati = data.getDoubleExtra("Lat", 0.0);
				start_longi = data.getDoubleExtra("Lon",0.0);
				start.setText(place);
			}
			else{
				end_lati = data.getDoubleExtra("Lat", 0.0);
				end_longi = data.getDoubleExtra("Lon",0.0);
				end.setText(place);
			}

		}
		else if(resultCode==5 && requestCode==3){
			String place=data.getStringExtra("P");
			mLatitude = data.getDoubleExtra("Lat", 0.0);
			mLongitude = data.getDoubleExtra("Lon",0.0);
			place_to_search.setText(place);
			LatLng latLng = new LatLng(mLatitude, mLongitude);

			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

		}
	}
}