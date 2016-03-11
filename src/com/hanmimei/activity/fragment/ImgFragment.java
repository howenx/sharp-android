package com.hanmimei.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.cpoopc.scrollablelayoutlib.ScrollAbleFragment;
import com.hanmimei.R;

public class ImgFragment extends ScrollAbleFragment   {
	

	public static ImgFragment newInstance(String data) {
		ImgFragment newFragment = new ImgFragment();
		Bundle bundle = new Bundle();
		bundle.putString("data", data);
		newFragment.setArguments(bundle);
		// bundle还可以在每个标签里传送数据
		return newFragment;
	}
	
	private WebView mWebView;
	private ProgressBar mProgressBar;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.webview_layout, null);
		String data = getArguments().getString("data");
		mWebView = (WebView) view.findViewById(R.id.mWebView);
		mProgressBar = (ProgressBar) view.findViewById(R.id.mProgressBar);
		mWebView.loadData(data, "text/html", "UTF-8");
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDisplayZoomControls(false);
		
		mWebView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if(newProgress>=99){
					mProgressBar.setVisibility(View.INVISIBLE);
					mWebView.setVisibility(View.VISIBLE);
				}
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
	
}
