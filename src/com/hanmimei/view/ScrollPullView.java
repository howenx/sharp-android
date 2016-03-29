package com.hanmimei.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hanmimei.R;
import com.hanmimei.utils.CommonUtil;

public class ScrollPullView extends ScrollView {

	public OnScrollUpListener mOnScrollListener;
	private int oldScrollY = 0;
	final int[] locations = new int[2];
	private int maxHeaderHeight;
	private int minHeaderHeight;

	private int currentHeaderHeight;
	private int headerHeight;
	private boolean zooming = false;

	private float downY = 0.0f;
	private float curY = 0.0f;

	private View headerView;
	private int statusBarHeight = 60;

	public void setOnScrollUpListener(OnScrollUpListener mOnScrollListener) {
		this.mOnScrollListener = mOnScrollListener;
	}

	public ScrollPullView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);

	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.pull);
		maxHeaderHeight = a.getLayoutDimension(
				R.styleable.pull_maxHeaderHeight, 0);
		minHeaderHeight = a.getLayoutDimension(
				R.styleable.pull_minHeaderHeight, 0);
		a.recycle();
		if (maxHeaderHeight == 0) {
			throw new RuntimeException(
					"ListZoomView maxHeaderHeight must be set.");
		}
		statusBarHeight = CommonUtil.getStatusBarHeight(context);
	}

	public void initHeaderView(View headerView) {
		this.headerView = headerView;
		measureHeader(headerView, minHeaderHeight);
		headerHeight = headerView.getMeasuredHeight();
		currentHeaderHeight = headerHeight;
	}

	/**
	 * measure header view's width and height
	 *
	 * @param headerView
	 */
	private void measureHeader(View headerView, int height) {
		ViewGroup.LayoutParams p = headerView.getLayoutParams();
		if (p == null) {
			int h = height == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : height;
			p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
		} else if (height != 0) {
			p.height = height;
		}
		headerView.setLayoutParams(p);
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		headerView.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		int action = event.getAction();
		boolean r = false;
		if (action == MotionEvent.ACTION_DOWN ) {
			Log.i("touch01", "down");
			downY = event.getRawY();
			curY = getY();
		} else if (action == MotionEvent.ACTION_MOVE ) {
			Log.i("touch01", "move");
			boolean upwards = downY - event.getRawY() > 0;
			if (headerView.getHeight() >= headerHeight) {
				r = !upwards;
			}
		}
		return r;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction();
		if (action == MotionEvent.ACTION_MOVE) {
			boolean downwards = downY - event.getRawY() < 0;
			headerView.getLocationInWindow(locations);
			if (downwards && locations[1] - statusBarHeight == getTop()) {
				if (headerView.getHeight() >= headerHeight) {
					Log.i("touch02", "下拉");
					computeTravel(event, false);
					downY = event.getRawY();
					return true;
				}
			}
			
		} else if (action == MotionEvent.ACTION_UP
				&& locations[1] - statusBarHeight == getTop()) {
			Log.i("touch02", "下拉恢复");
			computeTravel(event, true);
		}
		return super.onTouchEvent(event);
	}
	

	/**
	 * 计算并调整header显示的高度
	 *
	 * @param ev
	 * @param actionUp
	 */
	private void computeTravel(MotionEvent ev, boolean actionUp) {
		float movingY = ev.getRawY();
		int travel = (int) (downY - movingY);
		boolean up = travel > 0;
		travel = Math.abs(travel);

		move(travel, up, actionUp);
	}

	public void move(int distance, boolean upwards, boolean release) {
		// illegal distance
		if (distance > 30)
			return;

		if (release) {
			// zooming
			if (headerView.getHeight() > headerHeight) {
				collapse(headerView, headerHeight);
				currentHeaderHeight = headerHeight;
			}
			zooming = false;
			return;
		} else {
			zooming = true;
			resizeHeader(distance, upwards);
		}
	}

	private void resizeHeader(int distance, boolean upwards) {
		distance = (int) (distance / 1.5f);
		// zoom out
		if (upwards && headerView.getHeight() > headerHeight) {
			int tmpHeight = currentHeaderHeight - distance;
			if (tmpHeight < headerHeight) {
				tmpHeight = headerHeight;
			}
			currentHeaderHeight = tmpHeight;
			resizeHeight(currentHeaderHeight);

		}
		if (!upwards && headerView.getHeight() >= headerHeight) {
			// zoom in
			currentHeaderHeight += distance;
			if (currentHeaderHeight > maxHeaderHeight) {
				currentHeaderHeight = maxHeaderHeight;
			}
			resizeHeight(currentHeaderHeight);
		}

	}

	private void resizeHeight(int resizeHeight) {
		ViewGroup.LayoutParams params = headerView.getLayoutParams();
		if (params == null) {
			params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					resizeHeight);
		} else {
			params.height = resizeHeight;
		}
		headerView.setLayoutParams(params);
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
	
	
	// height change animation
    private void collapse(final View v, final int minHeight) {
        final int initialHeight = v.getMeasuredHeight();

        final int offset = initialHeight - minHeight;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.getLayoutParams().height = minHeight;
                    v.requestLayout();
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (offset * interpolatedTime);
                    v.requestLayout();
                }
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int) (minHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
    
    // height and width change animation
    private void collapse2(final View v, final int minHeight,final int minWidth) {
        final int initialHeight = v.getMeasuredHeight();
        final int initialWidth = v.getMeasuredWidth();

        final int offsetHeight = initialHeight - minHeight;
        final int offsetWidth = initialWidth - minWidth;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.getLayoutParams().height = minHeight;
                    v.getLayoutParams().width = minWidth;
                    v.requestLayout();
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (offsetHeight * interpolatedTime);
                    v.getLayoutParams().width = initialWidth - (int) (offsetWidth * interpolatedTime);
                    v.requestLayout();
                }
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int) (minHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

	public int getMaxHeaderHeight() {
		return maxHeaderHeight;
	}

	public void setMaxHeaderHeight(int maxHeaderHeight) {
		this.maxHeaderHeight = maxHeaderHeight;
	}

	public int getMinHeaderHeight() {
		return minHeaderHeight;
	}

	public void setMinHeaderHeight(int minHeaderHeight) {
		this.minHeaderHeight = minHeaderHeight;
	}

	public int getHeaderHeight() {
		return headerHeight;
	}

	public void setHeaderHeight(int headerHeight) {
		this.headerHeight = headerHeight;
	}
    
    
}
