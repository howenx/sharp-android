package com.hanmimei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ScrollView;

public class GridBottomView extends GridView {  
	  
    private OnScrollToBottomListener onScrollToBottom;  
      
    public GridBottomView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public GridBottomView(Context context) {  
        super(context);  
    }  
  
    @Override  
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,  
            boolean clampedY) {  
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);  
        if(scrollY != 0 && null != onScrollToBottom){
        	if(clampedY)
        		onScrollToBottom.onScrollBottomListener();  
        }  
    }  
      
    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener){  
        onScrollToBottom = listener;  
    }  
  
    public interface OnScrollToBottomListener{  
        public void onScrollBottomListener();  
    }  
}  