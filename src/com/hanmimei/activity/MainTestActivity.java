package com.hanmimei.activity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.hanmimei.R;
import com.hanmimei.adapter.TabPagerAdapter;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.VersionVo;
import com.hanmimei.fragment.HomeFragment;
import com.hanmimei.fragment.MineFragment;
import com.hanmimei.fragment.ShoppingCartFragment;
import com.hanmimei.manager.BadgeViewManager;
import com.hanmimei.manager.MessageMenager;
import com.hanmimei.override.ViewPageChangeListener;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.DoJumpUtils;
import com.hanmimei.utils.DownloadTools;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.utils.XMLPaserTools;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.BaseIconFragment;
import com.viewpagerindicator.IconTabPageIndicator;
import com.viewpagerindicator.IconTabPageIndicator.OnTabReselectedListener;

@SuppressLint("NewApi")
public class MainTestActivity extends BaseActivity implements OnClickListener {

	private DownloadTools downloadTools;

	private VersionVo info;

	private MainBroadCastReceiver netReceiver;
	private ViewPager mViewPager;
	// 极光推送
	public static boolean isForeground = false;

	private boolean isHome = true;
	IconTabPageIndicator mIndicator;

	private List<BaseIconFragment> fragments;
//	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_layout);
		ActionBarUtil.setActionBarStyle(this, "韩秘美",R.drawable.hmm_icon_message_n, false, this);
		ImageView view = (ImageView) findViewById(R.id.setting);
		MessageMenager.getInstance().initMessageMenager(this,view);
		// 关闭左滑退出
		closeSwipeBack();
//		showGuangGao();
		initViewPager();
		registerReceivers();
		doCheckVersionTask();
	}

	private void showGuangGao() {
		View view = LayoutInflater.from(this).inflate(R.layout.guanggao_layout, null);
		final Dialog dialog = new Dialog(this,R.style.CustomDialog);
		dialog.setContentView(view);
		dialog.show();
		view.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.gg_img).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				ToastUtils.Toast(MainTestActivity.this, "你点击了广告！！！");
			}
		});
	}

	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		fragments = initFragmentList();
		mIndicator = (IconTabPageIndicator) findViewById(R.id.indicator);

		mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(),
				fragments));
		mViewPager.setOffscreenPageLimit(3);
		mIndicator.setViewPager(mViewPager);
		mIndicator.setOnPageChangeListener(new ViewPageChangeListener(){

			@Override
			public void onPageSelected(int arg0) {
				initActionBar(arg0);
			}
			
		});
		mIndicator.setOnTabReselectedListener(new OnTabReselectedListener() {

			@Override
			public void onTabReselected(int position) {
				if (position == 0) {
					sendBroadcast(new Intent(
							AppConstant.MESSAGE_BROADCAST_UP_HOME_ACTION));
				}
			}
		});

		BadgeViewManager.getInstance().initBadgeViewManager(this,
				mIndicator.getTabViews().get(1));

	}

	private void initActionBar(int position) {
		/** 如果当前选项卡是home */
		if (position == 0) {
			isHome = true;
			ActionBarUtil.setActionBarStyle(this, "韩秘美",
					R.drawable.hmm_icon_message_n, false, this);
			/** 如果当前选项卡是shopping */
		} else if (position == 1) {
			ActionBarUtil.setActionBarStyle(this, "购物车", 0, false, this);
			/** 如果当前选项卡是my */
		} else if (position == 2) {
			isHome = false;
			ActionBarUtil.setActionBarStyle(this, "",
					R.drawable.hmm_icon_setting, false, this);
		}
		startShimmerAnimation();
	}

	private List<BaseIconFragment> initFragmentList() {
		List<BaseIconFragment> fragments = new ArrayList<>();
		fragments.add(new HomeFragment());
		fragments.add(new ShoppingCartFragment());
		fragments.add(new MineFragment());
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
					DoJumpUtils.doJump(this, MessageTypeActivity.class);
				}
			} else {
				DoJumpUtils.doJump(this, SettingActivity.class);
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
				MobclickAgent.onKillProcess(this);
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
		Http2Utils.doGetRequestTask(this, UrlUtil.UPDATE_HMM,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						try {
							InputStream is = new ByteArrayInputStream(result
									.getBytes());
							info = XMLPaserTools.getUpdataInfo(is);
							if (!info.getReleaseNumber().equals(
									CommonUtil.getVersionName(getActivity()))) {
								// 版本号不同,发送消息更新客户端
								setVersionInfo(info);
								AlertDialogUtils.showUpdate2Dialog(
										getActivity(), new OnClickListener() {

											@Override
											public void onClick(View v) {
												downloadApk(info.getDownloadLink());
											}
										});

							}
						} catch (Exception e) {
							ToastUtils.Toast(getActivity(), "获取服务器更新信息失败");
						}
					}

					@Override
					public void onError() {
						ToastUtils.Toast(getActivity(), "获取服务器更新信息失败");
					}
				});
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
		downloadTools = new DownloadTools(getApplicationContext());
		downloadTools.download(url);
	}

}
