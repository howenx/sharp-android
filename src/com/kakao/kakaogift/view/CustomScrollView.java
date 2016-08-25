package com.kakao.kakaogift.view;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.widget.ScrollView;

/**
 * 自定义ScrollView，解决：ScrollView嵌套ViewPager，导致ViewPager不能滑动的问题
 */
public class CustomScrollView extends ScrollView {
	private GestureDetector mGestureDetector;
	private int Scroll_height = 0;
	private int view_height = 0;
	protected Field scrollView_mScroller;
	private static final String TAG = "CustomScrollView";

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
//
	
	public OnScrollUpListener mOnScrollListener;
	private int oldScrollY = 0;

	public void setOnScrollUpListener(OnScrollUpListener mOnScrollListener) {
		this.mOnScrollListener = mOnScrollListener;
	}
	
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
//		Log.i("scrolled", "滚动");
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if(mOnScrollListener !=null)
			mOnScrollListener.onScroll(scrollY,scrollY-oldScrollY>0);
		oldScrollY = scrollY;
	}

	public interface OnScrollUpListener {
		public void onScroll(int scrollY, boolean scrollDirection);
	}
}
