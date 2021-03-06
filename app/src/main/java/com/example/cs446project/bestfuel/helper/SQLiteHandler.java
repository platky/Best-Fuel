/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.example.cs446project.bestfuel.helper;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "android_api";

	// Login table name
	private static final String TABLE_LOGIN = "login";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_UID = "uid";
	private static final String KEY_CREATED_AT = "created_at";

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
				+ KEY_CREATED_AT + " TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
		createCarDB(db);
		createPreferencesDB(db);
	}


	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

		// Create tables again
		onCreate(db);
	}

	public void createPreferencesDB(SQLiteDatabase db) {
		//SQLiteDatabase db = this.getWritableDatabase();
		String CREATE_PREF_TABLE = "CREATE TABLE IF NOT EXISTS Pref_Table (name TEXT, " +
				"fuel_type INT, "+ //0 = regular, 1= premium, 2= diesel
				"airport BOOLEAN, "+
				"atm BOOLEAN, "+
				"bakery BOOLEAN, "+
				"bank BOOLEAN, "+
				"bar BOOLEAN, "+
				"cafe BOOLEAN, "+
				"car_dealer BOOLEAN, "+
				"car_wash BOOLEAN, "+
				"convenience_store BOOLEAN, "+
				"food BOOLEAN, "+
				"hospital BOOLEAN, "+
				"liquor_store BOOLEAN, "+
				"lodging BOOLEAN, "+
				"meal_delivery BOOLEAN, "+
				"park BOOLEAN, "+
				"parking BOOLEAN, "+
				"restaurant BOOLEAN, "+
				"shopping_mall BOOLEAN, "+
				"prices INT, "+
				"open_only BOOLEAN)";
		db.execSQL(CREATE_PREF_TABLE);
		Log.d(TAG, "Database preference table created");

	}

	//creates table for car data
	public void createCarDB(SQLiteDatabase db) {
		//SQLiteDatabase db = this.getWritableDatabase();
		String CREATE_CAR_TABLE = "CREATE TABLE IF NOT EXISTS car_table (name TEXT, " +
				"isdefault BOOLEAN, "+
				"year INT," +
				"make TEXT, "+
				"model TEXT, "+
				"id INT, "+
				"body TEXT, "+
				"engine_position TEXT, "+
				"engine_cc INT, "+
				"engine_cyl INT, "+
				"engine_type TEXT, "+
				"engine_valves_per_cyl INT, "+
				"engine_power_rpm INT, "+
				"engine_fuel TEXT, "+
				"top_speed_kmh INT, "+
				"kph0to100 FLOAT, "+
				"drive TEXT, "+
				"transmission TEXT, "+
				"seats INT, "+
				"doors INT, "+
				"weight_kg INT, "+
				"length_mm INT, "+
				"width_mm INT, "+
				"height_mm INT, "+
				"wheelbase_mm INT, "+
				"hwy_lkm FLOAT, "+
				"mixed_lkm FLOAT, "+
				"city_lkm FLOAT, "+
				"fuel_capacity_l INT, "+
				"sold_in_us INT, "+
				"engine_hp INT, "+
				"top_speed_mph INT, "+
				"weight_ibs INT, "+
				"length_in FLOAT, "+
				"width_in FLOAT, "+
				"height_in FLOAT, "+
				"hwy_mpg FLOAT, "+
				"city_mpg FLOAT, "+
				"mixed_mpg FLOAT, "+
				"fuel_capacity_g FLOAT, "+
				"country TEXT, " +
				"picture BLOB )";
		db.execSQL(CREATE_CAR_TABLE);
		Log.d(TAG, "Database car table created");
	}


	/**
	 * Storing user details in database
	 * */
	public void addUser(String name, String email, String uid, String created_at) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_UID, uid); // Email
		values.put(KEY_CREATED_AT, created_at); // Created At

		// Inserting Row
		long id = db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		//Log.d(TAG, "Cursor is testing: " + cursor.getString(0));
		if (cursor.getCount() > 0) {
			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("created_at", cursor.getString(4));
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	/**
	 * Getting user login status return true if rows are there in table
	 * */
	public int getRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

	public void addCar(String name, Boolean isdefault, String year, String make, String model, String id, String body, String engine_position, String engine_cc, String engine_cyl, String engine_type,
					   String engine_valves_per_cyl, String engine_power_rpm, String engine_fuel, String top_speed_kmh, String kph0to100, String drive, String transmission, String seats, String doors, String weight_kg,
					   String length_mm, String height_mm, String width_mm, String wheelbase_mm, String hwy_lkm, String mixed_lkm, String city_lkm, String fuel_capacity_l, String sold_in_us, String engine_hp, String top_speed_mph,
					   String weight_ibs, String length_in, String width_in, String height_in, String hwy_mpg, String city_mpg, String mixed_mpg, String fuel_capacity_g, String country, byte[] blob) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("isdefault", isdefault);
		values.put("year", year);
		values.put("make", make);
		values.put("model", model);
		values.put("id", id);
		values.put("body", body);
		values.put("engine_position", engine_position);
		values.put("engine_cc", engine_cc);
		values.put("engine_cyl", engine_cyl);
		values.put("engine_type", engine_type);
		values.put("engine_valves_per_cyl", engine_valves_per_cyl);
		values.put("engine_power_rpm", engine_power_rpm);
		values.put("engine_fuel", engine_fuel);
		values.put("top_speed_kmh", top_speed_kmh);
		values.put("kph0to100", kph0to100);
		values.put("drive", drive);
		values.put("transmission", transmission);
		values.put("seats", seats);
		values.put("doors", doors);
		values.put("weight_kg", weight_kg);
		values.put("length_mm", length_mm);
		values.put("height_mm", height_mm);
		values.put("width_mm", width_mm);
		values.put("wheelbase_mm", wheelbase_mm);
		values.put("hwy_lkm", hwy_lkm); //25
		values.put("mixed_lkm", mixed_lkm);
		values.put("city_lkm", city_lkm);
		values.put("fuel_capacity_l", fuel_capacity_l);
		values.put("sold_in_us", sold_in_us);
		values.put("engine_hp", engine_hp);
		values.put("top_speed_mph", top_speed_mph);
		values.put("weight_ibs", weight_ibs);
		values.put("length_in", length_in);
		values.put("width_in", width_in);
		values.put("height_in", height_in);
		values.put("hwy_mpg", hwy_mpg);
		values.put("city_mpg", city_mpg);
		values.put("mixed_mpg", mixed_mpg);
		values.put("fuel_capacity_g", fuel_capacity_g);
		values.put("country", country);
		values.put("picture", blob);

		// Inserting Row
		long idr = db.insert("car_table", null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New car inserted into sqlite: " + idr);
	}

	public void deleteCar(int id, String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "DELETE FROM car_table WHERE id ='" + id + "' AND name ='" + name+"'";
		db.execSQL(query);
		db.close(); // Closing database connection
	}

	public ArrayList<HashMap<String, String>> getCars(String name){
		ArrayList<HashMap<String, String>> carList = new ArrayList<HashMap<String, String>>();

		String selectQuery = "SELECT  * FROM  car_table WHERE name='"+name+"'";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		//Log.d(TAG, "Cursor is testing: " + cursor.getString(0));
		Log.d("GETCAR", "starting to retrieve cars");
		if (cursor.getCount() > 0) {
			do {
				HashMap<String, String> car = new HashMap<String, String>();
				car.put("name", cursor.getString(0));
				car.put("isdefault", cursor.getString(1));
				car.put("year", cursor.getString(2));
				car.put("make", cursor.getString(3));
				car.put("model", cursor.getString(4));
				car.put("id", cursor.getString(5));
				car.put("body", cursor.getString(6));
				car.put("engine_position", cursor.getString(7));
				car.put("engine_cc", cursor.getString(8));
				car.put("engine_cyl", cursor.getString(9));
				car.put("engine_type", cursor.getString(10));
				car.put("engine_valves_per_cyl", cursor.getString(11));
				car.put("engine_power_rpm", cursor.getString(12));
				car.put("engine_fuel", cursor.getString(13));
				car.put("top_speed_kmh", cursor.getString(14));
				car.put("kph0to100", cursor.getString(15));
				car.put("drive", cursor.getString(16));
				car.put("transmission", cursor.getString(17));
				car.put("seats", cursor.getString(18));
				car.put("doors", cursor.getString(19));
				car.put("weight_kg", cursor.getString(20));
				car.put("length_mm", cursor.getString(21));
				car.put("height_mm", cursor.getString(22));
				car.put("width_mm", cursor.getString(23));
				car.put("wheelbase_mm", cursor.getString(24));
				car.put("hwy_lkm", cursor.getString(25));
				car.put("mixed_lkm", cursor.getString(26));
				car.put("city_lkm", cursor.getString(27));
				car.put("fuel_capacity_l", cursor.getString(28));
				car.put("sold_in_us", cursor.getString(29));
				car.put("engine_hp", cursor.getString(30));
				car.put("top_speed_mph", cursor.getString(31));
				car.put("weight_ibs", cursor.getString(32));
				car.put("length_in", cursor.getString(33));
				car.put("width_in", cursor.getString(34));
				car.put("height_in", cursor.getString(35));
				car.put("hwy_mpg", cursor.getString(36));
				car.put("city_mpg", cursor.getString(37));
				car.put("mixed_mpg", cursor.getString(38));
				car.put("fuel_capacity_g", cursor.getString(39));
				car.put("country", cursor.getString(40));

				Log.d("CURSOR TEST", "cursor is " + car.get("name"));
				Log.d("CURSOR TEST", "cursor is " + car.get("isdefault"));
				Log.d("CURSOR TEST", "cursor is " + car.get("year"));
				Log.d("CURSOR TEST", "cursor is " + car.get("make"));
				carList.add(car);

			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return user
		//Log.d(TAG, "Fetching car from Sqlite: " + car.toString());

		return carList;
	}

	public HashMap<String, String> getCar(Boolean isdefault, String name, int id) {
		String selectQuery ;
		if(isdefault==true) {
			selectQuery="SELECT * FROM car_table WHERE name='"+name+"' AND isdefault='true'";
			Log.d(TAG,"Checking for default car");
		} else {
			selectQuery="SELECT * FROM car_table WHERE name='"+name+"' AND id='"+id+"'";
		}
		HashMap<String, String> car = new HashMap<String, String>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		cursor.moveToFirst();
		Log.d(TAG, "Grabbing Default car with user " + name + " and default " + isdefault);
		if (cursor.getCount() > 0) {
			Log.d(TAG, "Grabbing cursor");
			car.put("name", cursor.getString(0));
			car.put("isdefault", cursor.getString(1));
			car.put("year", cursor.getString(2));
			car.put("make", cursor.getString(3));
			car.put("model", cursor.getString(4));
			car.put("id", cursor.getString(5));
			car.put("body", cursor.getString(6));
			car.put("engine_position", cursor.getString(7));
			car.put("engine_cc", cursor.getString(8));
			car.put("engine_cyl", cursor.getString(9));
			car.put("engine_type", cursor.getString(10));
			car.put("engine_valves_per_cyl", cursor.getString(11));
			car.put("engine_power_rpm", cursor.getString(12));
			car.put("engine_fuel", cursor.getString(13));
			car.put("top_speed_kmh", cursor.getString(14));
			car.put("kph0to100", cursor.getString(15));
			car.put("drive", cursor.getString(16));
			car.put("transmission", cursor.getString(17));
			car.put("seats", cursor.getString(18));
			car.put("doors", cursor.getString(19));
			car.put("weight_kg", cursor.getString(20));
			car.put("length_mm", cursor.getString(21));
			car.put("height_mm", cursor.getString(22));
			car.put("width_mm", cursor.getString(23));
			car.put("wheelbase_mm", cursor.getString(24));
			car.put("hwy_lkm", cursor.getString(25));
			car.put("mixed_lkm", cursor.getString(26));
			car.put("city_lkm", cursor.getString(27));
			car.put("fuel_capacity_l", cursor.getString(28));
			car.put("sold_in_us", cursor.getString(29));
			car.put("engine_hp", cursor.getString(30));
			car.put("top_speed_mph", cursor.getString(31));
			car.put("weight_ibs", cursor.getString(32));
			car.put("length_in", cursor.getString(33));
			car.put("width_in", cursor.getString(34));
			car.put("height_in", cursor.getString(35));
			car.put("hwy_mpg", cursor.getString(36));
			car.put("city_mpg", cursor.getString(37));
			car.put("mixed_mpg", cursor.getString(38));
			car.put("fuel_capacity_g", cursor.getString(39));
			car.put("country", cursor.getString(40));

		}
		cursor.close();
		db.close();
		return car;
	}
	public void updateCarPicture( String name, String id, byte[] picture) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Picture", picture);


		db.update("car_table", values, "name=? AND id=?", new String[]{name, id});


		db.close();
		return;
	}
	public byte[] getCarPicture( String name, String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery="SELECT picture FROM car_table WHERE name=? AND id=?";


		Cursor cursor = db.rawQuery(selectQuery,new String[]{name,id});

		cursor.moveToFirst();
        byte[] ret=cursor.getBlob(0);
		db.close();
		return ret;
	}

	public void defaultCarUpdate(String username, String id){
		//first get current default car
		HashMap<String,String> defCar = getCar(true, username, -1);
		//now change it to not-default
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put("isdefault", "false");
		Log.d(TAG, "Default car had name "+defCar.get("name")+" and id "+defCar.get("id"));
		int a =db.update("car_table", args, "name='" + username + "' AND id='" + defCar.get("id")+"'", null);
		Log.d(TAG,"default car update 1 was "+a);

		//update new car if needed
		if(id!=null && id!=""){
			ContentValues args2 = new ContentValues();
			args2.put("isdefault", "true");
			int b =db.update("car_table", args2, "name='" + username + "' AND id=" + id, null);
			Log.d(TAG, "default car update 2 was " + b);
		}
		db.close();

	}


	public ContentValues getPrefs(String username) {
		ContentValues prefs= new ContentValues();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM Pref_Table WHERE name='"+username+"'";

		Cursor cursor = db.rawQuery(selectQuery, null);

		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			prefs.put("name", cursor.getString(0));
			prefs.put("fuel_type", cursor.getInt(1));
			prefs.put("airport", cursor.getInt(2)==1);
			prefs.put("atm", cursor.getInt(3)==1);
			prefs.put("bakery", cursor.getInt(4)==1);
			prefs.put("bank", cursor.getInt(5)==1);
			prefs.put("bar", cursor.getInt(6)==1);
			prefs.put("cafe", cursor.getInt(7)==1);
			prefs.put("car_dealer", cursor.getInt(8)==1);
			prefs.put("car_wash", cursor.getInt(9)==1);
			prefs.put("convenience_store", cursor.getInt(10)==1);
			prefs.put("food", cursor.getInt(11)==1);
			prefs.put("hospital", cursor.getInt(12)==1);
			prefs.put("liquor_store", cursor.getInt(13)==1);
			prefs.put("lodging", cursor.getInt(14)==1);
			prefs.put("meal_delivery", cursor.getInt(15)==1);
			prefs.put("park", cursor.getInt(16)==1);
			prefs.put("parking", cursor.getInt(17)==1);
			prefs.put("restaurant", cursor.getInt(18)==1);
			prefs.put("shopping_mall", cursor.getInt(19)==1);
			prefs.put("prices", cursor.getInt(20));//range 1 to 4
			prefs.put("open_only", cursor.getInt(21)==1);
			Log.d(TAG, "cursor 1 name is " + cursor.getString(0));
			Log.d(TAG,"airport is "+cursor.getString(2));
			Log.d(TAG,"airport is (check) "+(cursor.getInt(2)==1));
		}
		cursor.close();
		db.close();
		return prefs;
	}

	public void defaultPrefs(String username) {
		SQLiteDatabase dbr = this.getReadableDatabase();
		SQLiteDatabase db = this.getWritableDatabase();
		String checkQuery = "SELECT * FROM Pref_Table WHERE name='"+username+"'";

		Cursor cursor = dbr.rawQuery(checkQuery, null);
		if(cursor.getCount()==0) {


			ContentValues values = new ContentValues();
			values.put("name", username);
			values.put("fuel_type", 0);
			values.put("airport", false);
			values.put("atm", false);
			values.put("bakery", false);
			values.put("bank", false);
			values.put("bar", false);
			values.put("cafe", true);
			values.put("car_dealer", false);
			values.put("car_wash", true);
			values.put("convenience_store", true);
			values.put("food", true);
			values.put("hospital", true);
			values.put("liquor_store", false);
			values.put("lodging", false);
			values.put("meal_delivery", true);
			values.put("park", false);
			values.put("parking", true);
			values.put("restaurant", true);
			values.put("shopping_mall", false);
			values.put("prices", 4);
			values.put("open_only", false);
			db.insert("Pref_Table", null, values);
			db.close(); // Closing database connection
			Log.d(TAG, "Created default preferences");
		} else {
			Log.d(TAG,"User has existing preferences");
		}
	}

	public void updatePrefs(ContentValues prefs, String username) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("fuel_type", prefs.getAsInteger("fuel_type"));
		values.put("airport",  prefs.getAsBoolean("airport"));
		values.put("atm",  prefs.getAsBoolean("atm"));
		values.put("bakery",  prefs.getAsBoolean("bakery"));
		values.put("bank",  prefs.getAsBoolean("bank"));
		values.put("bar",  prefs.getAsBoolean("bar"));
		values.put("cafe",  prefs.getAsBoolean("cafe"));
		values.put("car_dealer",  prefs.getAsBoolean("car_dealer"));
		values.put("car_wash",  prefs.getAsBoolean("car_wash"));
		values.put("convenience_store",  prefs.getAsBoolean("convenience_store"));
		values.put("food",  prefs.getAsBoolean("food"));
		values.put("hospital",  prefs.getAsBoolean("hospital"));
		values.put("liquor_store",  prefs.getAsBoolean("liquor_store"));
		values.put("lodging",  prefs.getAsBoolean("lodging"));
		values.put("meal_delivery",  prefs.getAsBoolean("meal_delivery"));
		values.put("park",  prefs.getAsBoolean("park"));
		values.put("parking",  prefs.getAsBoolean("parking"));
		values.put("restaurant",  prefs.getAsBoolean("restaurant"));
		values.put("shopping_mall",  prefs.getAsBoolean("shopping_mall"));
		values.put("prices",  prefs.getAsInteger("prices"));
		values.put("open_only", prefs.getAsBoolean("open_only"));
		db.update("Pref_Table", values, "name='" + username + "'", null);
		Log.d(TAG, "Preferences Updated");

		db.close();
	}

}
