package me.xor_hydrogen.i3control;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

class ApiService {
	static void sendRequest(String endpoint, Context context) {
		RequestQueue queue = Volley.newRequestQueue(context);
		String url ="http://router/" + endpoint;
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
