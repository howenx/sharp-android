package com.hanmimei.utils;

import java.io.IOException;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

/**
 * 重写  StringRequest  Map 传参
 * @author 刘奇
 *
 */

public class PostStringRequest extends StringRequest{

	private Map<String, String> map;
	
	public PostStringRequest(int method, String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		
	}


	public PostStringRequest(String path, Listener<String> listener,
			ErrorListener errorListener) {
		super(path, listener, errorListener);
	}

	public PostStringRequest(int method, String path, Listener<String> listener,
			ErrorListener errorListener,Map<String, String> map)  throws IOException {
		super(method, path, listener, errorListener);
		if(map !=null){
			this.map=map;
		}
		
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		
		return this.map;
	}

}

