package com.durga.locatemymap.LocateDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.durga.locatemymap.utility.DataDTO;

import java.util.ArrayList;

public class DatabaseHandlerNew extends SQLiteOpenHelper {
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "ShopAd.db";
	// Product Table name


    private static final String ID = "id";


	private static final String PLACE_TABLE="place_table";
	private static final String AD_TABLE="ad_table";
	private static final String AVD_ID="avd_id";
	private static final String PLACE_NAME="place_name";
	private static final String LATITUDE="latitude";
	private static final String LONGITUDE="longitude";




	public DatabaseHandlerNew(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {




		String PLACETABLE = "CREATE TABLE " + PLACE_TABLE + "("+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ PLACE_NAME + " TEXT,"
				+ LATITUDE + " TEXT,"
				+ LONGITUDE + " TEXT)";

		db.execSQL(PLACETABLE);



	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + AD_TABLE);


		// Create tables again
		onCreate(db);
	}



	public void AddPlaceData(String place_name, String latitude, String longitude) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(PLACE_NAME, place_name);
		values.put(LATITUDE, latitude);
		values.put(LONGITUDE, longitude);


		db.insert(PLACE_TABLE, null, values);

	}

	public ArrayList<DataDTO> getAllPlaceDetails() {
		ArrayList<DataDTO> Productlist = new ArrayList<DataDTO>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + PLACE_TABLE;

		//"select * from med_his where med like ''"

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				DataDTO productdto = new DataDTO();

				productdto.setPlace_name(cursor.getString(1));
				productdto.setLatitude(cursor.getString(2));
				productdto.setLongitude(cursor.getString(3));

				Productlist.add(productdto);

			} while (cursor.moveToNext());
		}
		// return product list
		return Productlist;
	}

	public int getCartCount() {
		String countQuery = "SELECT * FROM " + PLACE_TABLE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		return cursor.getCount();
	}
	public void deleteCartData(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(PLACE_TABLE, null, null);
		db.close();
	}

	public void deleteCartSigleData(String P_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(PLACE_TABLE, AVD_ID+"="+"'"+P_id+"'", null);
		db.close();
	}

}
