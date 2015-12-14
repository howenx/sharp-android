package com.hanmimei.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hanmimei.R;
import com.hanmimei.activity.AdressActivity;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.activity.CouponActivity;
import com.hanmimei.activity.EditUserInfoActivity;
import com.hanmimei.activity.LoginActivity;
import com.hanmimei.activity.MyOrderActivity;
import com.hanmimei.dao.UserDao;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.User;
import com.hanmimei.utils.DoJumpUtils;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.InitImageLoader;
import com.hanmimei.utils.KeyWordUtil;
import com.hanmimei.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AboutMyFragment extends Fragment implements OnClickListener {
	// private Drawable authenticate;
	// private Drawable un_authenticate;
	private Drawable address_icon;
	private Drawable order_icon;
	private Drawable shenfen_icon;
	private Drawable youhui_icon;
	private Drawable about_icon;
	private Drawable jiantou_icon;
	private RoundImageView header;
	private TextView user_name;
	private TextView address;
	private TextView order;
	private TextView youhui;

	private BaseActivity activity;
	private User user;
	private ImageLoader imageLoader;
	private DisplayImageOptions imageOptions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initIcon();
		activity = (BaseActivity) getActivity();
	}

	private void initIcon() {
		address_icon = getResources().getDrawable(R.drawable.icon_address);
		address_icon.setBounds(0, 0, 40, 40);
		order_icon = getResources().getDrawable(R.drawable.icon_dingdan);
		order_icon.setBounds(0, 0, 40, 40);
		shenfen_icon = getResources().getDrawable(R.drawable.icon_shenfenzheng);
		shenfen_icon.setBounds(0, 0, 40, 40);
		youhui_icon = getResources().getDrawable(R.drawable.icon_youhuiquan);
		youhui_icon.setBounds(0, 0, 40, 40);
		about_icon = getResources().getDrawable(R.drawable.icon_about);
		about_icon.setBounds(0, 0, 40, 40);
		jiantou_icon = getResources().getDrawable(R.drawable.icon_jiantou);
		jiantou_icon.setBounds(0, 0, 40, 40);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.wode_layout, null);
		user = activity.getUser();
		findView(view);
		if(user != null)
			getUserInfo();
		registerReceivers();
		return view;
	}

	private void getUserInfo() {
		user = activity.getUser();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.get(UrlUtil.GET_USERINFO_URL,
						user.getToken());
				User userInfo = DataParser.parserUserInfo(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = userInfo;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	public void initView() {
		user = activity.getUser();
		imageLoader = InitImageLoader.initLoader(getActivity());
		imageOptions = InitImageLoader.initOptions();
		imageLoader.displayImage(user.getUserImg(), header, imageOptions);
		user_name.setText(user.getUserName());
		String show = "    优惠券        " + user.getCouponCount() + "张可用";
		KeyWordUtil.setDifrentFontStyle(getActivity(), youhui, show, 14, show.length());
//		SpannableString styledText = new SpannableString(show);  
//        styledText.setSpan(new TextAppearanceSpan(getActivity(), R.style.youhui1), 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
//        styledText.setSpan(new TextAppearanceSpan(getActivity(), R.style.youhui2), 14, show.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
//		youhui.setText(styledText, TextView.BufferType.SPANNABLE);
	}

	private void clearView() {
		user = activity.getUser();
		imageLoader = InitImageLoader.initLoader(getActivity());
		imageOptions = InitImageLoader.initOptions();
		imageLoader.displayImage("", header, imageOptions);
		user_name.setText("点击登录");
	}

	private void findView(View view) {
		header = (RoundImageView) view.findViewById(R.id.header);
		user_name = (TextView) view.findViewById(R.id.user_name);

		address = (TextView) view.findViewById(R.id.address);
		address.setCompoundDrawables(address_icon, null, jiantou_icon, null);
		order = (TextView) view.findViewById(R.id.order);
		order.setCompoundDrawables(order_icon, null, jiantou_icon, null);
		youhui = (TextView) view.findViewById(R.id.youhui);
		youhui.setCompoundDrawables(youhui_icon, null, jiantou_icon, null);
		header.setOnClickListener(this);
		order.setOnClickListener(this);
		youhui.setOnClickListener(this);
		address.setOnClickListener(this);
		user_name.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header:
			doJump(EditUserInfoActivity.class);
			break;
		case R.id.order:
			doJump(MyOrderActivity.class);
			break;
		case R.id.youhui:
			doJump(CouponActivity.class);
			break;
		case R.id.address:
			doJump(AdressActivity.class);
			break;
		case R.id.user_name:
			doJump(EditUserInfoActivity.class);
			break;
		default:
			break;
		}
	}

	private void doJump(Class clazz) {
		if (activity.getUser() == null) {
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			getActivity()
					.startActivityForResult(intent, AppConstant.LOGIN_CODE);
		} else {
			DoJumpUtils.doJump(getActivity(), clazz);
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				User userinfo = (User) msg.obj;
				if (userinfo != null) {
					user.setUserName(userinfo.getUserName());
					user.setPhone(userinfo.getPhone());
					user.setUserImg(userinfo.getUserImg());
					user.setCouponCount(userinfo.getCouponCount());
					UserDao userDao = activity.getDaoSession().getUserDao();
					userDao.deleteAll();
					userDao.insert(user);
					initView();
				} else {

				}
				break;

			default:
				break;
			}
		}

	};
	private NetBroadCastReceiver netReceiver;

	// 广播接受者 注册
	private void registerReceivers() {
		netReceiver = new NetBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION);
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_QUIT_LOGIN_ACTION);
		getActivity().registerReceiver(netReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(netReceiver);
	}

	private class NetBroadCastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)) {
				getUserInfo();
			} else if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_QUIT_LOGIN_ACTION)) {
				clearView();
			}

		}

	}

}
