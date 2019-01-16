package com.theedgeofrage.i3control;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
	public void sendRequest(String endpoint, final Context context) {
		RequestQueue queue = Volley.newRequestQueue(context);

		SharedPreferences settings = context.getSharedPreferences(
				context.getApplicationContext().getPackageName() + "_preferences", 0);
		String host = settings.getString("pref_host", "");
		if (host.equals("")) {
			Toast.makeText(context, "No host set", Toast.LENGTH_SHORT).show();
			return;
		}

		String url ="http://" + host + "/" + endpoint;
		JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				if (response.length() > 0) {
					try {
						String current = response.getString("artist") + " - " + response.getString("title");
						((TextView)findViewById(R.id.currentlyPlayingText)).setText(current);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		});

		queue.add(stringRequest);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				startActivity(new Intent(MainActivity.this, SettingsActivity.class));
				break;
		}
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Context context = this;

		View.OnClickListener buttonClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.muteButton:
						((MainActivity)context).sendRequest("mute", context);
						break;
					case R.id.volDownButton:
						((MainActivity)context).sendRequest("vol_down", context);
						break;
					case R.id.volUpButton:
						((MainActivity)context).sendRequest("vol_up", context);
						break;
					case R.id.prevButton:
						((MainActivity)context).sendRequest("prev", context);
						break;
					case R.id.playButton:
						((MainActivity)context).sendRequest("play", context);
						break;
					case R.id.nextButton:
						((MainActivity)context).sendRequest("next", context);
						break;
					case R.id.shuffleButton:
						((MainActivity)context).sendRequest("shuffle", context);
						break;
				}
			}
		};

		findViewById(R.id.muteButton).setOnClickListener(buttonClickListener);
		findViewById(R.id.volDownButton).setOnClickListener(buttonClickListener);
		findViewById(R.id.volUpButton).setOnClickListener(buttonClickListener);
		findViewById(R.id.prevButton).setOnClickListener(buttonClickListener);
		findViewById(R.id.playButton).setOnClickListener(buttonClickListener);
		findViewById(R.id.nextButton).setOnClickListener(buttonClickListener);
		findViewById(R.id.shuffleButton).setOnClickListener(buttonClickListener);

		class RunnableQuery implements Runnable {
			private Handler mHandler;
			private boolean mRun = false;

			RunnableQuery() {
				mHandler = new Handler(getMainLooper());
			}

			public void start() {
				mRun = true;
				mHandler.postDelayed(this, 1000);
			}

			void stop() {
				mRun = false;
				mHandler.removeCallbacks(this);
			}

			@Override
			public void run() {
				if (!mRun) {
					return;
				}
				((MainActivity) context).sendRequest("query", context);
				mHandler.postDelayed(this, 1000);
			}
		}

		RunnableQuery runnableQuery = new RunnableQuery();
		runnableQuery.start();
	}
}
