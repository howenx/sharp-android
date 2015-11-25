package com.hanbimei.fragment;

import com.hanbimei.R;
import com.hanbimei.activity.AboutIdCardActivity;
import com.hanbimei.activity.AdressActivity;
import com.hanbimei.activity.CouponActivity;
import com.hanbimei.activity.IdCardActivity;
import com.hanbimei.activity.MyOrderActivity;
import com.hanbimei.utils.DoJumpUtils;
import com.hanbimei.view.RoundImageView;

import android.content.Context;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initIcon();
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
		return view;
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
		shenfenzheng.setCompoundDrawables(shenfen_icon, null, jiantou_icon, null);
		youhui = (TextView) view.findViewById(R.id.youhui);
		youhui.setCompoundDrawables(youhui_icon, null, jiantou_icon, null);
		about = (TextView) view.findViewById(R.id.about_card);
		about.setCompoundDrawables(about_icon, null, jiantou_icon, null);
		header.setOnClickListener(this);
		is_authenticate.setOnClickListener(this);
		order.setOnClickListener(this);
		youhui.setOnClickListener(this);
		shenfenzheng.setOnClickListener(this);
		address.setOnClickListener(this);
		about.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header:
			
			break;
		case R.id.order:
			DoJumpUtils.doJump(getActivity(),MyOrderActivity.class);
			break;
		case R.id.youhui:
			DoJumpUtils.doJump(getActivity(),CouponActivity.class);
			break;
		case R.id.shenfenzheng:
			DoJumpUtils.doJump(getActivity(),IdCardActivity.class);
			break;
		case R.id.address:
			DoJumpUtils.doJump(getActivity(),AdressActivity.class);
			break;
		case R.id.do_authenticate:
			DoJumpUtils.doJump(getActivity(),IdCardActivity.class);
			break;
		case R.id.about_card:
			DoJumpUtils.doJump(getActivity(),AboutIdCardActivity.class);
			break;
		default:
			break;
		}
	}
	

}
