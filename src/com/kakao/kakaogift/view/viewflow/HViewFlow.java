package com.kakao.kakaogift.view.viewflow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Adapter;

public class HViewFlow extends ViewFlow {
	
	private GestureDetector mGestureDetector;
	
	public HViewFlow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		 mGestureDetector = new GestureDetector(getContext(), new YScrollDetector());
	}
	public HViewFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		 mGestureDetector = new GestureDetector(getContext(), new YScrollDetector());
	}
	
	private int mCount;
    public void setCount(int count){
        mCount=count;
    }
    @Override
    public int getViewsCount() {
        return mCount;
    }
    private int mLastIndex;
    @Override
    public void setAdapter(Adapter adapter, int initialPosition){
        super.setAdapter(adapter,initialPosition);
        mLastIndex = initialPosition;
    }

    @Override
    protected void onScrollChanged(int h, int v, int oldh, int oldv) {
        //super.onScrollChanged(h, v, oldh, oldv);
        if (mIndicator != null) {
            /*
             * The actual horizontal scroll origin does typically not match the
             * perceived one. Therefore, we need to calculate the perceived
             * horizontal scroll origin here, since we use a view buffer.
             */
            int hPerceived = h + (mCurrentAdapterIndex%mCount - mCurrentBufferIndex) * getChildWidth();

            if(mLastIndex%mCount==mCount-1 && mCurrentAdapterIndex>mLastIndex) {
                oldh=0;
                hPerceived = 0;
            }
            if(mLastIndex%mCount==0&&mCurrentAdapterIndex<mLastIndex)
                hPerceived=h+(mCount-1-mCurrentBufferIndex)*getChildWidth();

//            LogUtil.e(Config.MYTAG,"mCurrentAdapterIndex="+mCurrentAdapterIndex);
//            LogUtil.e(Config.MYTAG,"mLastIndex="+mLastIndex);
//            LogUtil.e(Config.MYTAG, "hPerceived=" + hPerceived);

            mIndicator.onScrolled(hPerceived, v, oldh, oldv);

            mLastIndex=mCurrentAdapterIndex;
        }
    }

    private ViewGroup viewGroup;
    public void setViewGroup(ViewGroup viewGroup) {
    	this.viewGroup = viewGroup;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (viewGroup != null)
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	viewGroup.requestDisallowInterceptTouchEvent(!mGestureDetector.onTouchEvent(ev));
                    break;
                case MotionEvent.ACTION_UP:
                	viewGroup.requestDisallowInterceptTouchEvent(false);
                    break;
                case MotionEvent.ACTION_CANCEL:
                	viewGroup.requestDisallowInterceptTouchEvent(false);
                    break;
                case MotionEvent.ACTION_MOVE:
                	viewGroup.requestDisallowInterceptTouchEvent(!mGestureDetector.onTouchEvent(ev));
                    break;
            }
        return super.onInterceptTouchEvent(ev);
    }
    
    /**
     * 手势监听（用于识别手势滑动）
     * 
     * @author LiangZiChao
     * 
     */
    class YScrollDetector extends SimpleOnGestureListener {
 
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            /**
             * if we're scrolling more closer to x direction, return false, let
             * subview to process it
             */
            return (Math.abs(distanceY) > Math.abs(distanceX));
        }
    }
	
	

}
