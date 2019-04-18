/*
 * TranslateAppointment.java
 * Class used to translate an appointment's description
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.net.*;

import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import android.text.*;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class TranslateAppointment extends Activity implements OnClickListener {

	private String currentDate;
	private static final String TAG = "TRANSLATE";
	private Spinner fromSpinner;
	private Spinner toSpinner;
	private TextView origText;
	private TextView transtext;
	private TextView show;
	private Button transbutton;
	private Button updatebutton;
	private EditText id;
	private String fromLang;
	private String toLang;
	private String tempID;
	private String temp_desc;
	private String translated_desc;

	private TextWatcher textWatcher;
	private OnItemSelectedListener itemListener;
	private OnClickListener buttonListener;

	private String accessToken;
	private EventsData events;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.translateappo);
		Bundle bundle = getIntent().getExtras();
		currentDate = bundle.getString("date");
		findViews();// initialize the views
		setAdapters();// setting the adapters for the spinners
		setListeners();// setting the listeners
		displayRecords();// displaying all the records
		new GetAccessTokenTask().execute();
	}

	private void displayRecords() {
		events = new EventsData(this);
		try {
			Cursor cursor = getEvents();
			showEvents(cursor);
		} finally {
			events.close();
		}
	}

	private Cursor getEvents() {
		SQLiteDatabase db = events.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE DATE = '" + currentDate + "'", null);
		startManagingCursor(cursor);
		return cursor;
	}

	private void showEvents(Cursor cursor) {
		StringBuilder builder = new StringBuilder("Appointments of: "
				+ currentDate + "\n****************************************\n");
		while (cursor.moveToNext()) {
			long id = cursor.getLong(0);
			String title = cursor.getString(1);
			String time = cursor.getString(2);
			String desc = cursor.getString(3);
			builder.append(id).append(".\t\t");
			builder.append(time).append("\t\t");
			builder.append(title).append("\t\t");
			builder.append(desc).append("\n");
		}
		show.setText(builder);

	}

	private void findViews() {
		fromSpinner = (Spinner) findViewById(R.id.from_language);
		toSpinner = (Spinner) findViewById(R.id.to_language);
		origText = (TextView) findViewById(R.id.desc);
		id = (EditText) findViewById(R.id.trans_edit_enter_id);
		show = (TextView) findViewById(R.id.trans_show_records);
		transtext = (TextView) findViewById(R.id.translated_text);
		transbutton = (Button) findViewById(R.id.translate_button);
		updatebutton = (Button) findViewById(R.id.update_button);

		updatebutton.setOnClickListener(this);
	}

	private void setAdapters() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.languages, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		fromSpinner.setAdapter(adapter);
		toSpinner.setAdapter(adapter);

		fromSpinner.setSelection(8);// english
		toSpinner.setSelection(25);// spanish
	}

	private void setListeners() {
		textWatcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				// doTranslate2(origText.getText().toString().trim(),fromLang,toLang);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		};
		origText.addTextChangedListener(textWatcher);
		itemListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView parent, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				fromLang = getLang(fromSpinner);
				toLang = getLang(toSpinner);

				if (accessToken != null)
					doTranslate2(origText.getText().toString().trim(),
							fromLang, toLang);
			}

			@Override
			public void onNothingSelected(AdapterView parent) {
				// TODO Auto-generated method stub

			}
		};
		fromSpinner.setOnItemSelectedListener(itemListener);
		toSpinner.setOnItemSelectedListener(itemListener);

		buttonListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setEvent();
				if (accessToken != null)
					doTranslate2(origText.getText().toString().trim(),
							fromLang, toLang);
			}
		};
		transbutton.setOnClickListener(buttonListener);
	}

	private Cursor getDesc() {
		tempID = id.getText().toString();
		SQLiteDatabase db = events.getReadableDatabase();
		Cursor getID = db.rawQuery(
				"SELECT DESC FROM appointments WHERE _ID = '" + tempID + "'",
				null);
		return getID;
	}

	private void setEvent() {
		Cursor c = null;
		c = getDesc();
		if (c.getCount() != 0) {
			if (c.moveToFirst()) {
				do {
					temp_desc = c.getString(c.getColumnIndex(DESC));
				} while (c.moveToNext());
			}
		}
		origText.setText(temp_desc);
		// Toast.makeText(getApplicationContext(), temp_desc,
		// Toast.LENGTH_SHORT).show();
		c.close();
	}

	// function to update the records
	private void updateRecords() {
		tempID = id.getText().toString();
		if (tempID.matches("")) {
			Toast.makeText(getApplicationContext(), "Insert ID",
					Toast.LENGTH_SHORT).show();
		} else if (translated_desc.matches("")) {
			Toast.makeText(getApplicationContext(), "Please translate first",
					Toast.LENGTH_SHORT).show();
		} else {
			SQLiteDatabase db = events.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DESC, translated_desc);
			db.update(TABLE_NAME, values, "_ID=?",
					new String[] { tempID.toString() });
			Toast.makeText(getApplicationContext(),
					"Appointment Updated Successfully", Toast.LENGTH_SHORT)
					.show();
			db.close();
		}
	}

	private String getLang(Spinner spinner) {
		String result = spinner.getSelectedItem().toString();
		int lparen = result.indexOf('(');
		int rparen = result.indexOf(')');
		result = result.substring(lparen + 1, rparen);
		return result;
	}

	private void doTranslate2(String original, String from, String to) {
		if (accessToken != null)
			new TranslationTask().execute(original, from, to);
	}

	private class TranslationTask extends AsyncTask<String, Void, String> {

		protected void onPostExecute(String translation) {
			transtext.setText(translation);
			translated_desc = translation;
		}

		@Override
		protected String doInBackground(String... s) {
			// TODO Auto-generated method stub
			HttpURLConnection con2 = null;

			String result = getResources().getString(R.string.translate_error);
			String original = s[0];
			String from = s[1];
			String to = s[2];

			try {
				BufferedReader reader;

				String uri = "http://api.microsofttranslator.com"
						+ "/v2/Http.svc/Translate?text="
						+ URLEncoder.encode(original) + "&from=" + from
						+ "&to=" + to;
				URL url_translate = new URL(uri);
				String authToken = "Bearer" + " " + accessToken;
				con2 = (HttpURLConnection) url_translate.openConnection();
				con2.setRequestProperty("Authorization", authToken);
				con2.setDoInput(true);
				con2.setReadTimeout(1000);// milli
				con2.setConnectTimeout(15000);

				reader = new BufferedReader(new InputStreamReader(
						con2.getInputStream(), "UTF-8"));
				String translated_xml = reader.readLine();
				reader.close();

				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document doc = builder.parse(new InputSource(new StringReader(
						translated_xml)));
				NodeList node_list = doc.getElementsByTagName("string");
				NodeList l = node_list.item(0).getChildNodes();

				Node node;
				String translated = null;
				if (l != null && l.getLength() > 0) {
					node = l.item(0);
					translated = node.getNodeValue();
				}
				if (translated != null) {
					result = translated;
				}
			} catch (IOException e) {
				Log.e(TAG, "IOException", e);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (con2 != null) {
					con2.disconnect();
				}
			}
			return result;
		}
	}

	private class GetAccessTokenTask extends AsyncTask<Void, Void, String> {
		protected void onPostExecute(String access_token) {
			accessToken = access_token;
		}

		@Override
		protected String doInBackground(Void... v) {
			// TODO Auto-generated method stub
			String result = null;
			HttpURLConnection con = null;

			String clientID = "praveenTranslateTest";// client id from Microsoft
			String clientSecret = "ZOwYrDa7W9yTQN7toODHM2YPg+SvIRXpC5lnKPfYoPM=";// client
																					// secret
																					// from
																					// Microsoft

			String strTranslatorAccessURI = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";
			String strRequestDetails = "grant_type="
					+ " client_credentials&client_id="
					+ URLEncoder.encode(clientID) + "&client_secret="
					+ URLEncoder.encode(clientSecret)
					+ "&scope=http://api.microsofttranslator.com";

			try {
				URL url = new URL(strTranslatorAccessURI);
				con = (HttpURLConnection) url.openConnection();
				con.setReadTimeout(10000);
				con.setConnectTimeout(15000);
				con.setRequestMethod("POST");

				con.setDoInput(true);
				con.setDoOutput(true);
				con.setChunkedStreamingMode(0);

				con.connect();

				OutputStream out = new BufferedOutputStream(
						con.getOutputStream());
				out.write(strRequestDetails.getBytes());
				out.flush();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "UTF-8"));
				String payload = reader.readLine();
				reader.close();
				out.close();

				JSONObject jsonObject = new JSONObject(payload);
				result = jsonObject.getString("access_token");
			} catch (IOException e) {
				Log.e(TAG, "IOException", e);
			} catch (JSONException e) {
				Log.e(TAG, "JSONException", e);
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}
			return result;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.update_button:// update button event
			updateRecords();
			displayRecords();
			id.setText("");
			origText.setText("");
			transtext.setText("");
			break;
		}
	}

}
