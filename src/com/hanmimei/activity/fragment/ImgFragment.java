package com.hanmimei.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.cpoopc.scrollablelayoutlib.ScrollAbleFragment;
import com.hanmimei.R;

public class ImgFragment extends ScrollAbleFragment   {
	

	public static ImgFragment newInstance(String noticeInfo,String data) {
		ImgFragment newFragment = new ImgFragment();
		Bundle bundle = new Bundle();
		bundle.putString("data", data);
		bundle.putString("noticeInfo", noticeInfo);
		newFragment.setArguments(bundle);
		// bundle还可以在每个标签里传送数据
		return newFragment;
	}
	
	private WebView mWebView;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.webview_layout, null);
		String data = getArguments().getString("data");
		String noticeInfo = getArguments().getString("noticeInfo");
		mWebView = (WebView) view.findViewById(R.id.mWebView);
		mWebView.loadData(data, "text/html", "UTF-8");
		
		TextView notice = (TextView) view.findViewById(R.id.notice);
		if(TextUtils.isEmpty(noticeInfo)){
			notice.setVisibility(View.GONE);
		}else{
			notice.setVisibility(View.VISIBLE);
			notice.setText(noticeInfo);
		}
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDisplayZoomControls(false);
		
		return view;
	}

	@Override
	public View getScrollableView() {
		// TODO Auto-generated method stub
		return mWebView;
	}
	
}
