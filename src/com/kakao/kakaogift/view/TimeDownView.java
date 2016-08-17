
package com.kakao.kakaogift.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.override.TimeEndListner;
/**
 * 倒计时 （天时分秒）
 * 
 * @author vince
 * @since 
 *
 */
public class TimeDownView extends RelativeLayout implements Runnable {

	private TextView  timedown_hour, timedown_min,
			timedown_second;
	private Paint mPaint; // 画笔,包含了画几何图形、文本等的样式和颜色信息
	private int[] times;
	private long  mhour, mmin, msecond;// 天，小时，分钟，秒
	private boolean run = true; // 是否启动了

	public TimeDownView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public TimeDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public TimeDownView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		View.inflate(context, R.layout.timedown_layout, TimeDownView.this);
		timedown_hour = (TextView) this.findViewById(R.id.timedown_hour);
		timedown_min = (TextView) this.findViewById(R.id.timedown_min);
		timedown_second = (TextView) this.findViewById(R.id.timedown_second);

		mPaint = new Paint();
	}

	public int[] getTimes() {
		return times;
	}

	public void setTimes(int[] times) {
		this.times = times;
		mhour = times[0];
		mmin = times[1];
		msecond = times[2];
	}

	/**
	 * 倒计时计算
	 */
	private void ComputeTime() {
		msecond--;
		if (msecond < 0) {
			mmin--;
			msecond = 59;
			if (mmin < 0) {
				mmin = 59;
				mhour--;
			}
		}
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}
	
	public void stop(){
		run = false;
	}

	@Override
	public void run() {
		// 标示已经启动
		if (run) {
			ComputeTime();
			timedown_hour.setText(mhour + "");
			timedown_min.setText(mmin + "");
			timedown_second.setText(msecond + "");
			if ( mhour <= 0 && mmin <= 0 && msecond <= 0) {
				if(timeEndListner !=  null)
					timeEndListner.isTimeEnd();
				run = false;
				removeCallbacks(this);
				return;
			}
			postDelayed(this, 1000);
		}
	}
	private TimeEndListner timeEndListner;
	
	public void setTimeEndListner(TimeEndListner timeEndListner) {
		this.timeEndListner = timeEndListner;
	}

}
