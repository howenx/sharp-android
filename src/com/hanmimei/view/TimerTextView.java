package com.hanmimei.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hanmimei.override.TimeEndListner;

public class TimerTextView extends TextView implements Runnable{  
    
	private TimeEndListner timeEndListner;
    public TimerTextView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
    }  
    private String tex;
    private long  mhour, mmin, msecond;//天，小时，分钟，秒  
    private boolean run=false; //是否启动了  
    public void setTimes(long[] times, String tex) {   
        mhour = times[0];  
        mmin = times[1];  
        msecond = times[2];  
        this.tex = tex;
    }  
  
    /** 
     * 倒计时计算 
     */  
    private void ComputeTime() {  
        msecond--;  
        if (msecond < 0) {  
            mmin--; 
            if(mhour == 0 && mmin < 0){
            	msecond = 0;
            }else{

    			msecond = 59;
            }  
            if (mmin < 0) {
                mhour--;  
            	if(mhour < 0){
            		mmin = 0;
            	}else{
                    mmin = 59;  
            	}
                if (mhour < 0) {  
                    // 倒计时结束，一天有24个小时  
                    mhour = 0;    
                }  
            }  
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
            	strTime = tex;  
            	timeEndListner.isTimeEnd();
            }else{
            	strTime= "" + mhour+"小时:"+ mmin+"分钟:"+msecond+"秒";  
            }
            this.setText(strTime);  
            postDelayed(this, 1000);  
        }else {  
            removeCallbacks(this);  
        }  
    }  
    private boolean isEnd(){
    	if(mhour <= 0 && mmin <= 0&& msecond <= 0){
    		return true;
    	}else{
        	return false;
    	}
    }
	public void setTimeEndListner(TimeEndListner timeEndListner) {
		this.timeEndListner = timeEndListner;
	}
    
  
}  
