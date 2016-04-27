package com.hanmimei.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.activity.EditUserInfoActivity;
import com.hanmimei.activity.LoginActivity;
import com.hanmimei.activity.LogisticsActivity;
import com.hanmimei.activity.MyAddressActivity;
import com.hanmimei.activity.MyCollectionActivity;
import com.hanmimei.activity.MyCouponActivity;
import com.hanmimei.activity.MyOrderActivity;
import com.hanmimei.activity.MyPingouActivity;
import com.hanmimei.activity.PubCommentActivity;
import com.hanmimei.dao.UserDao;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.User;
import com.hanmimei.utils.GlideLoaderUtils;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.view.RoundImageView;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.BaseIconFragment;

/**
 * @author eric
 *
 */
public class MineFragment extends BaseIconFragment implements OnClickListener {
	private RoundImageView header;
	private TextView user_name;
	private TextView youhui_nums;
	private ImageView sex;
	private BaseActivity activity;
	private User user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.wode_layout, null);
		user = activity.getUser();
		findView(view);
		if (user != null)
			getUserInfo();
		registerReceivers();
		return view;
	}

	private void getUserInfo() {
		activity.getLoading().show();
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
		GlideLoaderUtils.loadCirlceImage(activity,user.getUserImg(), header);
		user_name.setText(user.getUserName());
		sex.setVisibility(View.VISIBLE);
		if (user.getSex().equals("F")) {
			sex.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.hmm_mine_female));
		} else {
			sex.setImageDrawable(activity.getResources().getDrawable(
					R.drawable.hmm_mine_male));
		}
		int couponCount;
		if (user.getCouponCount() == null) {
			couponCount = 0;
		} else {
			couponCount = user.getCouponCount();
		}
		youhui_nums.setText(couponCount + " 张可用");

	}

	private void clearView() {
		user = activity.getUser();
		user_name.setText("登录/注册");
		header.setImageResource(R.drawable.hmm_mine_face);
		sex.setVisibility(View.GONE);
	}

	private void findView(View view) {
		header = (RoundImageView) view.findViewById(R.id.header);
		user_name = (TextView) view.findViewById(R.id.user_name);
		sex = (ImageView) view.findViewById(R.id.sex);
		youhui_nums = (TextView) view.findViewById(R.id.youhui_nums);
		header.setOnClickListener(this);
		view.findViewById(R.id.order).setOnClickListener(this);
		view.findViewById(R.id.address).setOnClickListener(this);
		user_name.setOnClickListener(this);
		view.findViewById(R.id.collect).setOnClickListener(this);
		view.findViewById(R.id.youhui_linear).setOnClickListener(this);
		view.findViewById(R.id.pintuan).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header:
			doJump(EditUserInfoActivity.class);
			break;
		case R.id.order:
			doJump(MyOrderActivity.class);
//			doJump(PubCommentActivity.class);
			break;
		case R.id.address:
			doJump(MyAddressActivity.class);
			break;
		case R.id.user_name:
			doJump(EditUserInfoActivity.class);
			break;
		case R.id.collect:
			doJump(MyCollectionActivity.class);
			break;
		case R.id.youhui_linear:
			doJump(MyCouponActivity.class);
			break;
		case R.id.pintuan:
			doJump(MyPingouActivity.class);
			break;
		default:
			break;
		}
	}

	private void doJump(Class clazz) {
		if (activity.getUser() == null) {
			getActivity().startActivityForResult(
					new Intent(getActivity(), LoginActivity.class),
					AppConstant.LOGIN_CODE);
		} else {
			startActivity(new Intent(getActivity(), clazz));
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				activity.getLoading().dismiss();
				User userinfo = (User) msg.obj;
				if (userinfo.getUserName() != null) {
					user.setUserName(userinfo.getUserName());
					user.setPhone(userinfo.getPhone());
					user.setUserImg(userinfo.getUserImg());
					user.setCouponCount(userinfo.getCouponCount());
					user.setSex(userinfo.getSex());
					UserDao userDao = activity.getDaoSession().getUserDao();
					userDao.deleteAll();
					userDao.insert(user);
					initView();
				} else {
					initView();
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

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("AboutMyFragment"); // 统计页面，"MainScreen"为页面名称，可自定义
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("AboutMyFragment");
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "我的";
	}

	@Override
	public int getIconId() {
		// TODO Auto-generated method stub
		return R.drawable.tab_my;
	}

}
