package com.hanbimei.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.hanbimei.R;

public class RotateTextView extends TextView {
	private static final int DEFAULT_DEGREES = 0;
	private int mAngle;

	public RotateTextView(Context context) {
		super(context, null);
	}

	public RotateTextView(Context context, AttributeSet attrs) {
		super(context, attrs, android.R.attr.textViewStyle);
		this.setGravity(Gravity.CENTER);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.RotateTextView);
		mAngle = a.getDimensionPixelSize(R.styleable.RotateTextView_angle,
				DEFAULT_DEGREES);
		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
		canvas.rotate(mAngle, 0, this.getHeight() / 2f);
		super.onDraw(canvas);
		canvas.restore();
	}

	public int getmAngle() {
		return mAngle;
	}

	public void setAngle(int mAngle) {
		this.mAngle = mAngle;
	}

	
	
}
