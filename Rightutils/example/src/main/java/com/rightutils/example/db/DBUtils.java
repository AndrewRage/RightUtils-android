package com.rightutils.example.db;

import android.content.Context;

import com.rightutils.rightutils.db.RightDBUtils;

/**
 * Created by Anton Maniskevich on 1/20/15.
 */
public class DBUtils extends RightDBUtils {

	private static final String TAG = DBUtils.class.getSimpleName();

	private static DBUtils dbUtils;

	private DBUtils(){
	}

	public static DBUtils getInstance(Context context) {
		if (dbUtils == null) {
			dbUtils = new DBUtils();
			dbUtils.setDBContext(context, "example.sqlite", 1);
		}
		return dbUtils;
	}
}
