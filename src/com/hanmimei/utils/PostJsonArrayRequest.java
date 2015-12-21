package com.hanmimei.utils;

import org.json.JSONArray;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;

public class PostJsonArrayRequest extends JsonArrayRequest {

	public PostJsonArrayRequest(String url, Listener<JSONArray> listener,
			ErrorListener errorListener) {
		super(url, listener, errorListener);
	}

	
	

}
