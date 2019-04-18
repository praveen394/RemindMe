/*
 * Appointments.java
 * Class to hold the values of the appointment. 
 * This class is used for the search function
 * Author : Praveen Naresh 			2012053
 */
package com.Praveen.remindme;

public class Appointments {

	// class variables
	// *********************
	private long _ID;
	private String TITLE;
	private String TIME;
	private String DESC;
	private String DATE;

	// *********************

	// Getters & setters
	// *********************
	public long get_ID() {
		return _ID;
	}

	public void set_ID(long _ID) {
		this._ID = _ID;
	}

	public String getTITLE() {
		return TITLE;
	}

	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}

	public String getTIME() {
		return TIME;
	}

	public void setTIME(String tIME) {
		TIME = tIME;
	}

	public String getDESC() {
		return DESC;
	}

	public void setDESC(String dESC) {
		DESC = dESC;
	}

	public String getDATE() {
		return DATE;
	}

	public void setDATE(String dATE) {
		DATE = dATE;
	}
	// *********************

}
