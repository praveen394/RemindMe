/*
 * CreateAppointment.java
 * Class used to create appointments
 * Author : Praveen Naresh 			2012053
 */
package com.Praveen.remindme;

import java.sql.SQLException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;
import static com.Praveen.remindme.Constants.TITLE;
import static com.Praveen.remindme.Constants.TABLE_NAME;
import static com.Praveen.remindme.Constants.DATE;
import static com.Praveen.remindme.Constants.DESC;
import static com.Praveen.remindme.Constants.TIME;

public class CreateAppointment extends Activity implements OnClickListener {

	private EventsData events;// instance of the database
	private EditText title = null, time = null, desc = null;
	private TextView showdate = null;
	private Button save = null;
	private String currentDate;// variable to get the date from the main
								// activity
	private String tempDate;// variable to store the date

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createappo);
		initControls();
		Bundle bundle = getIntent().getExtras();// getting the date
		currentDate = bundle.getString("date");// from the main activity
		showdate.setText("Date: " + currentDate);
		tempDate = currentDate;
	}

	// initializing the views
	private void initControls() {
		title = (EditText) findViewById(R.id.editTitle);
		time = (EditText) findViewById(R.id.editTime);
		desc = (EditText) findViewById(R.id.editDesc);
		showdate = (TextView) findViewById(R.id.date);

		save = (Button) findViewById(R.id.butSaveApoint);
		save.setOnClickListener(this);
	}

	// function to add the data to the database
	private void addEvent(String title, String time, String desc, String date) {
		SQLiteDatabase db = events.getWritableDatabase();// instance of the
															// database
		ContentValues values = new ContentValues();// adding values to the
													// ContentValues
		values.put(TITLE, title);
		values.put(TIME, time);
		values.put(DESC, desc);
		values.put(DATE, date);
		db.insertOrThrow(TABLE_NAME, null, values);// using database function
													// .insert()
		Toast.makeText(getApplicationContext(),
				"Appointment Added Successfully", Toast.LENGTH_SHORT).show();
	}

	// function to check if the title exists on the same date
	private boolean checkRecords() throws SQLException {
		String title_temp = title.getText().toString();
		SQLiteDatabase db = events.getReadableDatabase();
		Cursor temp = db.rawQuery(// query to get the title
				"SELECT * FROM appointments WHERE TITLE LIKE '" + title_temp
						+ "'" + " AND DATE =" + "'" + currentDate + "'", null);
		if (temp != null && temp.getCount() == 0) {// check if the query returns
													// anything
			return false;
		} else {
			return true;
		}
	}

	// function to clear all the editText views
	private void clearFields() {
		title.setText("");
		time.setText("");
		desc.setText("");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.butSaveApoint:// save button event
			events = new EventsData(this);
			if (title.getText().toString().equals("")// check if all fields are
														// entered
					|| time.getText().toString().equals("")
					|| desc.getText().toString().equals("")) {
				Toast.makeText(getApplicationContext(), "Enter all fields",
						Toast.LENGTH_SHORT).show();
			} else {
				try {
					if (checkRecords()) {// check if the record already exists
						new AlertDialog.Builder(this)
								.setTitle("Error!")
								.setMessage(
										"Appointment "
												+ title.getText().toString()
												+ " already exists, please choose a different event title")
								.setCancelable(false)
								.setNegativeButton("OK",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												dialog.cancel();
											}
										}).show();
					} else {
						addEvent(title.getText().toString(), time.getText()// adding
																			// the
																			// data
																			// to
																			// the
																			// database
								.toString(), desc.getText().toString(),
								tempDate);
						clearFields();// clearing the fields
						// Cursor cursor = getEvents();
						// showEvents(cursor);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					events.close();
				}
			}

			break;
		}

	}

}
