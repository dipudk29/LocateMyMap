package com.durga.locatemymap.utility;


import android.content.Context;
import android.location.LocationManager;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
	
	public static boolean isEditTextContainEmail(EditText argEditText) {

        try {

            Pattern pattern = Pattern.compile("[a-zA-Z0-9.\\-_]{2,32}@[a-zA-Z0-9.\\-_]{2,32}\\.[A-Za-z]{2,4}");
            Matcher matcher = pattern.matcher(argEditText.getText());
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
	
	//^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$
	public static boolean isPasswordMatches(EditText pass,EditText conf) {

        boolean check = false;
        if(pass.getText().toString().equals(conf.getText().toString()) && pass.getText().toString().length()>=6)  
        {
        	check=true;
        }
        
		return check;
    }
	public static boolean isPasswordLengthCheck(EditText pass) {

        boolean check = false;
        if(pass.getText().toString().length()>=6)  
        {
        	check=true;
        }
        
		return check;
    }
public static boolean isEditTextHasvalue(EditText edt)
{
	boolean check;
	if(edt.getText().toString().trim().equals(""))
	{
		check=false;
	}
	else
		check=true;
	
	return check;
	
}

public static boolean isSpinnerSelected(Spinner spin)
{
	boolean check;
	if(spin.getSelectedItemPosition()<1)
	{
		check=false;
	}
	else
		check=true;
	
	return check;
	
}
	public  static  boolean isNumberselected(EditText number){
		boolean check;
		if(number.getText().toString().length()>9){
			check=true;
		}
		else {
			check=false;
		}
		return check;
	}
	public static boolean isGpsOn(Context context) {
		LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
}
