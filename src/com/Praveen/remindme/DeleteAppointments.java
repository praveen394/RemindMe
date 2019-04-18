/*
 * DeleteAppointments.java
 * Class used to delete appointments
 * Author : Praveen Naresh 			2012053
 */
package com.Praveen.remindme;

import static android.provider.BaseColumns._ID;
import static com.Praveen.remindme.Constants.DATE;
import static com.Praveen.remindme.Constants.DESC;
import static com.Praveen.remindme.Constants.TABLE_NAME;
import static com.Praveen.remindme.Constants.TIME;
import static com.Praveen.remindme.Constants.TITLE;
import android.app.Activity;
import android.app.AlertDialog;
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

public class DeleteAppointments extends Activity implements OnClickListener {

	private EventsData events;
	private String currentDate;
	private String temp = null;
	private String event_temp = null;
	private String date_temp = null;
	private static String[] FROM = { _ID, TITLE, TIME, DESC, DATE };
	private static String ORDER_BY = _ID;
	private Button delAll = null, delSin = null;
	private EditText id = null;
	private TextView show = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deleteappo);
		initControls();
		Bundle bundle = getIntent().getExtras();// getting the date
		currentDate = bundle.getString("date");
		displayRecords();// show the records for a particular date

	}

	// function to show the all the records
	private void displayRecords() {
		events = new EventsData(this);
		try {
			Cursor cursor = getEvents();
			showEvents(cursor);
		} finally {
			events.close();
		}
	}

	// function to get the title based on the id
	private Cursor getDesc() {
		SQLiteDatabase db = events.getReadableDatabase();
		Cursor getID = db.rawQuery(
				"SELECT TITLE FROM appointments WHERE _ID = '" + temp + "'",
				null);
		return getID;
	}

	// function to assign the title to a variable
	private void setEvent() {
		Cursor c = null;
		c = getDesc();
		if (c.getCount() != 0) {
			if (c.moveToFirst()) {
				do {
					event_temp = c.getString(c.getColumnIndex(TITLE));
				} while (c.moveToNext());
			}
		}
		c.close();
	}

	// function to delete all the records on a particular date
	private void deleteAllRecords() {
		SQLiteDatabase db = events.getWritableDatabase();
		// db.delete(TABLE_NAME,null,null);
		db.delete(TABLE_NAME, DATE + "=?", new String[] { currentDate });
		db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME
				+ "'");
		db.close();
	}

	// function to delete a single record on a particular date
	private void deleteSingleRecord() {
		temp = id.getText().toString();
		if (temp.matches("")) {
			Toast.makeText(getApplicationContext(), "Insert ID",
					Toast.LENGTH_SHORT).show();
		} else {

			setEvent();
			new AlertDialog.Builder(this)
					.setTitle("Delete appointment")
					.setMessage("Would you like to delete event:" + event_temp)
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									if (event_temp == null) {// check if any
																// events exists
										Toast.makeText(getApplicationContext(),
												"No events!",
												Toast.LENGTH_SHORT).show();
										dialog.cancel();
									} else {
										SQLiteDatabase db = events
												.getWritableDatabase();
										try {
											db.delete(TABLE_NAME, _ID + " = "
													+ temp, null);// delete
																	// using the
																	// .delete
																	// function
										} catch (Exception e) {
											e.printStackTrace();
										} finally {
											db.close();
											Toast.makeText(
													getApplicationContext(),
													"Record deleted",
													Toast.LENGTH_SHORT).show();
											event_temp = null;
											displayRecords();// show records
																// after
																// deleting
										}

									}
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							}).show();
		}

	}

	// initialize all the views
	private void initControls() {
		delAll = (Button) findViewById(R.id.butDeleteAll);
		delSin = (Button) findViewById(R.id.butDeleteSingle);
		id = (EditText) findViewById(R.id.enterID);
		show = (TextView) findViewById(R.id.displayDel);

		delAll.setOnClickListener(this);
		delSin.setOnClickListener(this);
	}

	// get all the records according to the time in ascending order
	private Cursor getEvents() {
		SQLiteDatabase db = events.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE DATE = '" + currentDate + "'" + "ORDER BY TIME ASC",
				null);
		startManagingCursor(cursor);
		return cursor;
	}

	// function to show the events
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
		show.setText(builder);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.butDeleteAll:// delete all button event
			if (currentDate == "No date selected!!") {
				Toast.makeText(getApplicationContext(), "No events to delete",
						Toast.LENGTH_SHORT).show();
			} else {
				deleteAllRecords();
				Toast.makeText(getApplicationContext(),
						"All appointments for " + currentDate + " deleted",
						Toast.LENGTH_SHORT).show();
				displayRecords();
			}
			break;
		case R.id.butDeleteSingle:// delete single record event
			deleteSingleRecord();
			// displayRecords();
			id.setText("");
			break;
		}
	}

}
