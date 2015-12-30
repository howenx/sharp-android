package com.hanmimei.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class HttpUtils {

	private static final String TAG = "uploadFile";

	private static final int TIME_OUT = 5 * 1000; // 超时时间

	private static final String CHARSET = "utf-8"; // 设置编码

	private HttpUtils() {
		throw new AssertionError();
	}

	/*
	 * android 原生get请求 public static String doGet(String httpUrl) {
	 * HttpURLConnection conn = null; URL url = null; InputStream is = null;
	 * StringBuilder result = new StringBuilder(); try { url = new URL(httpUrl);
	 * conn = (HttpURLConnection) url.openConnection();
	 * conn.setConnectTimeout(TIME_OUT); conn.setReadTimeout(TIME_OUT);
	 * conn.setRequestMethod("GET"); conn.setDoInput(true);
	 * conn.setDoOutput(true); if (conn.getResponseCode() == 200) { is =
	 * conn.getInputStream(); byte[] buffer = new byte[1024]; int len = 0; while
	 * ((len = is.read(buffer)) != -1) { result.append(new String(buffer, 0,
	 * len)); } } } catch (MalformedURLException e) { e.printStackTrace(); }
	 * catch (IOException e) { // 网络连接有问题 e.printStackTrace(); } finally { if
	 * (is != null) { conn.disconnect(); } } return result.toString(); }
	 */
	// apche get请求
	public static String get(String url) {
		StringBuilder result = new StringBuilder();

		DefaultHttpClient httpClient = new DefaultHttpClient();
		InputStream is = null;
		try {

			HttpGet getRequest = new HttpGet(url);
			getRequest.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() == 200) {
				is = response.getEntity().getContent();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					result.append(new String(buffer, 0, len));
				}
			}

		}catch (Exception e) {
			// 网络连接有问题
			e.printStackTrace();
		} finally {
			if (is != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}

		return result.toString();

	}
	public static String get(String url, String token) {
		StringBuilder result = new StringBuilder();

		DefaultHttpClient httpClient = new DefaultHttpClient();
		InputStream is = null;
		try {

			HttpGet getRequest = new HttpGet(url);
			getRequest.addHeader("Content-Type", "text/html;charset=UTF-8");  
			getRequest.addHeader("accept", "application/json");
			getRequest.addHeader("id-token", token);
			HttpResponse response = httpClient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() == 200) {
				is = response.getEntity().getContent();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					result.append(new String(buffer, 0, len));
				}
			}

		} catch (Exception e) {
			// 网络连接有问题
			e.printStackTrace();
		} finally {
			if (is != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}

		return result.toString();

	}

	public static String getToken(String url, String tokenKey, String tokenValue) {
		StringBuilder result = new StringBuilder();
		DefaultHttpClient httpClient = new DefaultHttpClient();
		InputStream is = null;
		try {
			HttpGet getRequest = new HttpGet(url);
			getRequest.addHeader("Content-Type", "text/json;charset=UTF-8"); 
			getRequest.addHeader("accept", "application/json");
			getRequest.addHeader(tokenKey, tokenValue);
			HttpResponse response = httpClient.execute(getRequest);
			response.setHeader("Content-type", "text/html;charset=UTF-8");  
			if (response.getStatusLine().getStatusCode() == 200) {
				is = response.getEntity().getContent();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					result.append(new String(buffer, 0, len));
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// 网络连接有问题
			e.printStackTrace();
		} finally {
			if (is != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}

		return result.toString();

	}

	// 上传图片
	public static String uploadFile(File file, String RequestURL) {
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(30000);
			conn.setConnectTimeout(30000);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			/**
			 * 当文件不为空，把文件包装并且上传
			 */
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = new StringBuffer();
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINE_END);
			/**
			 * 这里重点注意： name里面的值为服务端需要key 只有这个key 才可以得到对应的文件
			 * filename是文件的名字，包含后缀名的 比如:abc.png
			 */
			sb.append("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
					+ file.getName() + "\"" + LINE_END);
			sb.append("Content-Type: image/jpg; charset=" + CHARSET + LINE_END);
			sb.append(LINE_END);
			dos.write(sb.toString().getBytes());
			InputStream is = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int len = 0;
			while ((len = is.read(bytes)) != -1) {
				dos.write(bytes, 0, len);
			}
			is.close();
			dos.write(LINE_END.getBytes());
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
					.getBytes();
			dos.write(end_data);
			dos.flush();
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			int res = conn.getResponseCode();
			Log.e(TAG, "response code:" + res);
			// if(res==200)
			// {
			Log.e(TAG, "request success");
			InputStream input = conn.getInputStream();
			StringBuffer sb1 = new StringBuffer();
			int ss;
			while ((ss = input.read()) != -1) {
				sb1.append((char) ss);
			}
			result = sb1.toString();
			Log.e(TAG, "result : " + result);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// apche post请求
	public static String post(String url, Object obj, String tokenKey,
			String tokenValue) {
		String result = "";
		HttpResponse response;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			// 添加http头信息
			if(tokenKey.equals("id-token"))
				httppost.addHeader(tokenKey, tokenValue); // 认证token
			httppost.addHeader("Content-Type", "application/json");
			// http post的json数据格式： {"name": "your name"}
			if (obj == null) {
				StringEntity en = new StringEntity(null);
				en.setContentType("application/json");
				httppost.setEntity(en);
			} else {
				httppost.setEntity(new StringEntity(obj.toString(), HTTP.UTF_8));
			}
			response = httpclient.execute(httppost);
			// 检验状态码，如果成功接收数据
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// apche post请求
	public static String postCommon(String url, List<NameValuePair> params) {
		String result = "";
		HttpResponse response;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			response = httpclient.execute(httppost);
			// 检验状态码，如果成功接收数据
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
