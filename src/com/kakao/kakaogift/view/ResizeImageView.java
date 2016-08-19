/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-8-19 下午3:30:23 
 **/
package com.kakao.kakaogift.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author vince
 * 
 */
public class ResizeImageView extends ImageView {

	/**
	 * @param context
	 */
	public ResizeImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private ResizeImageView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	private ResizeImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	

	@TargetApi(21)
	private ResizeImageView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
	}

	// 然后在SmartImageView中，添加一个float类型的成员变量ratio作为图片的比例值，并且给它暴露一个setter方法，以便于设置图片比例。

	/** 图片宽和高的比例 */
	private float ratio = 0f;

	public void setRatio(float ratio) {
		this.ratio = ratio;
	}

	// 然后我们来重写onMeausre方法，如下：

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// 父容器传过来的宽度的值
		int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
				- getPaddingRight();
		// 父容器传过来的高度的值
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft()
				- getPaddingRight();
				
		if(ratio !=0){
			height = (int) ((width/ratio) +0.5f);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
