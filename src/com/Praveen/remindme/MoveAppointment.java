/*
 * MoveAppointment.java
 * Class used to move the appointment from one date to another
 * Author : Praveen Naresh 			2012053
 */
package com.Praveen.remindme;

import static android.provider.BaseColumns._ID;
import static com.Praveen.remindme.Constants.DATE;
import static com.Praveen.remindme.Constants.DESC;
import static com.Praveen.remindme.Constants.TABLE_NAME;
import static com.Praveen.remindme.Constants.TIME;
import static com.Praveen.remindme.Constants.TITLE;
import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MoveAppointment extends Activity implements OnClickListener {

	private String currentDate = null;
	private String temp_ID = null;
	private String temp_Date = null;

	private EventsData events;

	private int mYear;
	private int mMonth;
	private int mDay;

	private TextView mDateDisplay = null;
	private Button mPickDate = null;
	private TextView view = null;
	private EditText id = null;
	private Button update = null;

	static final int DATE_DIALOG_ID = 0;
	final Calendar c = Calendar.getInstance();

	private static String[] FROM = { _ID, TITLE, TIME, DESC, DATE };
	private static String ORDER_BY = "_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moveappo);
		Bundle bundle = getIntent().getExtras();
		currentDate = bundle.getString("date");// getting the selected date
		initControls();
		fillDialog();// fill the date picker
		displayRecords();// show all records

	}

	// function to initalize the views
	private void initControls() {
		mDateDisplay = (TextView) findViewById(R.id.showMyDate);
		mPickDate = (Button) findViewById(R.id.myDatePickerButton);

		view = (TextView) findViewById(R.id.moveDisplay);
		id = (EditText) findViewById(R.id.moveEnterID);
		update = (Button) findViewById(R.id.butUpdateDate);

		update.setOnClickListener(this);
	}

	// function to show all records
	private void displayRecords() {
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

	// function to get all records
	private Cursor getEvents() {
		SQLiteDatabase db = events.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE DATE = '" + currentDate + "'", null);
		startManagingCursor(cursor);
		return cursor;
	}

	// function to set the records
	private void showEvents(Cursor cursor) {
		StringBuilder builder = new StringBuilder("Appointments of: "
				+ currentDate + "\n***************************\n");
		while (cursor.moveToNext()) {
			long id = cursor.getLong(0);
			String title = cursor.getString(1);
			String time = cursor.getString(2);
			builder.append(id).append(".\t\t");
			builder.append(time).append("\t\t");
			builder.append(title).append("\n");
		}
		view.setText(builder);
	}

	// add the dates to the date picker
	private void fillDialog() {
		mPickDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});

		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		updateDisplay();
	}

	// assigning the selected date to a textView
	private void updateDisplay() {
		this.mDateDisplay.setText(new StringBuilder().append(mDay).append("/")
				.append(mMonth + 1).append("/").append(mYear).append(" "));
	}

	// function to set the date listener
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);

		}
		return null;
	}

	// function to update the date
	private void updateDate() {
		temp_ID = id.getText().toString();
		temp_Date = mDateDisplay.getText().toString().trim();
		if (temp_ID.matches("")) {// check if user has entered the id
			Toast.makeText(getApplicationContext(), "Insert ID",
					Toast.LENGTH_SHORT).show();
		} else if (temp_Date.matches("")) {// check if user has selected a date
			Toast.makeText(getApplicationContext(), "Select date",
					Toast.LENGTH_SHORT).show();
		} else {
			SQLiteDatabase db = events.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DATE, temp_Date);
			db.update(TABLE_NAME, values, _ID + "=" + temp_ID, null);// using
																		// the
																		// database
																		// .update
																		// function
			// db.update(TABLE_NAME, values," _ID "+"="+temp_ID,null);
			db.close();
			Toast.makeText(getApplicationContext(),
					"Appointment Updated Successfully", Toast.LENGTH_LONG)
					.show();
			id.setText("");
			mDateDisplay.setText("");

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.butUpdateDate:// update button event
			updateDate();
			displayRecords();
			break;
		}
	}

}
