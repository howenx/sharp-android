package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.activity.fragment.MyPinFragment;
import com.hanmimei.adapter.PinPagerAdapter;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.PinList;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;

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
		Http2Utils.doGetRequestTask(this, getHeaders(), UrlUtil.GET_MY_PINTUAN,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						PinList list = new Gson().fromJson(result, PinList.class);
						try {
							initData(list);
						} catch (Exception e) {
							ToastUtils.Toast(getActivity(), R.string.error);
						}
						getLoading().dismiss();
					}

					@Override
					public void onError() {
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
			if(list.getActivityListForMaster().size()<=0 && list.getActivityListForMember().size()>0){
				viewPager.setCurrentItem(1);
			}
		} else {
			ToastUtils.Toast(this, list.getMessage().getMessage());
		}
	}

}
