/**
 * @Description: TODO(商品评价) 
 * @author vince.刘奇
 * @date 2016-4-25 下午2:34:19 
**/
package com.hanmimei.activity;

import com.astuetz.PagerSlidingTabStrip;
import com.hanmimei.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

/**
 * @author vince
 *
 */
public class GoodsEvaluateActivity extends BaseActivity {
	
	private PagerSlidingTabStrip tabStrips;
	private ViewPager mViewPager;
	
	
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_evaluate_layout);
		
		
		
	}

}
