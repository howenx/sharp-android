/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-30 下午3:38:56 
**/
package com.hanmimei.view.webview;

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
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		if (newProgress >= 100) {
			progressbar.setVisibility(View.GONE);
		} else {
			if (progressbar.getVisibility() == View.GONE)
				progressbar.setVisibility(View.VISIBLE);
			progressbar.setProgress(newProgress);
		}
		super.onProgressChanged(view, newProgress);
	}
}
