package com.hanbimei.fragment;

import com.hanbimei.R;
import com.hanbimei.activity.AboutIdCardActivity;
import com.hanbimei.activity.AdressActivity;
import com.hanbimei.activity.BaseActivity;
import com.hanbimei.activity.CouponActivity;
import com.hanbimei.activity.IdCardActivity;
import com.hanbimei.activity.LoginActivity;
import com.hanbimei.activity.MyOrderActivity;
import com.hanbimei.data.AppConstant;
import com.hanbimei.entity.User;
import com.hanbimei.utils.DoJumpUtils;
import com.hanbimei.utils.InitImageLoader;
import com.hanbimei.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutMyFragment extends Fragment implements OnClickListener{
	private Drawable authenticate;
	private Drawable un_authenticate;
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
	private TextView shenfenzheng;
	private TextView youhui;
	private TextView about;
	private TextView is_authenticate;

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
		authenticate = getResources().getDrawable(R.drawable.icon_authenticate);
		un_authenticate = getResources().getDrawable(R.drawable.icon_un_authenticate);
		un_authenticate.setBounds(0, 0, 30, 30);
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
		findView(view);
		if(activity.getUser() != null){
			initView();
		}
		return view;
	}

	public void initView() {
		user = activity.getUser();
		imageLoader = InitImageLoader.initLoader(getActivity());
		imageOptions = InitImageLoader.initOptions();
		imageLoader.displayImage(user.getUserImg(), header, imageOptions);
		is_authenticate.setText("未绑定");
		shenfenzheng.setVisibility(View.VISIBLE);
		about.setVisibility(View.VISIBLE);
		user_name.setText(user.getUserName());
		is_authenticate.setText("尚未绑定");
	}

	private void findView(View view) {
		header = (RoundImageView) view.findViewById(R.id.header);
		is_authenticate = (TextView) view.findViewById(R.id.do_authenticate);
		is_authenticate.setCompoundDrawables(un_authenticate, null, null, null);
		user_name = (TextView) view.findViewById(R.id.user_name);
		
		address = (TextView) view.findViewById(R.id.address);
		address.setCompoundDrawables(address_icon, null, jiantou_icon, null);
		order = (TextView) view.findViewById(R.id.order);
		order.setCompoundDrawables(order_icon, null, jiantou_icon, null);
		shenfenzheng = (TextView) view.findViewById(R.id.shenfenzheng);
		shenfenzheng.setVisibility(View.GONE);
		shenfenzheng.setCompoundDrawables(shenfen_icon, null, jiantou_icon, null);
		youhui = (TextView) view.findViewById(R.id.youhui);
		youhui.setCompoundDrawables(youhui_icon, null, jiantou_icon, null);
		about = (TextView) view.findViewById(R.id.about_card);
		about.setVisibility(View.GONE);
		about.setCompoundDrawables(about_icon, null, jiantou_icon, null);
		header.setOnClickListener(this);
		is_authenticate.setOnClickListener(this);
		order.setOnClickListener(this);
		youhui.setOnClickListener(this);
		shenfenzheng.setOnClickListener(this);
		address.setOnClickListener(this);
		about.setOnClickListener(this);
		user_name.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header:
			doJump(MyOrderActivity.class);
			break;
		case R.id.order:
			doJump(MyOrderActivity.class);
			break;
		case R.id.youhui:
			doJump(CouponActivity.class);
			break;
		case R.id.shenfenzheng:
			doJump(IdCardActivity.class);
			break;
		case R.id.address:
			doJump(AdressActivity.class);
			break;
		case R.id.do_authenticate:
			doJump(IdCardActivity.class);
			break;
		case R.id.about_card:
			doJump(AboutIdCardActivity.class);
			break;
		case R.id.user_name:
			doJump(AboutIdCardActivity.class);
			break;
		default:
			break;
		}
	}
	private void doJump(Class clazz){
		if(activity.getUser() == null){
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			getActivity().startActivityForResult(intent, AppConstant.LOGIN_CODE);
		}else{
			DoJumpUtils.doJump(getActivity(),clazz);
		}
	}
	

}
