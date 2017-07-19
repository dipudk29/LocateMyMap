package com.durga.locatemymap.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceClass {

	private static final String USER_PREFS = "Urjaa Samadhan";
	private SharedPreferences appSharedPrefs;
	private SharedPreferences.Editor prefsEditor;

	// private String user_name = "user_name_prefs";
	// String user_id = "user_id_prefs";

	public SharedPreferenceClass(Context context) {
		this.appSharedPrefs = context.getSharedPreferences(USER_PREFS,Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}

	//get value
	public int getValue_int(String intKeyValue) {
		return appSharedPrefs.getInt(intKeyValue, 0);
	}

	public String getValue_string(String stringKeyValue) {
		return appSharedPrefs.getString(stringKeyValue, "false");
	}

	public Boolean getValue_boolean(String stringKeyValue) {
		return appSharedPrefs.getBoolean(stringKeyValue, false);
	}

	//setvalue
	
	public void setValue_int(String intKeyValue, int _intValue) {

		prefsEditor.putInt(intKeyValue, _intValue).commit();
	}

	public void setValue_string(String stringKeyValue, String _stringValue) {

		prefsEditor.putString(stringKeyValue, _stringValue).commit();

	}
	
	public void setValue_boolean(String stringKeyValue, Boolean _bool) {

		prefsEditor.putBoolean(stringKeyValue, _bool).commit();

	}

	public void setValue_int(String intKeyValue) {

		prefsEditor.putInt(intKeyValue, 0).commit();
	}

	public void clearData() {
		prefsEditor.clear().commit();

	}
}