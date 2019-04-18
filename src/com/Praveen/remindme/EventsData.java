/*
 * EventsData.java
 * Class used to create the database
 * Author : Praveen Naresh 			2012053
 */
package com.Praveen.remindme;

import static android.provider.BaseColumns._ID;
import static com.Praveen.remindme.Constants.TITLE;
import static com.Praveen.remindme.Constants.TABLE_NAME;
import static com.Praveen.remindme.Constants.DATE;
import static com.Praveen.remindme.Constants.DESC;
import static com.Praveen.remindme.Constants.TIME;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventsData extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "appointments.db";// database
																	// name
	private static final int DATABASE_VERSION = 1;// database version

	public EventsData(Context con) {
		super(con, DATABASE_NAME, null, DATABASE_VERSION);// creating the
															// database
	}

	@Override
	public void onCreate(SQLiteDatabase db) {// function to create the table
												// using the query
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE
				+ " TEXT NOT NULL," + TIME + " TEXT NOT NULL," + DESC
				+ " TEXT NOT NULL," + DATE + " TEXT NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
