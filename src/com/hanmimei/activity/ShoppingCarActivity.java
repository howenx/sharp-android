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
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
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
import com.hanmimei.manager.ShoppingCarMenager;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;

/*
 * 同shoppingcarfragment
 */
public class ShoppingCarActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener2<ListView> {

	private PullToRefreshListView mListView;
	private LinearLayout bottom;
	private TextView total_price;
	private TextView pay;
	private TextView attention;
	private LinearLayout no_data;
	private LinearLayout no_net;
	private TextView go_home;
	private List<Customs> data;
	private ShoppingCarPullListAdapter adapter;
	private User user;
	private ShoppingGoodsDao goodsDao;

	private ShoppingCar shoppingCar;
	private boolean isBack = false;
	private TextView reload;
	
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.shopping_car_list_layout);
		ActionBarUtil.setActionBarStyle(this, "购物车");
		registerReceivers();
		goodsDao = getDaoSession().getShoppingGoodsDao();
		shoppingCar = new ShoppingCar();
		data = new ArrayList<Customs>();
		findView();
		loadData();
	}

	private void loadData() {
		attention.setVisibility(View.INVISIBLE);
		mListView.setVisibility(View.GONE);
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
//			attention.setVisibility(View.VISIBLE);
//			mListView.setVisibility(View.VISIBLE);
//			no_data.setVisibility(View.GONE);
//			bottom.setVisibility(View.VISIBLE);
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
				object.put("skuType", goods.getSkuType());
				object.put("skuTypeId", goods.getSkuTypeId());
				array.put(object);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void getData() {
		getLoading().show();
		Http2Utils.doPostRequestTask2(this,null,UrlUtil.CAR_LIST_URL,new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				ShoppingCar car = DataParser.parserShoppingCar(result);
				afterLoadData(car);
			}
			
			@Override
			public void onError() {
				getLoading().dismiss();
				bottom.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				no_data.setVisibility(View.GONE);
				no_net.setVisibility(View.VISIBLE);
			}
		},array.toString());
	}

	private void afterLoadData(ShoppingCar car){
		if(isBack)
			return;
		getLoading().dismiss();
		mListView.onRefreshComplete();
		data.clear();
		if (car.getMessage() != null) {
			if (car.getList() != null && car.getList().size() > 0) {
				no_data.setVisibility(View.GONE);
				no_net.setVisibility(View.GONE);
				bottom.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.VISIBLE);
				attention.setVisibility(View.VISIBLE);
				data.addAll(car.getList());
				//
				ShoppingCarMenager.getInstance()
						.initShoppingCarMenager(ShoppingCarActivity.this, adapter,
								data, attention, 
								total_price, pay, no_data, bottom,mListView);
				clearPrice();
				//
			} else {
				bottom.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				if(car.getMessage().getCode() == 1015){
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
		adapter.notifyDataSetChanged();
	}
	private void getNetData() {
		getLoading().show();
		Http2Utils.doGetRequestTask(this, getHeaders(),UrlUtil.GET_CAR_LIST,new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				ShoppingCar car = DataParser.parserShoppingCar(result);
				afterLoadData(car);
			}
			
			@Override
			public void onError() {
				getLoading().dismiss();
				bottom.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				no_data.setVisibility(View.GONE);
				no_net.setVisibility(View.VISIBLE);
				attention.setVisibility(View.INVISIBLE);
			}
		});
	}

	private void findView() {
		bottom = (LinearLayout) findViewById(R.id.bottom);
		total_price = (TextView) findViewById(R.id.total_price);
		pay = (TextView) findViewById(R.id.pay);
		go_home = (TextView)findViewById(R.id.go_home);
		go_home.setOnClickListener(this);
		pay.setOnClickListener(this);
		attention = (TextView) findViewById(R.id.attention);
		mListView = (PullToRefreshListView) findViewById(R.id.mylist);
		mListView.setVisibility(View.GONE);
		mListView.setOnRefreshListener(this);
		mListView.setMode(Mode.PULL_DOWN_TO_REFRESH);
		no_data = (LinearLayout) findViewById(R.id.data_null);
		no_net = (LinearLayout) findViewById(R.id.no_net);
//		check_all.setOnClickListener(this);
		adapter = new ShoppingCarPullListAdapter(data, this);
		mListView.setAdapter(adapter);
		reload = (TextView) findViewById(R.id.reload);
		reload.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
					customs.setInvAreaNm(data.get(i).getInvAreaNm());
					customsList.add(customs);
				}
			}
			shoppingCar.setList(customsList);
			shoppingCar.setBuyNow(2);
			if (shoppingCar.getList().size() > 0) {
				user = getUser();
				if (user != null) {
					doPay(shoppingCar);
				} else {
					startActivity(new Intent(this, LoginActivity.class));
				}

			} else {
				ToastUtils.Toast(this, "请选择商品");
			}
			break;
		case R.id.go_home:
			startActivity(new Intent(this,MainActivity.class));
			break;
		case R.id.reload:
			loadData();
			break;
		default:
			break;
		}
	}

	private void doPay(ShoppingCar shoppingCar) {
		Intent intent = new Intent(getActivity(), GoodsBalanceActivity.class);
		intent.putExtra("car", shoppingCar);
		intent.putExtra("orderType", "item");
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
//			sendBroadcast(new Intent(
//					AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR));
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
		intentFilter
				.addAction(AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR);
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION);
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_QUIT_LOGIN_ACTION);
		registerReceiver(netReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netReceiver);
		isBack = true;
		if(isShoppingcarChanged()){
			sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR));
		}
	}

	private class CarBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR)) {
				loadData();
			} else if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)) {
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
	

}