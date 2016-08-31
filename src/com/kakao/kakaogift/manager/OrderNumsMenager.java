package com.kakao.kakaogift.manager;

import java.io.Serializable;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * @author eric
 *
 */
public class OrderNumsMenager implements Serializable {

	private TextView t1_num;
	private TextView t2_num;
	private TextView t3_num;
	
	
	
	public OrderNumsMenager() {
	}
	public void initOrderMenager(Context mContext, TextView t1_num, TextView t2_num, TextView t3_num){
		this.t1_num = t1_num;
		this.t2_num = t2_num;
		this.t3_num = t3_num;
	}
	public void numsChanged(int nums1,int nums2,int nums3){
		setNoPayNums(nums1);
		setPayNums(nums2);
		setCommetNums(nums3);	
	}
	public void setNoPayNums(int nums){
		if(nums == 0){
			t1_num.setVisibility(View.INVISIBLE);
		}else{
			t1_num.setVisibility(View.VISIBLE);
			t1_num.setText("" + nums);
		}
	}
	public void setPayNums(int nums){
		if(nums == 0){
			t2_num.setVisibility(View.INVISIBLE);
		}else{
			t2_num.setVisibility(View.VISIBLE);
			t2_num.setText("" + nums);
		}
	}
	public void setCommetNums(int nums){
		if(nums == 0){
			t3_num.setVisibility(View.INVISIBLE);
		}else{
			t3_num.setVisibility(View.VISIBLE);
			t3_num.setText("" + nums);
		}
	}
	
	
}