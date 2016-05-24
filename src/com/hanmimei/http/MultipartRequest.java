package com.hanmimei.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

public class MultipartRequest extends Request<JSONObject> {

	private VolleyResponseListener listener = null;
	private MultipartRequestParams params = null;
	private HttpEntity httpEntity = null;
	private Map<String, String> header = null;

	public MultipartRequest(int method, MultipartRequestParams params,
			String url, VolleyResponseListener listener,
			Map<String, String> header) {
		super(method, url, null);
		this.params = params;
		this.listener = listener;
		this.header = header;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			if (params != null) {
				httpEntity = params.getEntity();
				if (httpEntity != null) {
					httpEntity.writeTo(baos);
				}
				return baos.toByteArray();
			}
		} catch (IOException e) {
			
		}
		return null;
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			listener.setFlag(false);
			return Response.error(new ParseError(response));
		} catch (JSONException je) {
			listener.setFlag(false);
			return Response.error(new ParseError(response));
		}
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// TODO Auto-generated method stub
		Map<String, String> headers = super.getHeaders();
		if (null == headers || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}
		if (header != null) {
			headers.putAll(header);
		}
		return headers;
	}

	@Override
	public String getBodyContentType() {
		// TODO Auto-generated method stub
		return httpEntity.getContentType().getValue();
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		// TODO Auto-generated method stub
		listener.onResponse(response);
	}

	@Override
	public void deliverError(VolleyError error) {
		// TODO Auto-generated method stub
		if (listener != null) {
			listener.onErrorResponse(error);
		}
	}

	public interface VolleyResponseListener {
		void onResponse(JSONObject response);

		void setFlag(boolean flag);

		void onErrorResponse(VolleyError error);
	}
	
	
}
