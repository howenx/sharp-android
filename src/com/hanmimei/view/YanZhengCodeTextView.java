package com.hanmimei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hanmimei.activity.listener.TimeEndListner;

public class YanZhengCodeTextView extends TextView implements Runnable{  
    
	private TimeEndListner timeEndListner;
    public YanZhengCodeTextView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
    }  
  
    private int  msecond;//天，小时，分钟，秒  
    private boolean run=false; //是否启动了  
    public void setTimes(int times) {     
        msecond = times;  
  
    }  
  
    /** 
     * 倒计时计算 
     */  
    private void ComputeTime() {  
        msecond--;  
        if (msecond < 0) {  
            	msecond = 0;
        }  
      
    }  
  
    public boolean isRun() {
    		return run;  
    }  
  
    public void beginRun() {  
        this.run = true;  
        run();  
    }  
      
    public void stopRun(){  
        this.run = false;  
    }  
      
  
    @Override  
    public void run() {  
        //标示已经启动  
        if(run){  
            ComputeTime();  
            String strTime;
            if(isEnd()){
            	strTime = "获取验证码";  
            	timeEndListner.isTimeEnd();
            }else{
            	strTime= "获取验证码" + msecond +"s";  
            }
            this.setText(strTime);  
            postDelayed(this, 1000);  
        }else {  
            removeCallbacks(this);  
        }  
    }  
    private boolean isEnd(){
    	if(msecond <= 0){
    		return true;
    	}else{
        	return false;
    	}
    }
	public void setTimeEndListner(TimeEndListner timeEndListner) {
		this.timeEndListner = timeEndListner;
	}
    
  
}  
