package com.hanmimei.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.android.volley.toolbox.HurlStack;

public class HttpsHurlStack extends HurlStack{
	
	@Override
	protected HttpURLConnection createConnection(URL url) throws IOException {  
	    //如果请求是https请求那么就信任所有SSL  
		if (url.toString().contains("https")) {  
            HTTPSTrustManager.allowAllSSL();  
      }  
      return (HttpURLConnection) url.openConnection();  
	}
}
