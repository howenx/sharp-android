package com.hanmimei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class TouchWebView extends WebView {
	public TouchWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TouchWebView(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(event);
	}
}
