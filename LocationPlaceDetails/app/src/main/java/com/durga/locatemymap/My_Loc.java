package com.durga.locatemymap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.durga.locatemymap.LocateDatabase.DatabaseHandlerNew;
import com.durga.locatemymap.utility.DataDTO;
import com.durga.locatemymap.utility.MyLocation;

import java.util.ArrayList;

/**
 * Created by admin on 09-04-2016.
 */
public class My_Loc extends AppCompatActivity{
    private Toolbar toolbar;
    ListView my_loc_list;
    ImageView add;
    ArrayList<DataDTO> dataDTOs;
    DatabaseHandlerNew databaseHandlerNew;
    MyLocation myLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_location);
        my_loc_list= (ListView) findViewById(R.id.my_loc_list);
        add= (ImageView) findViewById(R.id.add);
        myLocation=new MyLocation(this);
        databaseHandlerNew=new DatabaseHandlerNew(this);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("My Location");
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(My_Loc.this,MapsActivity.class);
                intent.putExtra("MyLat", getIntent().getDoubleExtra("MyLat",0.0));
                intent.putExtra("MyLng",getIntent().getDoubleExtra("MyLng",0.0));

                startActivity(intent);
            }
        });
        dataDTOs=databaseHandlerNew.getAllPlaceDetails();

        Adapter adapter =new Adapter(My_Loc.this,dataDTOs);
        my_loc_list.setAdapter(adapter);
        my_loc_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(My_Loc.this,MyLocOnMapActivity.class);
        intent.putExtra("Name",dataDTOs.get(position).getPlace_name());
        intent.putExtra("Latitude",dataDTOs.get(position).getLatitude());
        intent.putExtra("Longitude",dataDTOs.get(position).getLongitude());
        startActivity(intent);
    }
});
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
                startActivity(new Intent(My_Loc.this, MainActivity.class));
                break;
            case R.id.my_loc:
                startActivity(new Intent(My_Loc.this, My_Loc.class));
                break;
            case R.id.about:
                startActivity(new Intent(My_Loc.this, About_us.class));
                break;
            case R.id.share:
                Location location = myLocation.getBestLocation();
                double mLatitude = location.getLatitude();
                double mLongitude = location.getLongitude();
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
            final Dialog dialog = new Dialog(My_Loc.this);
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
}
