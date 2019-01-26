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
import android.widget.ImageButton;
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
	class RunnableQuery implements Runnable {
		private Handler mHandler;
		private boolean mRun = false;
		private MainActivity context;

		private RunnableQuery(MainActivity context) {
			mHandler = new Handler(getMainLooper());
			this.context = context;
		}

		private void start() {
			if (mRun) {
				return;
			}
			mRun = true;
			context.sendRequest("query", context);
			mHandler.postDelayed(this, 2000);
		}

		void stop() {
			if (!mRun) {
				return;
			}
			mRun = false;
			mHandler.removeCallbacks(this);
		}

		@Override
		public void run() {
			if (!mRun) {
				return;
			}
			context.sendRequest("query", context);
			mHandler.postDelayed(this, 2000);
		}
	}

	RunnableQuery runnableQuery;

	public void sendRequest(String endpoint, final Context context) {
		RequestQueue queue = Volley.newRequestQueue(context);

		SharedPreferences settings = context.getSharedPreferences(
				context.getApplicationContext().getPackageName() + "_preferences", 0);
		String host = settings.getString("pref_host", "");
		if (host == null || host.equals("")) {
			Toast.makeText(context, "No host set", Toast.LENGTH_SHORT).show();
			return;
		}

		String url ="http://" + host + "/" + endpoint;
		JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				if (response.length() > 0) {
					try {
						TextView statusTextView = findViewById(R.id.statusText);
						TextView albumTextView = findViewById(R.id.albumText);
						ImageButton playButton = findViewById(R.id.playButton);
						ImageButton shuffleButton = findViewById(R.id.shuffleButton);

						String current = response.getString("artist") + " - " + response.getString("title");
						statusTextView.setText(current);
						albumTextView.setText(response.getString("album"));
						if (response.getBoolean("playing")) {
							playButton.setImageResource(R.drawable.ic_pause_black_24dp);
						} else {
							playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
						}
						if (response.getBoolean("shuffle")) {
							shuffleButton.setColorFilter(getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
						} else {
							shuffleButton.setColorFilter(getColor(R.color.colorForeground), android.graphics.PorterDuff.Mode.SRC_IN);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
					Toast.makeText(context, volleyError.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
				}
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

		final MainActivity context = this;

		View.OnClickListener buttonClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.muteButton:
						context.sendRequest("mute", context);
						break;
					case R.id.volDownButton:
						context.sendRequest("vol_down", context);
						break;
					case R.id.volUpButton:
						context.sendRequest("vol_up", context);
						break;
					case R.id.prevButton:
						context.sendRequest("prev", context);
						context.sendRequest("query", context);
						break;
					case R.id.playButton:
						context.sendRequest("play", context);
						context.sendRequest("query", context);
						break;
					case R.id.nextButton:
						context.sendRequest("next", context);
						context.sendRequest("query", context);
						break;
					case R.id.shuffleButton:
						context.sendRequest("shuffle", context);
						context.sendRequest("query", context);
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

		this.runnableQuery = new RunnableQuery(this);
		this.runnableQuery.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.runnableQuery.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.runnableQuery.stop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.runnableQuery.stop();
	}
}
