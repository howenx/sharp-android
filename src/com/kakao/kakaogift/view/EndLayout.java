package com.kakao.kakaogift.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kakao.kakaogift.R;


public class EndLayout extends FrameLayout implements IEndLayout {

	private LinearLayout mLinearLayout;
	private TextView topTextView;
	private TextView bottomTextView;

	public EndLayout(Context context) {
		super(context);
		initView(context);
	}

	private EndLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private EndLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater.from(context).inflate(
				R.layout.pull_to_refresh_end_layout, this);

		mLinearLayout = (LinearLayout) findViewById(R.id.ll_inner);
		topTextView = (TextView) mLinearLayout.findViewById(R.id.text_top);
		bottomTextView = (TextView) mLinearLayout
				.findViewById(R.id.text_bottom);
	}
	
	
	public final void setHeight(int height) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.height = height;
		requestLayout();
	}
	
	public final void setWidth(int width) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.width = width;
		requestLayout();
	}

	@Override
	public void show() {
		setVisibility(View.VISIBLE);
	}

	@Override
	public void hide() {
		setVisibility(View.GONE);
	}

	@Override
	public void setText_top(String content) {
		topTextView.setText(content);
	}

	@Override
	public void setText_bottom(String content) {
		bottomTextView.setText(content);
	}

	@Override
	public void setTextColor_top(int color) {
		topTextView.setTextColor(color);
	}

	@Override
	public void setTextColor_bottom(int color) {
		// TODO Auto-generated method stub
		bottomTextView.setTextColor(color);
	}

	@Override
	public void setTextSize_top(int dimenRes) {
		// TODO Auto-generated method stub
		topTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, dimenRes);
	}

	@Override
	public void setTextSize_bottom(int dimenRes) {
		// TODO Auto-generated method stub
		bottomTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, dimenRes);
	}

}
