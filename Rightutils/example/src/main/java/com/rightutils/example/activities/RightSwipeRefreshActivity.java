package com.rightutils.example.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rightutils.example.R;
import com.rightutils.rightutils.collections.RightList;
import com.rightutils.rightutils.widgets.RightSwipeRefreshLayout;

/**
 * Created by Anton Maniskevich on 5/13/15.
 */
public class RightSwipeRefreshActivity extends AppCompatActivity implements RightSwipeRefreshLayout.OnRefreshListener {

	private static final String TAG = RightSwipeRefreshActivity.class.getSimpleName();
	private RightSwipeRefreshLayout rightSwipeRefreshLayout;
	private ArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_right_swipe_refresh);
		RightList<String> values = RightList.asRightList("100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114");
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
		((ListView)findViewById(R.id.list_view)).setAdapter(adapter);
		rightSwipeRefreshLayout = (RightSwipeRefreshLayout) findViewById(R.id.refresh);
		rightSwipeRefreshLayout.setOnRefreshListener(this);
	}


	@Override
	public void onRefresh(final @RightSwipeRefreshLayout.RefreshType int refreshType) {
		rightSwipeRefreshLayout.setRefreshing(true, refreshType);
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				switch (refreshType) {
					case RightSwipeRefreshLayout.TOP_REFRESH:
						int firstValue = Integer.valueOf((String) adapter.getItem(0));
						if (firstValue > 90) {
							adapter.insert(String.valueOf(--firstValue), 0);
							adapter.insert(String.valueOf(--firstValue), 0);
						}
						break;
					case RightSwipeRefreshLayout.BOTTOM_REFRESH:
						int lastValue = Integer.valueOf((String) adapter.getItem(adapter.getCount()-1));
						Log.i(TAG, "lastValue="+lastValue);
						if (lastValue < 124) {
							adapter.add(String.valueOf(++lastValue));
							adapter.add(String.valueOf(++lastValue));
						}
						break;
				}
				adapter.notifyDataSetChanged();
				rightSwipeRefreshLayout.setRefreshing(false, refreshType);
			}
		}.execute();
	}
}
