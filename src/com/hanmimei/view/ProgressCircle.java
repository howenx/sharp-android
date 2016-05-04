package com.hanmimei.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import com.hanmimei.utils.LayoutUtils;

public class ProgressCircle extends View{
	private static final String TAG = "PregressCircle";
	
	private static float DEFAULT_TEXT_SIZE = 64;
	
	public static ProgressCircle make(Context context, ViewGroup parent,int width, int height) {
		return make(context,parent,width,height,0,0,0,0);
	}
	
	public static ProgressCircle make(Context context, ViewGroup parent,int width, int height, int left, int top) {
		return make(context,parent,width,height,left,top,0,0);
	}
	
	public static ProgressCircle make(Context context, ViewGroup parent,int width, int height, int left, int top, int right, int bottom) {
		ProgressCircle view = new ProgressCircle(context);
		parent.addView(view);
		LayoutUtils.setLayoutParams(parent, view, width, height, left, top, right, bottom);
		return view;
	}
	
	private float mMax = 100;
	private float mValue = 0;
	
	private boolean mIsInfinity = false;
	private float mInfinityStartDegrees = -90;
	
	private float mDegrees = 0;
	
	private float mScale = 0.5f;
	
    private Paint mPaints;
    private Paint mBGCirclePaints;
    private Paint mTextPaints;
    
    private float mTextSize = -1;
    
    private RectF mBigOval;
    private RectF mBeforeBigOval;
    private int mCircleStroke = 2;
    
    private boolean mIsShowText = false;
    private int mPersent = 0;
    
	public ProgressCircle(Context context) {
        super(context);
        mPaints = new Paint();
        mPaints.setStyle(Paint.Style.STROKE);
        mPaints.setStrokeWidth(mCircleStroke);
        mPaints.setColor(Color.WHITE);
        mPaints.setAntiAlias(true);
        mPaints.setStrokeCap(Paint.Cap.BUTT);
        
        mBGCirclePaints = new Paint();
        mBGCirclePaints.setStyle(Paint.Style.STROKE);
        mBGCirclePaints.setStrokeWidth(mCircleStroke);
        mBGCirclePaints.setColor(Color.BLACK);
        mBGCirclePaints.setAntiAlias(true);
        mBGCirclePaints.setStrokeCap(Paint.Cap.BUTT);
        
        mTextPaints = new Paint();  
        mTextPaints.setAntiAlias(true);  
        mTextPaints.setColor(Color.RED);  
        
        mBigOval = new RectF();
	}

	public void reStartInfinity() {
       if (mIsInfinity && mStopInfinity) {
            mStopInfinity = false;
            mHandler.sendEmptyMessage(SHOW_INFINITY);
        }
	}
	public void setInfinity(boolean isInfinity) {
	    if (mIsInfinity == isInfinity)
	        return;
		mIsInfinity = isInfinity;
		if (mIsInfinity) {
		    mStopInfinity = false;
		    mHandler.sendEmptyMessage(SHOW_INFINITY);
		}
	}
	
	public boolean isInfinity() {
		return mIsInfinity;
	}
	
    public void setMax(float max) {
    	mMax = max;
    }
    
    public float getMax() {
    	return mMax;
    }
    public void setProgress(int value) {
    	mValue = value;
    	mDegrees = (mValue/mMax) * 360.0f;
        if (mDegrees > 360) {
            mDegrees = 360;
        }
    	mPersent = (int) ((mValue/mMax) * 100);
        if (mPersent > 100) {
            mPersent = 100;
        }
    	invalidate();
    }
	
    public float getProgress() {
    	return mValue;
    }
    public void setCircleColor(int color) {
    	mPaints.setColor(color);
    }
    
    public void setBGCircleColor(int color) {
    	mBGCirclePaints.setColor(color);
    }

    public void setCircleScale(float scale) {
    	mScale = scale;
    }

    public void setStrokeWidth(int stroke) {
    	mCircleStroke = stroke;
    	mPaints.setStrokeWidth(mCircleStroke);
    	mBGCirclePaints.setStrokeWidth(mCircleStroke);
    }
    
    public void setShowPersent(boolean isShow) {
    	mIsShowText = isShow;
    }
    
    public void setPersentColor(int color) {
    	mTextPaints.setColor(color);
    }
    
    public void cancel() {
        mStopInfinity = true;
    }
    
    private void drawArcs(Canvas canvas, RectF oval, boolean useCenter, Paint paint) {
        canvas.drawArc(oval, -90, mDegrees, useCenter, paint);
        canvas.drawArc(oval, -90, mDegrees - 360, useCenter, mBGCirclePaints);
    }
    
    private void drawInfinityArcs(Canvas canvas, RectF oval, boolean useCenter, Paint paint) {
        canvas.drawArc(oval, mInfinityStartDegrees, 90, useCenter, paint);
        canvas.drawArc(oval, mInfinityStartDegrees - 360, (90 - 360), useCenter, mBGCirclePaints);
    }
    
    @Override 
    protected void onDraw(Canvas canvas) {
    	if (mValue >= 0 || mIsInfinity) {
        	setBound();
        	//canvas.drawColor(Color.alpha(Color.CYAN));
        	if (mIsInfinity) {
        		drawInfinityArcs(canvas, mBigOval, false, mPaints);
        	} else {
        		drawArcs(canvas, mBigOval, false, mPaints);
        		if (mIsShowText) {
        			drawText(canvas);
        		}
        	}
    	}
    }
    
    private void drawText(Canvas canvas) {
        getComputeTextSize();
		String text = mPersent + "%";
		float textHeight = mTextPaints.descent() - mTextPaints.ascent();
	    float texttopOffset = (textHeight / 2) - mTextPaints.descent();
		float textLeftOffset = mTextPaints.measureText(text) / 2;
		canvas.drawText(mPersent + "%", getWidth() / 2 - textLeftOffset, getHeight() / 2 + texttopOffset, mTextPaints);
    }
    
    private float getComputeTextSize() {
        if (mTextSize > 0)
            return mTextSize;
        mTextSize = DEFAULT_TEXT_SIZE;
        mTextPaints.setTextSize(mTextSize);
        String testText = "100%";
        float limitWidth = (float) (mBigOval.width() * 0.4);
        
        for (int i=0;i<DEFAULT_TEXT_SIZE;i++) {
            float texthor = mTextPaints.measureText(testText) / 2;
            if (limitWidth < texthor) {
                mTextSize--;
                mTextPaints.setTextSize(mTextSize);
            } else {
                mTextPaints.setTextSize(mTextSize);
                break;
            }
        }
        return mTextSize;
    }
    
    private void setBound() {
    	int width = getWidth();
    	int height = getHeight();
    	int size = (int) ((width<height?width:height) * mScale);
    	int left = (width - size) / 2;
    	int top = (height - size) / 2;
    	mBigOval.set(left, top, left + size, top + size);
    	if (mBeforeBigOval == null) {
    	    mBeforeBigOval = new RectF();
    	    mBeforeBigOval.set(left, top, left + size, top + size);
    	}
    }
    
    private boolean mStopInfinity = true;
    private int SHOW_INFINITY = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SHOW_INFINITY) {
                if (!mStopInfinity) {
                    mInfinityStartDegrees +=10;
                    if (mInfinityStartDegrees > 270) {
                        mInfinityStartDegrees = -90;
                    }
                    invalidate();
                    msg = obtainMessage(SHOW_INFINITY);
                    sendMessageDelayed(msg, 100);
                }
            }
        }
    };
}
