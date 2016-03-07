package com.hanmimei.activity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.VersionVo;
import com.hanmimei.fragment.AboutMyFragment;
import com.hanmimei.fragment.FragmentTabHost;
import com.hanmimei.fragment.HomeFragment;
import com.hanmimei.fragment.ShoppingCartFragment;
import com.hanmimei.manager.BadgeViewManager;
import com.hanmimei.manager.MessageMenager;
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

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements OnTabChangeListener,
		OnClickListener {

	private String TAB_HOME_ID = "tab_01";
	private String TAB_CAR_ID = "tab_02";
	private String TAB_MY_ID = "tab_03";
	private String TAB_PIN_ID = "tab_04";
	private String TAB_HOME = "首页";
	private String TAB_CAR = "购物车";
	private String TAB_MY = "我的";
	private int home_drawable = R.drawable.tab_home;
	private int shopping_drawable = R.drawable.tab_shopping;
	private int my_drawable = R.drawable.tab_my;
//	private int pingou_drawable = R.drawable.tab_pingou;
	private DownloadTools downloadTools;
	
	private VersionVo info;

	private MainBroadCastReceiver netReceiver;
	private FragmentTabHost mTabHost;
	private LinearLayout guanggao;
	//极光推送
	public static boolean isForeground = false;
	
	private boolean isHome = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBarUtil.setActionBarStyle(this, "", 0, false, this);
		guanggao = (LinearLayout) findViewById(R.id.guanggao);
		guanggao.setOnClickListener(this);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realcontent);
		findViewById(R.id.close).setOnClickListener(this);
		findViewById(R.id.gg_img).setOnClickListener(this);
		mTabHost.setOnTabChangedListener(this);

		addTabItem(TAB_HOME_ID, home_drawable, TAB_HOME, HomeFragment.class);
		addTabItem(TAB_CAR_ID, shopping_drawable, TAB_CAR,ShoppingCartFragment.class);
		addTabItem(TAB_MY_ID, my_drawable, TAB_MY, AboutMyFragment.class);

		BadgeViewManager.getInstance().initBadgeViewManager(this, mTabHost);
		registerReceivers();
		BadgeViewManager.getInstance().initBadgeViewManager(this, mTabHost);
		mTabHost.getTabWidget().getChildAt(0).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTabHost.setCurrentTab(0);
				sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_UP_HOME_ACTION));
			}
		});	
		submitTask(new CheckVersionTask());
	}
	
	

	@Override
	public void onTabChanged(String tabId) {
		/** 如果当前选项卡是home */
		if (tabId.equals(TAB_HOME_ID)) {
			isHome = true;
			ActionBarUtil.setActionBarStyle(this, "韩秘美", R.drawable.icon_xiaoxi, false, this);
			ImageView setting = (ImageView) findViewById(R.id.setting);
			MessageMenager.getInstance().initMessageMenager(this, setting);
			/** 如果当前选项卡是shopping */
		} else if (tabId.equals(TAB_CAR_ID)) {
			ActionBarUtil.setActionBarStyle(this, "购物车", 0, false, this);
			/** 如果当前选项卡是my */
		} else if (tabId.equals(TAB_PIN_ID)) {
			ActionBarUtil.setActionBarStyle(this, "拼购", 0, false, this);
			/** 如果当前选项卡是my */
		} else if (tabId.equals(TAB_MY_ID)) {
			isHome = false;
			ActionBarUtil.setActionBarStyle(this, "", R.drawable.icon_setting,
					false, this);
		}
	}

	/**
	 * 初始化tab
	 * 
	 * @param tag  标签
	 * @param title  tab 标题
	 * @param img  tab 图标
	 * @param clzss  显示fragment
	 */
	@SuppressLint("InflateParams")
	private void addTabItem(String tag, int img, String title, Class<?> clzss) {
		View indaicatorView = getLayoutInflater().inflate(
				R.layout.tab_item_layout, null);
		ImageView tabIcon = (ImageView) indaicatorView.findViewById(R.id.img);
		TextView tabTitle = (TextView) indaicatorView.findViewById(R.id.name);
		if (tabIcon != null && img != 0)
			tabIcon.setImageResource(img);
		if (tabTitle != null && title != null)
			tabTitle.setText(title);
		TabSpec tab = mTabHost.newTabSpec(tag).setIndicator(indaicatorView);
		mTabHost.addTab(tab, clzss, null);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting:
			if(isHome){
				if (getUser() == null) {
					startActivity(new Intent(getActivity(), LoginActivity.class));
				} else {
					DoJumpUtils.doJump(this, MessageTypeActivity.class);
				}
			}else{
				DoJumpUtils.doJump(this, SettingActivity.class);
			}
			break;
		case R.id.close:
			guanggao.setVisibility(View.GONE);
			break;
		case R.id.gg_img:
			ToastUtils.Toast(this, "你点击了广告！！！");
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
	
	private void doCheckVersionTask(){
		Http2Utils.doGetRequestTask(this, UrlUtil.UPDATE_HMM, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				try {
					InputStream is = new   ByteArrayInputStream(result.getBytes());
					info = XMLPaserTools.getUpdataInfo(is);
					if (!info.getReleaseNumber().equals(CommonUtil.getVersionName(getActivity()))) {
						// 版本号不同,发送消息更新客户端
						
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
	
	/**
	 * 开启子线程run方法,服务器请求更新版本
	 * 
	 * @author Administrator
	 *
	 */
	private class CheckVersionTask implements Runnable {
		InputStream is;

		public void run() {
			try {
				String path = UrlUtil.UPDATE_HMM;
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				if (conn.getResponseCode() == 200) {
					// 从服务器获得一个输入流
					is = conn.getInputStream();
					info = XMLPaserTools.getUpdataInfo(is);
					if (!info.getReleaseNumber().equals(CommonUtil.getVersionName(getActivity()))) {
						// 版本号不同,发送消息更新客户端
						handler.sendEmptyMessage(VersionVo.UPDATA_CLIENT);
					} 
				}else{
					handler.sendEmptyMessage(VersionVo.GET_UNDATAINFO_ERROR);
				}
			} catch (Exception e) {
				handler.sendEmptyMessage(VersionVo.GET_UNDATAINFO_ERROR);
			}
		}
	}

	/**
	 * 定义handler 接收信息 判断是否更新客户端
	 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case VersionVo.UPDATA_CLIENT:
				setVersionInfo(info);
				AlertDialogUtils.showUpdateDialog(getActivity(), info, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						downloadApk(info.getDownloadLink());
					}
				});
				break;
			case VersionVo.GET_UNDATAINFO_ERROR:
				// 服务器超时
				ToastUtils.Toast(getActivity(), "获取服务器更新信息失败");
				break;
			}
		}
	};
	

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
			if (intent.getAction().equals(AppConstant.MESSAGE_BROADCAST_GO_HOME)) {
				mTabHost.setCurrentTab(0);
			}
		}
	}

	@Override
	public void onResume() {
		isForeground = true;
		super.onResume();
		
	}
	
	private void downloadApk(String url){
		downloadTools = new DownloadTools(getApplicationContext());
		downloadTools.download(url);
	}
	

}
