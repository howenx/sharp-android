/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-14 下午3:08:01 
**/
package com.hanmimei.manager;

import android.widget.TextView;


/**
 * @author eric
 *
 */
public class MyCouponMenager {
	private TextView nums;
	private static class CouponMenagerHolder {
		public static final MyCouponMenager instance = new MyCouponMenager();
	}

	public static MyCouponMenager getInstance() {
		return CouponMenagerHolder.instance;
	}
	public void initCouponMenager(TextView nums){
		this.nums = nums;
	}
	public void setNums(int t1){
		nums.setText(t1 + " 张可用");
	}
}
