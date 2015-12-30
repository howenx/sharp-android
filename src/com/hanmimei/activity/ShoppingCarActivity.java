package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.adapter.ShoppingCarPullListAdapter;
import com.hanmimei.dao.ShoppingGoodsDao;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.User;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ShoppingCarMenager;
import com.umeng.analytics.MobclickAgent;

/*
 * 同shoppingcarfragment
 */
public class ShoppingCarActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener2<ListView> {

	private PullToRefreshListView mListView;
	private LinearLayout bottom;
	private TextView total_price;
	private ImageView check_all;
	private TextView pay;
	private TextView attention;
	private LinearLayout no_data;
	private LinearLayout no_net;
	private List<Customs> data;
	private ShoppingCarPullListAdapter adapter;
	private User user;
	private ShoppingGoodsDao goodsDao;

	private Drawable check_Drawable;
	private Drawable uncheck_Drawable;

	private ShoppingCar shoppingCar;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.shopping_car_list_layout);
		registerReceivers();
		goodsDao = getDaoSession().getShoppingGoodsDao();
		shoppingCar = new ShoppingCar();
		data = new ArrayList<Customs>();
		check_Drawable = getResources().getDrawable(R.drawable.checked);
		uncheck_Drawable = getResources().getDrawable(R.drawable.check_un);
		findView();
		loadData();
	}

	private void loadData() {
		user = getUser();
		if (user != null) {
			getNetData();
		} else {
			getLocalData();
		}
	}

	private void getLocalData() {
		List<ShoppingGoods> list = goodsDao.queryBuilder().build().list();
		if (list != null && list.size() > 0) {
			mListView.setVisibility(View.VISIBLE);
			no_data.setVisibility(View.GONE);
			bottom.setVisibility(View.VISIBLE);
			toJsonArray(list);
			getData();
		} else {
			bottom.setVisibility(View.GONE);
			no_data.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
	}

	private JSONArray array;

	private void toJsonArray(List<ShoppingGoods> list) {
		try {
			array = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				ShoppingGoods goods = list.get(i);
				JSONObject object = new JSONObject();
				object.put("cartId", 0);
				object.put("skuId", goods.getGoodsId());
				object.put("amount", goods.getGoodsNums());
				object.put("state", goods.getState());
				array.put(object);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void getData() {
		getLoading().show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.post(UrlUtil.CAR_LIST_URL,
						array, "null", "");
				ShoppingCar car = DataParser.parserShoppingCar(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = car;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private void getNetData() {
		getLoading().show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = HttpUtils.post(UrlUtil.GET_CAR_LIST_URL,
						new JSONObject(), "id-token", user.getToken());
				ShoppingCar car = DataParser.parserShoppingCar(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = car;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				getLoading().dismiss();
				mListView.onRefreshComplete();
				ShoppingCar car = new ShoppingCar();
				car = (ShoppingCar) msg.obj;
				if (car.getMessage() != null) {
					if (car.getList() != null && car.getList().size() > 0) {
						no_data.setVisibility(View.GONE);
						no_net.setVisibility(View.GONE);
						bottom.setVisibility(View.VISIBLE);
						mListView.setVisibility(View.VISIBLE);
						data.clear();
						data.addAll(car.getList());
						//
						ShoppingCarMenager.getInstance()
								.initShoppingCarMenager(ShoppingCarActivity.this, adapter,
										data, false, attention, check_all,
										total_price, pay, no_data, bottom);
						ShoppingCarMenager.getInstance().initDrawable(
								getActivity());
						clearPrice();
						//
						adapter.notifyDataSetChanged();
					} else {
						bottom.setVisibility(View.GONE);
						mListView.setVisibility(View.GONE);
						if(car.getMessage().getCode() == 1010){
							no_data.setVisibility(View.VISIBLE);
							no_net.setVisibility(View.GONE);
						}else{
							no_data.setVisibility(View.GONE);
							no_net.setVisibility(View.VISIBLE);
						}
					}
				} else {
					bottom.setVisibility(View.GONE);
					mListView.setVisibility(View.GONE);
					no_data.setVisibility(View.GONE);
					no_net.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
		}
	};

	private void findView() {
		TextView title = (TextView) findViewById(R.id.header);
		title.setText("购物车");
		bottom = (LinearLayout) findViewById(R.id.bottom);
		total_price = (TextView) findViewById(R.id.total_price);
		check_all = (ImageView) findViewById(R.id.all);
		if (ShoppingCarMenager.getInstance().getChecked()) {
			check_all.setImageDrawable(check_Drawable);
		} else {
			check_all.setImageDrawable(uncheck_Drawable);
		}
		pay = (TextView) findViewById(R.id.pay);
		pay.setOnClickListener(this);
		attention = (TextView) findViewById(R.id.attention);
		mListView = (PullToRefreshListView) findViewById(R.id.mylist);
		no_data = (LinearLayout) findViewById(R.id.data_null);
		no_net = (LinearLayout) findViewById(R.id.no_net);
		findViewById(R.id.top).setVisibility(View.VISIBLE);
		findViewById(R.id.back).setVisibility(View.VISIBLE);
		findViewById(R.id.back).setOnClickListener(this);
		check_all.setOnClickListener(this);
		adapter = new ShoppingCarPullListAdapter(data, this);
		mListView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.all:
			if (ShoppingCarMenager.getInstance().getChecked()) {
				check_all.setImageDrawable(uncheck_Drawable);
				clearPrice();
			} else {
				check_all.setImageDrawable(check_Drawable);
				doPrice();
			}
			break;
		case R.id.back:
			// 发广播 通知shoppingfragment数据发生改变
			sendBroadcast(new Intent(
					AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR));
			finish();
			break;
		case R.id.pay:
			List<Customs> customsList = new ArrayList<Customs>();
			for (int i = 0; i < data.size(); i++) {
				List<ShoppingGoods> goodsList = new ArrayList<ShoppingGoods>();
				Customs customs = new Customs();
				for (int j = 0; j < data.get(i).getList().size(); j++) {
					if (data.get(i).getList().get(j).getState().equals("G")) {
						goodsList.add(data.get(i).getList().get(j));
					}
				}
				if (goodsList.size() > 0) {
					customs.setList(goodsList);
					customs.setInvArea(data.get(i).getInvArea());
					customs.setInvCustoms(data.get(i).getInvCustoms());
					customsList.add(customs);
				}
			}
			shoppingCar.setList(customsList);
			user = getUser();
			if (shoppingCar.getList().size() > 0) {
				if (user != null) {
					doPay(shoppingCar);
				} else {
					startActivity(new Intent(this, LoginActivity.class));
				}

			} else {
				Toast.makeText(getActivity(), "请选择商品", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		default:
			break;
		}
	}

	private void doPay(ShoppingCar shoppingCar) {
		Intent intent = new Intent(getActivity(), GoodsBalanceActivity.class);
		intent.putExtra("car", shoppingCar);
		getActivity().startActivity(intent);
	}

	private void doPrice() {
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < data.get(i).getList().size(); j++) {
				if (!data.get(i).getList().get(j).getState().equals("S"))
					data.get(i).getList().get(j).setState("G");
			}
		}
		adapter.notifyDataSetChanged();
		ShoppingCarMenager.getInstance().setBottom();
	}

	private void clearPrice() {
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < data.get(i).getList().size(); j++) {
				if (!data.get(i).getList().get(j).getState().equals("S"))
					data.get(i).getList().get(j).setState("I");
			}
		}
		adapter.notifyDataSetChanged();
		ShoppingCarMenager.getInstance().setBottom();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {

		super.onActivityResult(requestCode, resultCode, arg2);
		loadData();
	}

	// 主界面返回之后在后台运行
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 发广播 通知shoppingfragment数据发生改变
			sendBroadcast(new Intent(
					AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR));
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private CarBroadCastReceiver netReceiver;

	// 广播接收者 注册
	private void registerReceivers() {
		netReceiver = new CarBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_ADD_CAR);
//		intentFilter
//				.addAction(AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR);
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

	private class CarBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction()
					.equals(AppConstant.MESSAGE_BROADCAST_ADD_CAR)) {
				loadData();
			} 
//			else if (intent.getAction().equals(
//					AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR)) {
//				loadData();
//			} 
			else if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)) {
//				clearPrice();
				loadData();
			} else if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_QUIT_LOGIN_ACTION)) {
				loadData();
			}
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//		clearPrice();
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//		clearPrice();
		loadData();
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("ShoppingCarActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
	    MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("ShoppingCarActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
	    MobclickAgent.onPause(this);
	}

}
