/*
 * ViewAppointments.java
 * Class to view and edit appointments
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
import android.content.ContentValues;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAppointments extends Activity implements android.view.View.OnClickListener{
	
	private static String[] FROM = {_ID,TITLE,TIME,DESC,DATE};
	private static String ORDER_BY = "_ID";

	private String currentDate=null;
	private String temp_ID=null;
	private EventsData events;
	private TextView view=null;
	private Button update=null;
	private EditText title=null,time=null,desc=null,id=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewappo);
		Bundle bundle = getIntent().getExtras();
		currentDate = bundle.getString("date");
		initControls();
		displayRecords();
	}
	
	//display all the records
	private void displayRecords()
	{
		events = new EventsData(this);
		try
		{
			Cursor cursor = getEvents();
			showEvents(cursor);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			events.close();
		}
	}
	
	//initialize the views
	private void initControls()
	{
		view =(TextView)findViewById(R.id.display);
		update = (Button)findViewById(R.id.butEdit);
		id = (EditText)findViewById(R.id.delEnterID);
		title = (EditText)findViewById(R.id.enterTitle);
		time = (EditText)findViewById(R.id.enterTime);
		desc = (EditText)findViewById(R.id.enterDesc);
		
		update.setOnClickListener(this);
	}
	
	//function to clear all editText boxes
	private void clearFields()
	{
		id.setText("");
		title.setText("");
		time.setText("");
		desc.setText("");
	}
	
	//function to update the records
	private void updateRecords(String title,String time,String desc)
	{
		temp_ID = id.getText().toString();
		if(temp_ID.matches(""))//check if id is entered
		{
			Toast.makeText(getApplicationContext(), "Insert ID", Toast.LENGTH_SHORT).show();
		}
		else
		{
			if(title.matches("") || time.matches("") || desc.matches(""))//check if all fields are entered
			{
				Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
			}
			else
			{	
				SQLiteDatabase db = events.getWritableDatabase();//update database using .update() function
				ContentValues values = new ContentValues();
				values.put(TITLE, title);
				values.put(TIME, time);
				values.put(DESC, desc);
				db.update(TABLE_NAME, values,_ID +"=?", new String[] {temp_ID});
				Toast.makeText(getApplicationContext(), "Appointment Updated Successfully", Toast.LENGTH_SHORT).show();
				db.close();
			}
		}
		
		
	}
	
	//function to get all the events
	private Cursor getEvents()
	{
		SQLiteDatabase db = events.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME+ " WHERE DATE = '"+currentDate+"'", null);
		startManagingCursor(cursor);
		return cursor;
	}
	
	//function to show all the records
	private void showEvents(Cursor cursor)
	{
		StringBuilder builder = new StringBuilder("Appointments of: "+ currentDate + "\n***************************\n");
		while(cursor.moveToNext())
		{
			long id = cursor.getLong(0);
			String title = cursor.getString(1);
			String time = cursor.getString(2);
			String desc = cursor.getString(3);
			builder.append(id).append(".\t\t");
			builder.append(time).append("\t\t");
			builder.append(title).append("\t\t");
			builder.append(desc).append("\n");
		}
		view.setText(builder);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.butEdit://edit button event
			updateRecords(title.getText().toString(),time.getText().toString(),desc.getText().toString());
			clearFields();
			displayRecords();
			break;
		}
	}

	
}
