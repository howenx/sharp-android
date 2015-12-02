package com.hanmimei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

public class ScrollZoomView extends ScrollView {

	public OnScrollUpListener mOnScrollListener;
	private int oldScrollY = 0;

	public void setOnScrollUpListener(OnScrollUpListener mOnScrollListener) {
		this.mOnScrollListener = mOnScrollListener;
	}

	public ScrollZoomView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		Log.i("scrolled", "滚动");
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if(mOnScrollListener !=null)
			mOnScrollListener.onScroll(scrollY,scrollY-oldScrollY>0);
		oldScrollY = scrollY;
	}

	public interface OnScrollUpListener {
		public void onScroll(int scrollY, boolean scrollDirection);
	}
}
