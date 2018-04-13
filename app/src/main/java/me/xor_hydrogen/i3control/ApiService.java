package me.xor_hydrogen.i3control;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

class ApiService {
	static void sendRequest(String endpoint, Context context) {
		RequestQueue queue = Volley.newRequestQueue(context);

		SharedPreferences settings = context.getSharedPreferences(
				context.getApplicationContext().getPackageName() + "_preferences", 0);
		String host = settings.getString("pref_host", "");
		if (host.equals("")) {
			Toast.makeText(context, "No host set", Toast.LENGTH_SHORT).show();
			return;
		}

		String url ="http://" + host + "/" + endpoint;
		Log.i(context.getClass().getName(), "Sending request to: " + url);

		StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		});

		queue.add(stringRequest);
	}
}
