/*
 * SearchAppointments.java
 * Class to search for appointments
 * Author : Praveen Naresh 			2012053
 */
package com.Praveen.remindme;

import static android.provider.BaseColumns._ID;
import static com.Praveen.remindme.Constants.DATE;
import static com.Praveen.remindme.Constants.DESC;
import static com.Praveen.remindme.Constants.TABLE_NAME;
import static com.Praveen.remindme.Constants.TIME;
import static com.Praveen.remindme.Constants.TITLE;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SearchAppointments extends Activity implements OnClickListener {

	private EventsData events;
	private String currentDate;
	private EditText et = null;
	private Button but = null;
	private TextView tv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchappo);
		init();
		Bundle bundle = getIntent().getExtras();
		currentDate = bundle.getString("date");
		// displayRecords();
	}

	// intialize the views
	private void init() {
		et = (EditText) findViewById(R.id.EnterSearch);
		tv = (TextView) findViewById(R.id.showEvents);
		but = (Button) findViewById(R.id.butSearch);

		but.setOnClickListener(this);
	}

	private void displayRecords() {
		events = new EventsData(this);
		try {
			Cursor cursor = getEvents();
			DisplayEvents(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			events.close();
		}
	}

	private void DisplayEvents(Cursor c) {
		StringBuilder builder = new StringBuilder();

		while (c.moveToNext()) {
			long id = c.getLong(0);
			String title = c.getString(1);
			String time = c.getString(2);
			String desc = c.getString(3);
			String date = c.getString(4);

			builder.append(id).append(".\t");
			builder.append(title).append(",\t");
			builder.append(time).append(",\t");
			builder.append(desc).append(",\t");
			builder.append(date).append("\n");
		}
		tv.setText(builder);
	}

	// function to retrive all the records
	// and search
	private void getData() {
		events = new EventsData(this);
		try {
			Cursor cursor = getEvents();
			showEvents(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			events.close();
		}
	}

	// function to get the events
	private Cursor getEvents() {
		SQLiteDatabase db = events.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE DATE >= '" + currentDate + "'", null);
		return c;
	}

	// function to show the events
	private void showEvents(Cursor c) {
		String search = et.getText().toString().toLowerCase();// getting the
																// search field
		ArrayList<Appointments> al = new ArrayList<Appointments>();// creating
																	// an
																	// arraylist
																	// of
																	// appointment
																	// type
		StringBuilder temp = new StringBuilder();
		while (c.moveToNext())// getting only the title and description
		{
			String title = c.getString(1);
			String desc = c.getString(3);

			Appointments obj = new Appointments();
			obj.setTITLE(title);
			obj.setDESC(desc);
			al.add(obj);

		}

		for (Appointments test : al)// using a loop iterate and search for the
									// user's text
		{
			if (test.getTITLE().toString().toLowerCase().contains(search)
					|| test.getDESC().toString().toLowerCase().contains(search)) {
				// tv.setText("");
				temp.append(test.getTITLE() + "\t\t" + test.getDESC() + "\n");
			}
		}
		tv.setText(temp);// assign it to a textView
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.butSearch:// search button event
			getData();
			et.setText("");
			break;
		}
	}

}
