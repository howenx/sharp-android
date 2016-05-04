package com.hanmimei.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LayoutUtils {
	private static final String TAG = "LayoutUtils";
	
	public static void setLayoutParams(ViewGroup parentView, View view, int width, int height) {
		setLayoutParams(parentView,view,width,height,0,0,0,0);
	}

	public static void setLayoutParams(ViewGroup parentView, View view, int width, int height, int x, int y) {
		setLayoutParams(parentView,view,width,height,x,y,0,0);
	}
	
	public static void setLayoutParams(ViewGroup parentView, View view, int width, int height, int x, int y,int r, int b) {
		if (width == 0 && height == 0 )
			return;
		if (parentView instanceof RelativeLayout)
			LayoutUtils.setRelativeLayoutParams(view,width,height,x,y,r,b,-1);
		else if (parentView instanceof LinearLayout)
			LayoutUtils.setLinearLayoutParams(view,width,height,x,y,r,b,-1);
		else if (parentView instanceof FrameLayout)
			LayoutUtils.setFrameLayoutParams(view,width,height,x,y,r,b);
		else{
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		}
	}
	
    public static RelativeLayout.LayoutParams setRelativeLayoutParams(View view,int w, int h, int rule) {
    	return setRelativeLayoutParams(view,w,h,0,0,rule);
    }
    
    public static RelativeLayout.LayoutParams setRelativeLayoutParams(View view,int w, int h, int left, int top, int rule) {
    	return setRelativeLayoutParams(view,w,h,left,top,0,0,rule);
    }
    
    public static RelativeLayout.LayoutParams setRelativeLayoutParams(View view,int w, int h, float left, float top, int rule) {
    	return setRelativeLayoutParams(view,w,h,(int)left,(int)top,0,0,rule);
    }
    
    public static RelativeLayout.LayoutParams setRelativeLayoutParams(View view,int w, int h, int left, int top, int right, int bottom, int ruleGroup) {
    	RelativeLayout.LayoutParams params = null;
    	if (view.getLayoutParams() != null && view.getLayoutParams() instanceof RelativeLayout.LayoutParams)
    		params = (RelativeLayout.LayoutParams) view.getLayoutParams();
    	else
    		params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	
    	params.width = w;
    	params.height = h;
    	params.leftMargin = left;
    	params.topMargin = top;
    	params.rightMargin = right;
    	params.bottomMargin = bottom;
    	if (ruleGroup >= 0)
    		params.addRule(ruleGroup);

    	view.setLayoutParams(params);
    	return params;
    }
    
    public static void setRelativeBelow(View view, View belowView, int ruleGroup) {
    	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
    	params.addRule(ruleGroup,belowView.getId());
    	view.setLayoutParams(params);
    }
    
    public static void setRelativeRule(View view, int ruleGroup) {
    	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
    	params.addRule(ruleGroup);
    	view.setLayoutParams(params);
    }
    
    public static int getRelativeLeftMargin(View view) {
    	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
    	return params.leftMargin;
    }
    
    
    public static void setLinearLayoutParams(View view,int w, int h) {
    	setLinearLayoutParams(view,w,h,0,0,-1);
    }
    
    public static void setLinearLayoutParams(View view,int w, int h, int left, int top) {
    	setLinearLayoutParams(view,w,h,left,top,0,0,-1);
    }
    
    public static void setLinearLayoutParams(View view,int w, int h, int gravity) {
    	setLinearLayoutParams(view,w,h,0,0,gravity);
    }
    
    public static void setLinearLayoutParams(View view,int w, int h, int left, int top, int gravity) {
    	setLinearLayoutParams(view,w,h,left,top,0,0,gravity);
    }
    
    public static void setLinearLayoutParams(View view,int w, int h, int left, int top, int right, int bottom) {
    	setLinearLayoutParams(view,w,h,left,top,right,bottom,-1);
	}
    
    public static void setLinearLayoutParams(View view,int w, int h, int left, int top, int right, int bottom, int gravity) {
    	LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
    	if (params == null) {
    		params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	}

    	params.width = w;
    	params.height = h;
    	params.leftMargin = left;
    	params.topMargin = top;
    	params.rightMargin = right;
    	params.bottomMargin = bottom;
    	params.gravity = gravity;
    	view.setLayoutParams(params);
    }
    
    public static void setFrameLayoutParams(View view,int w, int h) {
    	setFrameLayoutParams(view,w,h,0,0,0,0);
	}
    
    public static void setFrameLayoutParams(View view,int w, int h, int left, int top) {
    	setFrameLayoutParams(view,w,h,left,top,0,0);
	}
    
    public static FrameLayout.LayoutParams setFrameLayoutParams(View view,int w, int h, int left, int top, int right, int bottom) {
    	FrameLayout.LayoutParams params = null;
    	if (view.getLayoutParams() != null && view.getLayoutParams() instanceof FrameLayout.LayoutParams)
    		params = (FrameLayout.LayoutParams) view.getLayoutParams();
    	else
    		params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	
    	params.width = w;
    	params.height = h;
    	params.leftMargin = left;
    	params.topMargin = top;
    	params.rightMargin = right;
    	params.bottomMargin = bottom;

    	view.setLayoutParams(params);
    	return params;
    }
    
    public static void setLinearWeight(View view,int weight) {
    	LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
    	params.weight = weight;
    	view.setLayoutParams(params);
    }
    
    public static void setLinearGravity(View view,int gravity) {
    	LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
    	params.gravity = gravity;
    	view.setLayoutParams(params);
    }
    
    public static void setLayoutView(View view,float Scale, int l, int t, int r, int  b) {
    	view.layout(l, t, r, b);
    }
    
    public static void setLayoutView(View page,int l, int t, int r, int  b) {    	
    	page.layout(l, t, r, b);
    	FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)page.getLayoutParams();
    	params.topMargin = t;
        params.leftMargin = l;
        params.width = r - l;
        params.height = b - t;
    }
    
    public static AbsListView.LayoutParams setListLayoutParams(View view,int w, int h) {
    	AbsListView.LayoutParams params = null;
    	if (view.getLayoutParams() != null && view.getLayoutParams() instanceof FrameLayout.LayoutParams)
    		params = (AbsListView.LayoutParams) view.getLayoutParams();
    	else
    		params = new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    	
    	params.width = w;
    	params.height = h;
    	view.setLayoutParams(params);
    	return params;
    }
    
    public static void setLayoutParams(View view,int x, int y, int width, int height) {
//    	view.layout(l, t, r, b);
    	LayoutParams params = view.getLayoutParams();
    	if (params == null)
    	    return;
    	if (params instanceof RelativeLayout.LayoutParams) {
    		params = setLayoutParams((RelativeLayout.LayoutParams)params,x,y,width,height);
    	} else if (params instanceof FrameLayout.LayoutParams) {
    		params = setLayoutParams((FrameLayout.LayoutParams)params,x,y,width,height);
    	}
    	view.setLayoutParams(params);
    }
    
    public static LayoutParams setLayoutParams(RelativeLayout.LayoutParams params,float x, float y, float width, float height) {
    	if (params == null)
    		params = new RelativeLayout.LayoutParams(0, 0);
    	
    	params.width = (int) (width);
    	params.height = (int) (height);
    	params.leftMargin = (int) x;
    	params.topMargin = (int) y;
    	if (width > 0)
    		params.rightMargin = (int) -width;
    	if (height > 0)
    		params.bottomMargin = (int) -height;
    	return params;
    }
    
    public static LayoutParams setLayoutParams(FrameLayout.LayoutParams params,float x, float y, float width, float height) {
    	if (params == null)
    		params = new FrameLayout.LayoutParams(0, 0);
    	params.width = (int) (width);
    	params.height = (int) (height);
    	params.leftMargin = (int) x;
    	params.topMargin = (int) y;
    	params.rightMargin = (int) -width;
    	params.bottomMargin = (int) -height;
    	
    	return params;
    }
    
    
}
