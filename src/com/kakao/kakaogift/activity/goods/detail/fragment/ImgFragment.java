package com.kakao.kakaogift.activity.goods.detail.fragment;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cpoopc.scrollablelayoutlib.ScrollAbleFragment;
import com.kakao.kakaogift.R;
/**
 * 
 * @author vince
 *
 */
public class ImgFragment extends ScrollAbleFragment   {
	

	
	private WebView mWebView;

	@SuppressLint({ "SetJavaScriptEnabled", "InflateParams" })
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.webview_layout, null);
		mWebView = (WebView) view.findViewById(R.id.mWebView);
		
		mWebView.getSettings().setBlockNetworkImage(false); 
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
		    @Override
		    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		        handler.proceed();//接受证书
		    }
		});
		return view;
	}

	@Override
	public View getScrollableView() {
		return mWebView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mWebView.removeAllViews();
		mWebView.destroy();
	}

	@Override
	public String getTitle() {
		return "图文详情";
	}

	@Override
	public void showData(Object obj) {
		try {
			if(obj !=null && !"".equals(obj))
				mWebView.loadData((String)obj, "text/html; charset=UTF-8", null);
		} catch (Exception e) {
		}
	
	}
	
}
