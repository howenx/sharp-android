package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.activity.listener.TimeEndListner;
import com.hanmimei.application.HMMApplication;
import com.hanmimei.data.AppConstant;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.PinActivity;
import com.hanmimei.entity.PinResult;
import com.hanmimei.entity.PinUser;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ImageLoaderUtils;
import com.hanmimei.utils.KeyWordUtil;
import com.hanmimei.utils.PopupWindowUtil;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.RoundImageView;
import com.hanmimei.view.TimeDownView;

public class PingouResultActivity extends BaseActivity implements
		TimeEndListner, OnClickListener {

	private TimeDownView timer;
	private ListView mListView;
	private GridView gridlayout;
	private PinResult pinResult;
	private TextView pro_title, tuan_guige, master_name, master_time,
			btn_xiadan, about;
	private ImageView tuan_status, pro_img, tuan_state, master_face;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "组团详情");
		setContentView(R.layout.pingou_result_layout);

		findView();
		loadPinUrl();
		registerReceivers();
	}

	private void findView() {
		tuan_status = (ImageView) findViewById(R.id.tuan_status);
		pro_img = (ImageView) findViewById(R.id.imageView1);
		pro_title = (TextView) findViewById(R.id.textView1);
		tuan_guige = (TextView) findViewById(R.id.tuan_guige);
		tuan_state = (ImageView) findViewById(R.id.tuan_state);
		master_face = (ImageView) findViewById(R.id.master_face);
		master_name = (TextView) findViewById(R.id.master_name);
		master_time = (TextView) findViewById(R.id.master_time);
		btn_xiadan = (TextView) findViewById(R.id.btn_xiadan);
		about = (TextView) findViewById(R.id.about);

		timer = (TimeDownView) findViewById(R.id.timer);
		mListView = (ListView) findViewById(R.id.mListView);
		gridlayout = (GridView) findViewById(R.id.gridlayout);

	}

	private void loadPinUrl() {
		getLoading().show();
		Http2Utils.doGetRequestTask(this, getHeaders(), getIntent()
				.getStringExtra("url"), new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				try {
					pinResult = new Gson().fromJson(result, PinResult.class);
					if (pinResult.getMessage().getCode() == 200) {
						pinActivity = pinResult.getActivity();
						initPageData();
					} else {
						ToastUtils.Toast(getActivity(), pinResult.getMessage()
								.getMessage());
					}
				} catch (Exception e) {
					ToastUtils.Toast(getActivity(), R.string.error);
				}
				getLoading().dismiss();
			}

			@Override
			public void onError() {
				getLoading().dismiss();
				ToastUtils.Toast(getActivity(), R.string.error);
			}
		});
	}

	private void initPageData() {

		if (pinActivity.getStatus().equals("Y")) {
			tuan_state.setImageResource(R.drawable.hmm_zutuan);
			if (pinActivity.getPay().equals("new")) {
				if (pinActivity.getUserType().equals("master")) {
					tuan_status.setImageResource(R.drawable.hmm_kaituan_success);
				} else {
					tuan_status.setImageResource(R.drawable.hmm_cantuan_success);
				}
				
				btn_xiadan.setText("还差"+ (pinActivity.getPersonNum() - pinActivity
								.getJoinPersons()) + "人，点击复制去分享！");
				btn_xiadan.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						initPop();
//						doCopy();
					}
				});
			} else {
				tuan_status.setVisibility(View.GONE);
				if (pinActivity.getOrJoinActivity() == 1) {
					btn_xiadan.setText("还差"
							+ (pinActivity.getPersonNum() - pinActivity
									.getJoinPersons()) + "人，让小伙伴们都来组团吧！");
					btn_xiadan.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							doCopy();
						}
					});
				} else {
					btn_xiadan.setText("立即下单");
					btn_xiadan.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							goToGoodsBalance(pinActivity);
						}
					});
				}
			}
			about.setText("还差"
					+ (pinActivity.getPersonNum() - pinActivity
							.getJoinPersons()) + "人，让小伙伴们都来组团吧！");
			initTimer();
		} else if (pinActivity.getStatus().equals("F")) {
			tuan_status.setImageResource(R.drawable.hmm_pingou_fail);
			tuan_state.setImageResource(R.drawable.hmm_zutuan_fail);
			about.setVisibility(View.GONE);
			btn_xiadan.setVisibility(View.GONE);
			findViewById(R.id.jishiView).setVisibility(View.GONE);
			
			findViewById(R.id.xiadanView).setVisibility(View.INVISIBLE);

		} else if (pinActivity.getStatus().equals("C")) { 
			tuan_status.setImageResource(R.drawable.hmm_pingou_success);
			tuan_state.setImageResource(R.drawable.hmm_zutuan_success);
			about.setText("对于诸位大侠的相助，团长感激涕零");
			findViewById(R.id.jishiView).setVisibility(View.GONE);

			findViewById(R.id.xiadanView).setVisibility(View.INVISIBLE);
		}

		ImageLoaderUtils.loadImage(pinActivity.getPinImg().getUrl(), pro_img);
		pro_title.setText(pinActivity.getPinTitle() + "");
		String guige = getResources().getString(R.string.tuan_gui,
				pinActivity.getPersonNum(), pinActivity.getPinPrice());
		KeyWordUtil.setDifferentFontColor13(this, tuan_guige, guige,
				guige.indexOf("¥"), guige.length());
		// tuan_guige.setText(getResources().getString(R.string.tuan_gui,
		// pinActivity.getPersonNum(), pinActivity.getPinPrice()));

		PinUser master = pinActivity.getPinUsersForMaster();
		ImageLoaderUtils.loadImage(master.getUserImg(), master_face);
		master_name.setText("团长" + master.getUserNm());
		master_time.setText(master.getJoinAt() + "开团");
		gridlayout
				.setAdapter(new PinTuanGridAdapter(pinActivity.getPinUsers()));
		mListView.setAdapter(new PinTuanListAdapter(pinActivity
				.getPinUsersForMember()));

		findViewById(R.id.btn_see_goods).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								PingouDetailActivity.class);
						intent.putExtra("url", pinActivity.getPinSkuUrl());
						startActivity(intent);
					}
				});

	}
	
	private void initTimer(){
		if (pinActivity.getEndCountDown() > 0) {
			int[] time = { pinActivity.getEndCountDownForDay(),
					pinActivity.getEndCountDownForHour(),
					pinActivity.getEndCountDownForMinute(),
					pinActivity.getEndCountDownForSecond() };
			timer.setTimes(time);
			timer.setTimeEndListner(this);
			timer.run();
		}
	}
	
	
	private PopupWindow shareWindow;
	private void initPop() {
		View view = LayoutInflater.from(this).inflate(R.layout.share_layout,
				null);
		shareWindow = PopupWindowUtil.showPopWindow(this, view);
//		view.findViewById(R.id.qq).setOnClickListener(this);
//		view.findViewById(R.id.weixin).setOnClickListener(this);
//		view.findViewById(R.id.weixinq).setOnClickListener(this);
//		view.findViewById(R.id.sina).setOnClickListener(this);
		view.findViewById(R.id.qq).setVisibility(View.GONE);
		view.findViewById(R.id.weixin).setVisibility(View.GONE);
		view.findViewById(R.id.weixinq).setVisibility(View.GONE);
		view.findViewById(R.id.sina).setVisibility(View.GONE);
		view.findViewById(R.id.copy).setOnClickListener(this);
	}

	private PinActivity pinActivity;

	private void doCopy() {
		String code[] = pinActivity.getPinUrl().split("activity/");
		HMMApplication application = (HMMApplication) getApplication();
		application.setKouling("KAKAO-HMM 复制这条信息,打开👉韩秘美👈即可看到<T>【"
				+ pinActivity.getPinTitle() + "】," + code[1] + ",－🔑 M令 🔑");
		ToastUtils.Toast(this, "复制成功，赶快去粘贴吧！");
	}

	@Override
	public void isTimeEnd() {
		findViewById(R.id.xiadanView).setVisibility(View.INVISIBLE);
	}

	private class PinTuanListAdapter extends BaseAdapter {

		private List<PinUser> members;

		public PinTuanListAdapter(List<PinUser> members) {
			this.members = members;
		}

		@Override
		public int getCount() {
			return members.size();
		}

		@Override
		public Object getItem(int arg0) {
			return members.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder holder = null;
			if (arg1 == null) {
				arg1 = getLayoutInflater().inflate(
						R.layout.pingou_result_list_item_layout, null);
				holder = new ViewHolder(arg1);

				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}

			PinUser p = members.get(arg0);
			holder.nameView.setText(p.getUserNm());
			ImageLoaderUtils.loadImage(p.getUserImg(), holder.faceView);
			holder.timeView.setText(p.getJoinAt() + "参团");

			return arg1;
		}

		private class ViewHolder {
			TextView nameView, timeView;
			ImageView faceView;

			public ViewHolder(View view) {
				super();
				this.nameView = (TextView) view.findViewById(R.id.nameView);
				this.timeView = (TextView) view.findViewById(R.id.timeView);
				this.faceView = (ImageView) view.findViewById(R.id.faceView);
			}
		}
	}

	private void goToGoodsBalance(PinActivity s) {
		if (getUser() == null) {
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		ShoppingCar car = new ShoppingCar();
		List<Customs> list = new ArrayList<Customs>();
		Customs customs = new Customs();
		ShoppingGoods sgoods;
		if (s.getStatus().equals("Y")) {
			sgoods = new ShoppingGoods();
			sgoods.setGoodsId(s.getSkuId().toString());
			sgoods.setGoodsImg(s.getPinImg().getUrl());
			sgoods.setGoodsName(s.getPinTitle());
			sgoods.setGoodsNums(1);
			sgoods.setGoodsPrice(Double.valueOf(s.getPinPrice()));
			sgoods.setPinTieredPriceId(s.getPinTieredPriceId());
			if (sgoods.getGoodsPrice() == null) {
				ToastUtils.Toast(this, "请选择商品");
				return;
			}
			sgoods.setInvArea(s.getInvArea());
			sgoods.setInvAreaNm(s.getInvAreaNm());
			sgoods.setInvCustoms(s.getInvCustoms());
			sgoods.setPostalTaxRate(Integer.valueOf(s.getPostalTaxRate()));
			sgoods.setPostalStandard(Integer.valueOf(s.getPostalStandard()));
			sgoods.setSkuType(s.getSkuType());
			sgoods.setSkuTypeId(s.getSkuTypeId());
		} else if (s.getStatus().equals("P")) {
			ToastUtils.Toast(this, "尚未开售");
			return;
		} else {
			ToastUtils.Toast(this, "活动已结束");
			return;
		}
		customs.addShoppingGoods(sgoods);
		customs.setInvArea(sgoods.getInvArea());
		customs.setInvAreaNm(sgoods.getInvAreaNm());
		customs.setInvCustoms(sgoods.getInvCustoms());
		list.add(customs);
		car.setList(list);
		Intent intent = new Intent(this, GoodsBalanceActivity.class);
		intent.putExtra("car", car);
		intent.putExtra("orderType", "pin");
		intent.putExtra("pinActiveId", s.getPinActiveId() + "");
		startActivity(intent);
		finish();
	}

	private class PinTuanGridAdapter extends BaseAdapter {

		private List<PinUser> members;

		public PinTuanGridAdapter(List<PinUser> members) {
			this.members = members;
		}

		@Override
		public int getCount() {
			return pinResult.getActivity().getPersonNum();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder holder = null;
			if (arg1 == null) {
				arg1 = getLayoutInflater().inflate(
						R.layout.pingou_result_grid_item_layout, null);
				holder = new ViewHolder(arg1);

				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			if (arg0 < members.size()) {
				if (members.get(arg0).isOrMaster()) {
					holder.roleView.setVisibility(View.VISIBLE);
				} else {
					holder.roleView.setVisibility(View.INVISIBLE);
				}
				ImageLoaderUtils.loadImage(members.get(arg0).getUserImg(),
						holder.faceView);
				holder.faceView.setBorderColor(getResources().getColor(
						R.color.theme));
			} else {
				holder.faceView.setBorderColor(getResources().getColor(
						R.color.white));
			}

			return arg1;
		}

		private class ViewHolder {
			TextView roleView;
			RoundImageView faceView;

			public ViewHolder(View view) {
				super();
				this.roleView = (TextView) view.findViewById(R.id.roleView);
				this.faceView = (RoundImageView) view
						.findViewById(R.id.faceView);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netReceiver);
	}

	private CarBroadCastReceiver netReceiver;

	// 广播接收者 注册
	private void registerReceivers() {
		netReceiver = new CarBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR);
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION);
		getActivity().registerReceiver(netReceiver, intentFilter);
	}

	private class CarBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)) {
				loadPinUrl();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.copy:
			doCopy();
			shareWindow.dismiss();
			break;

		default:
			break;
		}
	}

}
