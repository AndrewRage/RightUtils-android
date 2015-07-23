package com.rightutils.example.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.rightutils.example.R;
import com.rightutils.example.applications.ExampleApplication;
import com.rightutils.example.entities.Company;
import com.rightutils.example.provider.ExampleProvider;
import com.rightutils.rightutils.collections.RightList;
import com.rightutils.rightutils.db.RightContentUtils;

/**
 * Created by Anton Maniskevich on 3/31/15.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btn_custom_font_activity).setOnClickListener(this);
		findViewById(R.id.btn_loader_activity).setOnClickListener(this);
		findViewById(R.id.btn_fragment_loader).setOnClickListener(this);
		findViewById(R.id.btn_right_refresh_listview).setOnClickListener(this);
		findViewById(R.id.btn_right_refresh_recyclerview).setOnClickListener(this);
		findViewById(R.id.btn_right_refresh_viewpager).setOnClickListener(this);

		getSupportLoaderManager().initLoader(1, null, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_custom_font_activity:
				startActivity(new Intent(MainActivity.this, CustomFontActivity.class));
				break;
			case R.id.btn_loader_activity:
				startActivity(new Intent(MainActivity.this, LoaderActivity.class));
				break;
			case R.id.btn_fragment_loader:
				startActivity(new Intent(MainActivity.this, FragmentLoaderActivity.class));
				break;
			case R.id.btn_right_refresh_listview:
				startActivity(new Intent(MainActivity.this, RightSwipeRefreshActivity.class));
				break;
			case R.id.btn_right_refresh_recyclerview:
				startActivity(new Intent(MainActivity.this, RightSwipeRefreshRecycleViewActivity.class));
				break;
			case R.id.btn_right_refresh_viewpager:
				startActivity(new Intent(MainActivity.this, RightSwipeRefreshViewPagerActivity.class));
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getSupportLoaderManager().destroyLoader(1);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch (id) {
			case 1:
				return new CursorLoader(
						MainActivity.this,
						ExampleProvider.getUri(Company.class),
						null,
						null,
						null,
						null
				);
			default:
				return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(TAG, "onLoadFinished " + loader + " data: " + data);
		RightList<Company> companyList = new RightContentUtils().cursorMapper(data, Company.class);
		Log.d(TAG, "companyList: " + companyList);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(TAG, "onLoaderReset " + loader);
	}
}
