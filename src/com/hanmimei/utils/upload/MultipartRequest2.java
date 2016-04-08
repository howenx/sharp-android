package com.hanmimei.utils.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

public class MultipartRequest2 extends Request<String> {

	private org.apache.http.entity.mime.MultipartEntity entity = new org.apache.http.entity.mime.MultipartEntity();

	private final Response.Listener<String> mListener;

	private List<File> mFileParts;
	private String mFilePartName;
	private Map<String, String> mParams;
	
	private Map<String,String> mHeader ;
	/**
	 * 单个文件
	 * @param url
	 * @param errorListener
	 * @param listener
	 * @param filePartName
	 * @param file
	 * @param params
	 */
	public MultipartRequest2(String url, Response.ErrorListener errorListener,
			Response.Listener<String> listener, String filePartName, File file,
			Map<String, String> params,Map<String,String> header) {
		super(Method.POST, url, errorListener);

		mFileParts = new ArrayList<File>();
		if (file != null) {
			mFileParts.add(file);
		}
		this.mFilePartName = filePartName;
		this.mListener = listener;
		this.mParams = params;
		this.mHeader =header;
		buildMultipartEntity();
	}
	/**
	 * 多个文件，对应一个key
	 * @param url
	 * @param errorListener
	 * @param listener
	 * @param filePartName
	 * @param files
	 * @param params
	 */
	public MultipartRequest2(String url, Response.ErrorListener errorListener,
			Response.Listener<String> listener, String filePartName,
			List<File> files, Map<String, String> params,Map<String,String> header) {
		super(Method.POST, url, errorListener);
		this.mFilePartName = filePartName;
		this.mListener = listener;
		this.mFileParts = files;
		this.mParams = params;
		this.mHeader = header;
		buildMultipartEntity();
	}

	private void buildMultipartEntity() {
		if (mFileParts != null && mFileParts.size() > 0) {
			for (File file : mFileParts) {
				entity.addPart(mFilePartName, new FileBody(file));
			}
			long l = entity.getContentLength();
//			CLog.log(mFileParts.size()+"个，长度："+l);
		}

		try {
			if (mParams != null && mParams.size() > 0) {
				for (Map.Entry<String, String> entry : mParams.entrySet()) {
					entity.addPart(
							entry.getKey(),
							new StringBody(entry.getValue(), Charset
									.forName("UTF-8")));
				}
			}
		} catch (UnsupportedEncodingException e) {
			VolleyLog.e("UnsupportedEncodingException");
		}
	}

	@Override
	public String getBodyContentType() {
		return entity.getContentType().getValue();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			entity.writeTo(bos);
		} catch (IOException e) {
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
//		CLog.log("parseNetworkResponse");
		if (VolleyLog.DEBUG) {
			if (response.headers != null) {
				for (Map.Entry<String, String> entry : response.headers
						.entrySet()) {
					VolleyLog.d(entry.getKey() + "=" + entry.getValue());
				}
			}
		}

		String parsed;
		try {
			parsed = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed,
				HttpHeaderParser.parseCacheHeaders(response));
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.volley.Request#getHeaders()
	 */
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		VolleyLog.d("getHeaders");
		Map<String, String> headers = super.getHeaders();

		if (headers == null || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}
		if(this.mHeader !=null){
			headers.putAll(mHeader);
		}
		
		return headers;
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}
}
