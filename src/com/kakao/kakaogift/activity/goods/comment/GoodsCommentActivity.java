/**
 * @Description: TODO(商品评价) 
 * @author vince.刘奇
 * @date 2016-4-25 下午2:34:19 
**/
package com.kakao.kakaogift.activity.goods.comment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.goods.comment.fragment.GoodsComImgFragment;
import com.kakao.kakaogift.activity.goods.comment.fragment.GoodsCommentFragment;
import com.kakao.kakaogift.activity.goods.comment.fragment.GoodsCommentFragment.EvaType;
import com.kakao.kakaogift.event.CommentEvent;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.viewpagerindicator.BaseIconFragment;
import com.ypy.eventbus.EventBus;

/**
 * @author vince
 *
 */
public class GoodsCommentActivity extends BaseActivity {
	
	private PagerSlidingTabStrip tabStrips;
	private ViewPager mViewPager;
	
	
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		setContentView(R.layout.goods_comment_layout);
		
		ActionBarUtil.setActionBarStyle(this, "商品评价");
		
		tabStrips = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		
		String skuType = getIntent().getStringExtra("skuType");
		String skuTypeId = getIntent().getStringExtra("skuTypeId");
		
		List<BaseIconFragment> fragments = new ArrayList<>();
		BaseIconFragment allFragment = new GoodsCommentFragment(EvaType.all,skuType,skuTypeId);
		BaseIconFragment goodFragment = new GoodsCommentFragment(EvaType.good,skuType,skuTypeId);
		BaseIconFragment badFragment = new GoodsCommentFragment(EvaType.bad,skuType,skuTypeId);
		BaseIconFragment picFragment = new GoodsComImgFragment(skuType,skuTypeId);
		
		fragments.add(allFragment);
		fragments.add(goodFragment);
		fragments.add(badFragment);
		fragments.add(picFragment);
		CommentPagerAdapter adapter = new CommentPagerAdapter(getSupportFragmentManager(), fragments);
		
		mViewPager.setAdapter(adapter);
		mViewPager.setOffscreenPageLimit(4);
		tabStrips.setViewPager(mViewPager);
	}
	
	
	public class CommentPagerAdapter extends FragmentPagerAdapter {
		private List<BaseIconFragment> fragments;

		public CommentPagerAdapter(FragmentManager fm, List<BaseIconFragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return fragments.get(position).getTitle();
		}

	}
	
	

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}



	public void onEvent(CommentEvent event) {
		tabStrips.notifyDataSetChanged(event.getPositon());
	}
}
