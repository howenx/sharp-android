/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-30 下午3:38:56 
**/
package com.hanmimei.view.webview;

import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * @author vince
 *
 */
public class ProgressWebChromeClient extends android.webkit.WebChromeClient {
	
	ProgressBar progressbar;
	public ProgressWebChromeClient(ProgressBar progressbar) {
		super();
		this.progressbar = progressbar;
		this.progressbar.setMax(100);
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		if (progressbar.getVisibility() == View.GONE)
			progressbar.setVisibility(View.VISIBLE);
		progressbar.setProgress(newProgress);
		if (newProgress >= 100) {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					progressbar.setVisibility(View.GONE);
				}
			}, 500);
		} 
		super.onProgressChanged(view, newProgress);
	}
	
	
}
