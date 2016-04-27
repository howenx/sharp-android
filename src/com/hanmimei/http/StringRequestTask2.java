package com.hanmimei.http;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
/**
 * 重写 StringRequest string 传参
 * @author vince
 *
 */
public class StringRequestTask2 extends StringRequest {

	private String param;
	private Map<String, String> headers;

	public StringRequestTask2(int method, String url,
			Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
	}

	public StringRequestTask2(int method, Map<String, String> headers,
			String path, Listener<String> listener,
			ErrorListener errorListener, String param) throws IOException {
		super(method, path, listener, errorListener);
		if (param != null) {
			this.param = param;
		}
		this.headers = new HashMap<String, String>();
		this.headers.put("Content-Type", "application/json");
		if (headers != null) {
			this.headers.putAll(headers);
		}
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		return param == null ? super.getBody() : param.getBytes(Charset
				.forName("UTF-8"));
	}
}
