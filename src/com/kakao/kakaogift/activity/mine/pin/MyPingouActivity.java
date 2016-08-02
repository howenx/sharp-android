package com.kakao.kakaogift.activity.mine.pin;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.mine.pin.adapter.PinPagerAdapter;
import com.kakao.kakaogift.activity.mine.pin.fragment.MyPinFragment;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.PinList;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.BadgeView;

/**
 * 
 * @author vince
 * 
 */
public class MyPingouActivity extends BaseActivity {

	private ViewPager viewPager;
	private PagerSlidingTabStrip pagerSlidingTabStrip;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_pingou_pager_layout);
		ActionBarUtil.setActionBarStyle(this, "我的拼购");

		viewPager = (ViewPager) findViewById(R.id.view_pager);
		pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		loadData();

	}

	private void loadData() {
		getLoading().show();
		VolleyHttp.doGetRequestTask(getHeaders(), UrlUtil.GET_MY_PINTUAN,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						Log.e("pin:pintuan", result);
						PinList list = new Gson().fromJson(result,
								PinList.class);
						try {
							initData(list);
						} catch (Exception e) {
							ToastUtils.Toast(getActivity(), R.string.error);
						}
						getLoading().dismiss();
					}

					@Override
					public void onError() {
						Log.e("pin:pintuan", "1111111111111");
						ToastUtils.Toast(getActivity(), R.string.error);
						getLoading().dismiss();
					}
				});
	}

	private void initData(PinList list) {
		if (list.getMessage().getCode() == 200) {
			List<String> titles = new ArrayList<String>();
			titles.add("我的开团");
			titles.add("我的参团");

			List<Fragment> fragments = new ArrayList<Fragment>();
			MyPinFragment masterFragment = MyPinFragment.newInstance(list
					.getActivityListForMaster());
			MyPinFragment memberFragment = MyPinFragment.newInstance(list
					.getActivityListForMember());
			fragments.add(masterFragment);
			fragments.add(memberFragment);

			PinPagerAdapter adapter = new PinPagerAdapter(
					this.getSupportFragmentManager(), fragments, titles);

			viewPager.setAdapter(adapter);
			pagerSlidingTabStrip.setViewPager(viewPager);
			if (list.getActivityListForMaster().size() <= 0
					&& list.getActivityListForMember().size() > 0) {
				viewPager.setCurrentItem(1);
			}
		} else {
			ToastUtils.Toast(this, list.getMessage().getMessage());
		}
	}

}
