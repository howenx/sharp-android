package com.kakao.kakaogift.view.webview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.kakao.kakaogift.R
;

/**
 * @Description:带进度条的WebView
 * @author http://blog.csdn.net/finddreams
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {

	private ProgressBar progressbar;

	public ProgressWebView(Context context) {
		super(context);
		progressbar = new ProgressBar(context, null,
				android.R.attr.progressBarStyleHorizontal);
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				8, 0, 0));
		Drawable draw = context.getResources().getDrawable(
				R.drawable.progress_bar_states);
		progressbar.setProgressDrawable(draw);
		addView(progressbar);
		setWebViewClient(new WebViewClient());
		setWebChromeClient(new ProgressWebChromeClient(progressbar));
	}

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		progressbar = new ProgressBar(context, null,
				android.R.attr.progressBarStyleHorizontal);
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				4, 0, 0));
		Drawable drawable = context.getResources().getDrawable(
				R.drawable.progress_bar_states);
		progressbar.setProgressDrawable(drawable);
		addView(progressbar);
		setWebViewClient(new WebViewClient());
		setWebChromeClient(new ProgressWebChromeClient(progressbar));
		
	}



	public ProgressBar getProgressBar() {
		return progressbar;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}
}