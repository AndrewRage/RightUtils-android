package com.rightutils.example.applications;

import android.app.Application;
import android.util.Log;

import com.rightutils.example.db.DBUtils;

/**
 * Created by Anton Maniskevich on 1/20/15.
 */
public class ExampleApplication extends Application {

	public static DBUtils dbUtils;

	@Override
	public void onCreate() {
		Log.d("ExampleApplication", "onCreate");
		super.onCreate();
		dbUtils = DBUtils.getInstance(this);
	}
}
