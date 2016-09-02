/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-14 下午3:08:01 
**/
package com.kakao.kakaogift.manager;

import android.widget.TextView;


/**
 * @author eric
 *
 */
public class CouponMenager{
	private TextView title1;
	private TextView title2;
	private TextView title3;

	

	public CouponMenager() {
		super();
	}
	public void initCouponMenager(TextView title1, TextView title2, TextView title3){
		this.title1 = title1;
		this.title2 = title2;
		this.title3 = title3;
	}
	public void setTitle(String t1, String t2, String t3){
		title1.setText(t1);
		title2.setText(t2);
		title3.setText(t3);
	}
}
