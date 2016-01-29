package com.hanmimei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.hanmimei.R;

/**
 * @Description:带进度条的WebView
 * @author http://blog.csdn.net/finddreams
 */ 
@SuppressWarnings("deprecation")
public class ProgressWebView2 extends WebView {

    private NumberProgressBar progressbar;
    private WebViewClient mWebViewClient;
   
    
    public ProgressWebView2(Context context){
   	 super(context);
   	 initProgressbar(context, null);
   }

    public ProgressWebView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProgressbar(context, attrs);
    }
    
    private void initProgressbar(Context context, AttributeSet attrs){
    	progressbar = new NumberProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0, 0));
        progressbar.setReachedBarColor(getResources().getColor(R.color.theme));
        progressbar.setProgressTextColor(getResources().getColor(R.color.theme));
        addView(progressbar);
        setWebViewClient(new WebViewClient(){});
    }

    public class HWebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        	Log.i("newProgress", newProgress+"");
            if (newProgress >= 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
    
    public NumberProgressBar getProgressBar(){
    	return progressbar;
    }
    
    
    

	@Override
	public void setWebViewClient(WebViewClient client) {
		super.setWebViewClient(client);
		this.mWebViewClient = client;
		
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