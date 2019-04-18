/*
 * Constants.java
 * Class used to hold the database fields
 * Author : Praveen Naresh 			2012053
 */
package com.Praveen.remindme;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {

	public static final String TABLE_NAME = "appointments";// table name

	// other fields in the database
	public static final String TITLE = "title";
	public static final String TIME = "time";
	public static final String DESC = "desc";
	public static final String DATE = "date";
}
