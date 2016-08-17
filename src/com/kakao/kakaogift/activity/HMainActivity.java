package com.kakao.kakaogift.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.login.LoginActivity;
import com.kakao.kakaogift.activity.mine.config.SettingActivity;
import com.kakao.kakaogift.activity.mine.message.MessageTypeActivity;
import com.kakao.kakaogift.activity.presenter.hmain.HMainPresenter;
import com.kakao.kakaogift.activity.presenter.hmain.HMainPresenterImpl;
import com.kakao.kakaogift.activity.view.HMainView;
import com.kakao.kakaogift.adapter.TabPagerAdapter;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.entity.VersionVo;
import com.kakao.kakaogift.fragment.HomeFragment;
import com.kakao.kakaogift.fragment.MineFragment;
import com.kakao.kakaogift.fragment.ShoppingCartFragment;
import com.kakao.kakaogift.manager.BadgeViewManager;
import com.kakao.kakaogift.manager.HDownloadManager;
import com.kakao.kakaogift.utils.AlertDialogUtils;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.SlidingViewPager;
import com.viewpagerindicator.BaseIconFragment;
import com.viewpagerindicator.IconTabPageIndicator;
import com.viewpagerindicator.IconTabPageIndicator.OnTabReselectedListener;

/**
 * 
 * @author vince
 * 
 */
@SuppressLint("NewApi")
public class HMainActivity extends BaseActivity implements OnClickListener,
		HMainView {

	private MainBroadCastReceiver netReceiver;
	private SlidingViewPager mViewPager;
	public static boolean isForeground = false;

	private boolean isHome = true;
	IconTabPageIndicator mIndicator;

	
	private List<BaseIconFragment> fragments; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_layout);
		
		
		// 关闭滑动退出
		setBackEnable(false);
		initViewPager();
		registerReceivers();
		doCheckVersionTask();
		
	}


	private void initViewPager() {
		mViewPager = (SlidingViewPager) findViewById(R.id.id_viewpager);
		mViewPager.setNoScroll(true);
		fragments = initFragmentList();
		mIndicator = (IconTabPageIndicator) findViewById(R.id.indicator);

		mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(),
				fragments));
		mViewPager.setOffscreenPageLimit(3);
		mIndicator.setViewPager(mViewPager);
		mIndicator.setOnTabReselectedListener(new OnTabReselectedListener() {

			@Override
			public void onTabReselected(int position) {
				if (position == 0) {
					sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_UP_HOME_ACTION));
				}
			}
		});

		BadgeViewManager.getInstance().initBadgeViewManager(this, mIndicator.getTabViews().get(1));

	}


	private List<BaseIconFragment> initFragmentList() {
		List<BaseIconFragment> fragments = new ArrayList<>();
		HomeFragment homeFragment = new HomeFragment();
		ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
		MineFragment mineFragment = new MineFragment();
		fragments.add(homeFragment);
		fragments.add(shoppingCartFragment);
		fragments.add(mineFragment);
		return fragments;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting:
			if (isHome) {
				if (getUser() == null) {
					startActivity(new Intent(getActivity(), LoginActivity.class));
				} else {
					startActivity(new Intent(getActivity(), MessageTypeActivity.class));
				}
			} else {
				startActivity(new Intent(getActivity(), SettingActivity.class));
			}
			break;
		default:
			break;
		}
	}

	// 主界面返回之后在后台运行
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitClick();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private long mExitTime;

	/**
	 * 退出函数
	 */
	private void exitClick() {
		if (mViewPager.getCurrentItem() != 0) {
			mViewPager.setCurrentItem(0);
		} else {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				ToastUtils.Toast(this, "再按一次退出程序");
				mExitTime = System.currentTimeMillis();

			} else {
				setClipboard();
				finish();
				 System.exit(0);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netReceiver);
	}

	private void doCheckVersionTask() {
		HMainPresenter hMainPresenter = new HMainPresenterImpl(this);
		hMainPresenter.checkVersionInfo();
	}

	// 广播接收者 注册
	private void registerReceivers() {
		netReceiver = new MainBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_GO_HOME);
		registerReceiver(netReceiver, intentFilter);
	}

	private class MainBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction()
					.equals(AppConstant.MESSAGE_BROADCAST_GO_HOME)) {
				mViewPager.setCurrentItem(0);
			}
		}
	}
	
	

	@Override
	public void onResume() {
		isForeground = true;
		super.onResume();

	}

	private void downloadApk(String url) {
		HDownloadManager downloadTools = new HDownloadManager(
				getApplicationContext());
		downloadTools.download(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.view.HMainView#loadVersionInfo(com.kakao.kakaogift.entity
	 * .VersionVo)
	 */
	@Override
	public void loadVersionInfo(final VersionVo info) {
		if(info.getReleaseNumber() <= CommonUtils.getVersionCode(this))
			return;
		setVersionInfo(info);
		AlertDialogUtils.showUpdateDialog(getActivity(),
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						downloadApk(info.getDownloadLink());
					}
				});

	}

	@Override
	public void onLoadFailed(String msg) {
		ToastUtils.Toast(getActivity(), msg);
	}

}
