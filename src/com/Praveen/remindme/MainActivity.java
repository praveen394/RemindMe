/*
 * MainActivity.java
 * Main class for the appointments application
 * Author : Praveen Naresh 			2012053
 */
package com.Praveen.remindme;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private CalendarView showCalendar = null;// using calendarview to show the
												// calendar

	private Button create = null;
	private Button view = null;
	private Button delete = null;
	private Button move = null;
	private Button search = null;
	private Button translate = null;
	private String selectedDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		showCalendar = (CalendarView) findViewById(R.id.calendar);
		showCalendar
				.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {// date
																					// change
																					// listener
																					// for
																					// the
																					// calendar

					@Override
					public void onSelectedDayChange(CalendarView view,
							int year, int month, int dayOfMonth) {
						// TODO Auto-generated method stub
						selectedDate = dayOfMonth + "/" + (month + 1) + "/"
								+ year;
						// Toast.makeText(getApplicationContext(),
						// dayOfMonth+"/"+month+"/"+year,
						// Toast.LENGTH_SHORT).show();
					}
				});
		initControls();
	}

	// function to initalize the views
	private void initControls() {
		create = (Button) findViewById(R.id.butCreatAppointment);
		view = (Button) findViewById(R.id.butViewAppointment);
		delete = (Button) findViewById(R.id.butDeleteAppointment);
		move = (Button) findViewById(R.id.butMoveAppointment);
		search = (Button) findViewById(R.id.butSearchAppointment);
		translate = (Button) findViewById(R.id.butTranslateAppointment);

		create.setOnClickListener(this);
		view.setOnClickListener(this);
		delete.setOnClickListener(this);
		move.setOnClickListener(this);
		search.setOnClickListener(this);
		translate.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.butCreatAppointment:// create button event
			if (selectedDate == null || selectedDate == "") {
				Toast.makeText(getApplicationContext(),
						"Please select a date!", Toast.LENGTH_SHORT).show();
			} else {
				Intent create = new Intent(this, CreateAppointment.class);
				create.putExtra("date", selectedDate);// passing the selected
														// date
				startActivity(create);
			}
			break;
		case R.id.butViewAppointment:// view button event
			if (selectedDate == null || selectedDate == "") {
				Toast.makeText(getApplicationContext(),
						"Please select a date!", Toast.LENGTH_SHORT).show();
			} else {
				Intent view = new Intent(this, ViewAppointments.class);
				view.putExtra("date", selectedDate);// passing the selected date
				startActivity(view);
			}
			break;
		case R.id.butDeleteAppointment:// delete button event
			if (selectedDate == null || selectedDate == "") {
				Toast.makeText(getApplicationContext(),
						"Please select a date!", Toast.LENGTH_SHORT).show();
			} else {
				Intent delete = new Intent(this, DeleteAppointments.class);
				delete.putExtra("date", selectedDate);// passing the selected
														// date
				startActivity(delete);
			}
			break;
		case R.id.butMoveAppointment:// move button event
			if (selectedDate == null || selectedDate == "") {
				Toast.makeText(getApplicationContext(),
						"Please select a date!", Toast.LENGTH_SHORT).show();
			} else {
				Intent move = new Intent(this, MoveAppointment.class);
				move.putExtra("date", selectedDate);// passing the selected date
				startActivity(move);
			}
			break;
		case R.id.butSearchAppointment:// search button event
			if (selectedDate == null || selectedDate == "") {
				Toast.makeText(getApplicationContext(),
						"Please select a date!", Toast.LENGTH_SHORT).show();
			} else {
				Intent search = new Intent(this, SearchAppointments.class);
				search.putExtra("date", selectedDate);// passing the selected
														// date
				startActivity(search);
			}
			break;
		case R.id.butTranslateAppointment:// translate button event
			if (selectedDate == null || selectedDate == "") {
				Toast.makeText(getApplicationContext(),
						"Please select a date!", Toast.LENGTH_SHORT).show();
			} else {
				Intent translate = new Intent(this, TranslateAppointment.class);
				translate.putExtra("date", selectedDate);// passing the selected
															// date
				startActivity(translate);
			}
			break;
		}
	}

}
