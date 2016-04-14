/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-13 下午4:26:18 
**/
package com.hanmimei.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hanmimei.R;

/**
 * @author vince
 *
 */
public class ToolbarLayout extends LinearLayout {

	/**
	 * @param context
	 */
	@SuppressLint("NewApi")
	public ToolbarLayout(Context context) {
		super(context);
		setFitsSystemWindows(true);
	}
	
	public void attachToActivity(Activity activity) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.main_header_new_layout, null);
         addView(view);
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decor.removeView(decorChild);
        addView(decorChild);
        decor.addView(this);
    }
	

}
