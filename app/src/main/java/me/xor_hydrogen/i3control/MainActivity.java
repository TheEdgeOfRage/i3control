package me.xor_hydrogen.i3control;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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
		ImageButton muteButton = findViewById(R.id.muteButton);
		ImageButton volDownButton = findViewById(R.id.volDownButton);
		ImageButton volUpButton = findViewById(R.id.volUpButton);
		ImageButton prevButton = findViewById(R.id.prevButton);
		ImageButton playButton = findViewById(R.id.playButton);
		ImageButton nextButton = findViewById(R.id.nextButton);

		View.OnClickListener buttonClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.muteButton:
						ApiService.sendRequest("mute", context);
						break;
					case R.id.volDownButton:
						ApiService.sendRequest("vol_down", context);
						break;
					case R.id.volUpButton:
						ApiService.sendRequest("vol_up", context);
						break;
					case R.id.prevButton:
						ApiService.sendRequest("prev", context);
						break;
					case R.id.playButton:
						ApiService.sendRequest("play", context);
						break;
					case R.id.nextButton:
						ApiService.sendRequest("next", context);
						break;
				}
			}
		};

		muteButton.setOnClickListener(buttonClickListener);
		volDownButton.setOnClickListener(buttonClickListener);
		volUpButton.setOnClickListener(buttonClickListener);
		prevButton.setOnClickListener(buttonClickListener);
		playButton.setOnClickListener(buttonClickListener);
		nextButton.setOnClickListener(buttonClickListener);
	}
}
