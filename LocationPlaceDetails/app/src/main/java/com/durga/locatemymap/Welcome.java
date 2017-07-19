package com.durga.locatemymap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.durga.locatemymap.utility.InputValidation;
import com.durga.locatemymap.utility.MyLocation;


public class Welcome extends Activity {
    Boolean isInternetPresent = false;
    private static final int TIME = 2 * 1000;// 4 seconds
    private SharedPreferences shp;
    TextView text;
    private LocationManager locMan;
    public static Location curLocation;
    private Boolean locationChanged;
    MyLocation myLocation;
    //ConnectionDetector connectionDetector;
    boolean internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomescreen);
        myLocation=new MyLocation(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
    if(InputValidation.isGpsOn(Welcome.this)){



    Location location = myLocation.getBestLocation();
    if (location != null) {
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        finish();

    } else {
        Toast.makeText(Welcome.this, "Can Not Find Your Location,Try Again", Toast.LENGTH_SHORT).show();
    }

}
else{
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
            Welcome.this);

    // set title
    alertDialogBuilder.setTitle("No GPS!!");

    // set dialog message
    alertDialogBuilder
            .setMessage("Please Turn On Your GPS And Try Again.")
            .setCancelable(false)
            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // if this button is clicked, just close
                    // the dialog box and do nothing
                    dialog.cancel();
                    finish();
                }
            });

    // create alert dialog
    AlertDialog alertDialog = alertDialogBuilder.create();

    // show it
    alertDialog.show();
}


            }
        }, TIME);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { } }, TIME);
    }


}
