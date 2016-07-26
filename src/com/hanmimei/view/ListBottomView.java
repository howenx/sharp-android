package com.hanmimei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListBottomView extends ListView {  
	  
    private OnScrollToBottomListener onScrollToBottom;  
      
    public ListBottomView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public ListBottomView(Context context) {  
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