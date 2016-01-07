package com.hanmimei.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

import com.hanmimei.R;
import com.hanmimei.data.AppConstant;
import com.hanmimei.fragment.AboutMyFragment;
import com.hanmimei.fragment.FragmentTabHost;
import com.hanmimei.fragment.HomeFragment;
import com.hanmimei.fragment.PinFragment;
import com.hanmimei.fragment.ShoppingCartFragment;
import com.hanmimei.manager.TabHostManager;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.DoJumpUtils;
import com.hanmimei.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements OnTabChangeListener,
		OnClickListener {

	private static final String TAB_HOME_ID = "tab_01";
	private static final String TAB_CAR_ID = "tab_02";
	private static final String TAB_MY_ID = "tab_03";
	private static final String TAB_PIN_ID = "tab_04";
	private static final String TAB_HOME = "首页";
	private static final String TAB_CAR = "购物车";
	private static final String TAB_MY = "我的";
	private static final String TAB_PIN = "拼购";
	private static final int home_drawable = R.drawable.tab_home;
	private static final int shopping_drawable = R.drawable.tab_shopping;
	private static final int my_drawable = R.drawable.tab_my;
	private static final int pingou_drawable = R.drawable.tab_pingou;

	private MainBroadCastReceiver netReceiver;
	private FragmentTabHost mTabHost;
	private LinearLayout guanggao;
//	private View state_bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (VERSION.SDK_INT >= 19) {
//			getWindow().addFlags(
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		}
		setContentView(R.layout.activity_main);
		ActionBarUtil.setActionBarStyle(this, "", 0, false, this);
//		state_bar = findViewById(R.id.state_bar);
		guanggao = (LinearLayout) findViewById(R.id.guanggao);
		guanggao.setOnClickListener(this);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realcontent);
//		if (VERSION.SDK_INT >= 19) {
//			LayoutParams params;
//			params = state_bar.getLayoutParams();
//			params.height = CommonUtil.getStatusBarHeight(this);
//			state_bar.setLayoutParams(params);
//		}
		findViewById(R.id.close).setOnClickListener(this);
		findViewById(R.id.gg_img).setOnClickListener(this);
		mTabHost.setOnTabChangedListener(this);
		TabHostManager.getInstance().initTabHostManager(this, mTabHost,
				R.layout.tab_item_layout);
		TabHostManager.getInstance().initTabItem(TAB_HOME_ID, home_drawable,
				TAB_HOME, HomeFragment.class);
		TabHostManager.getInstance().initTabItem(TAB_PIN_ID, pingou_drawable,
				TAB_PIN, PinFragment.class);
		TabHostManager.getInstance().initTabItem(TAB_CAR_ID, shopping_drawable,
				TAB_CAR, ShoppingCartFragment.class);
		TabHostManager.getInstance().initTabItem(TAB_MY_ID, my_drawable,
				TAB_MY, AboutMyFragment.class);
		registerReceivers();
	}

	@Override
	public void onTabChanged(String tabId) {
		/** 如果当前选项卡是home */
		if (tabId.equals(TAB_HOME_ID)) {
			ActionBarUtil.setActionBarStyle(this, "韩秘美", 0, false, this);
			/** 如果当前选项卡是shopping */
		} else if (tabId.equals(TAB_CAR_ID)) {
			ActionBarUtil.setActionBarStyle(this, "购物车", 0, false, this);
			/** 如果当前选项卡是my */
		} else if (tabId.equals(TAB_PIN_ID)) {
			ActionBarUtil.setActionBarStyle(this, "拼购", 0, false, this);
			/** 如果当前选项卡是my */
		}else if (tabId.equals(TAB_MY_ID)) {
			ActionBarUtil.setActionBarStyle(this, "", R.drawable.icon_setting,
					false, this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting:
			DoJumpUtils.doJump(this, SettingActivity.class);
			break;
		case R.id.close:
			guanggao.setVisibility(View.GONE);
			break;
		case R.id.gg_img:
			Toast.makeText(this, "你点击了广告！！！", Toast.LENGTH_SHORT).show();
			guanggao.setVisibility(View.GONE);
			break;
		case R.id.guanggao:
			guanggao.setVisibility(View.GONE);
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
		if (mTabHost.getCurrentTab() != 0) {
			mTabHost.setCurrentTab(0);
		} else {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				ToastUtils.Toast(this, "再按一次退出程序");
				mExitTime = System.currentTimeMillis();

			} else {
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

//广播接收者 注册
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
				mTabHost.setCurrentTab(0);
			}
		}
	}
	


	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
	
	

}
