package com.kakao.kakaogift.activity.car;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.balance.GoodsBalanceActivity;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.car.adapter.ShoppingCarPullListAdapter;
import com.kakao.kakaogift.activity.login.LoginActivity;
import com.kakao.kakaogift.activity.main.HMainActivity;
import com.kakao.kakaogift.dao.ShoppingGoodsDao;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.CustomsVo;
import com.kakao.kakaogift.entity.ShoppingCar;
import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.entity.User;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.manager.ShoppingCarMenager;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.DataNoneLayout;

/*
 * 同shoppingcarfragment
 */
/**
 * @author eric
 * 购物车的activity
 *
 */
public class ShoppingCarActivity extends BaseActivity implements
		OnClickListener{

	private PullToRefreshListView mListView;
	private LinearLayout bottom;
	private TextView total_price;
	private TextView pay;
	private TextView attention;
	private LinearLayout no_net;
	private List<CustomsVo> data;
	private ShoppingCarPullListAdapter adapter;
	private User user;
	private ShoppingGoodsDao goodsDao;

	private ShoppingCar shoppingCar;
	private boolean isBack = false;
	private TextView reload;
	private RelativeLayout shopping_main;
	private DataNoneLayout dataNoneLayout;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.shopping_car_list_layout);
		ActionBarUtil.setActionBarStyle(this, "购物车");
		findViewById(R.id.actionbarView).setVisibility(View.GONE);
		registerReceivers();
		goodsDao = getDaoSession().getShoppingGoodsDao();
		shoppingCar = new ShoppingCar();
		data = new ArrayList<CustomsVo>();
		findView();
		loadData();
		
	}
	/**
	 * 加载购物车数据，根据登陆状态的不同，操作不同
	 */
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
	//为登陆状态下，加载本地购物车数据
	private void getLocalData() {
		List<ShoppingGoods> list = goodsDao.queryBuilder().build().list();
		if (list != null && list.size() > 0) {
			toJsonArray(list);
			getData();
		} else {//购物车本地数据暂无
			bottom.setVisibility(View.GONE);
			setDataNone();
			mListView.setVisibility(View.GONE);
		}
	}

	/**
	 * 
	 */
	private void setDataNone() {
		dataNoneLayout.loadData(1);
		dataNoneLayout.setVisible();
	}

	private JSONArray array;
	//将购物车的list列表数据转化成jsonarray，用与提交数据至后台服务器
	private void toJsonArray(List<ShoppingGoods> list) {
		try {
			array = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				ShoppingGoods goods = list.get(i);
				JSONObject object = new JSONObject();
				object.put("cartId", 0);	//未登录状态，cartid默认是0
				object.put("skuId", goods.getGoodsId());
				object.put("amount", goods.getGoodsNums());
				object.put("state", goods.getState());
				object.put("skuType", goods.getSkuType());
				object.put("skuTypeId", goods.getSkuTypeId());
				object.put("pinTieredPriceId", null);
				object.put("orCheck", goods.getOrCheck());
				object.put("cartSource", 1);
				array.put(object);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	//未登陆状态下，将本地购物车数据提交服务器进行校验
	private void getData() {
		//loading图show
		getLoading().show();
		VolleyHttp.doPostRequestTask2(null,UrlUtil.CAR_LIST_URL,new VolleyJsonCallback() {
			
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
				if(dataNoneLayout != null)
					dataNoneLayout.setNoVisible();
				no_net.setVisibility(View.VISIBLE);
			}
		},array.toString());
	}
	/**
	 * @param car
	 * 请求数据完成时候的操作
	 * 
	 */
	private void afterLoadData(ShoppingCar car){
		if(isBack)
			return;
		getLoading().dismiss();
		mListView.onRefreshComplete();
		data.clear();
		if (car.getMessage() != null) {
			if (car.getList() != null && car.getList().size() > 0) {
				if(dataNoneLayout != null)
					dataNoneLayout.setNoVisible();
				no_net.setVisibility(View.GONE);
				bottom.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.VISIBLE);
				attention.setVisibility(View.VISIBLE);
				data.addAll(car.getList());
				//初始化购物车管理类
				ShoppingCarMenager.getInstance()
						.initShoppingCarMenager(ShoppingCarActivity.this, adapter,
								data, attention, 
								total_price, pay,  bottom,mListView, dataNoneLayout);
				clearPrice();
				//
			} else {
				bottom.setVisibility(View.GONE);
				mListView.setVisibility(View.GONE);
				if(car.getMessage().getCode() == 1015){
					setDataNone();
					no_net.setVisibility(View.GONE);
				}else{
					if(dataNoneLayout != null)
						dataNoneLayout.setNoVisible();
					no_net.setVisibility(View.VISIBLE);
				}
			}
		} else {
			bottom.setVisibility(View.GONE);
			mListView.setVisibility(View.GONE);
			if(dataNoneLayout != null)
				dataNoneLayout.setNoVisible();
			no_net.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
	}
	/**
	 * 登陆状态下，向服务器请求购物车数据
	 */
	private void getNetData() {
		getLoading().show();
		VolleyHttp.doGetRequestTask( getHeaders(),UrlUtil.GET_CAR_LIST,new VolleyJsonCallback() {
			
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
				if(dataNoneLayout != null)
					dataNoneLayout.setNoVisible();
				no_net.setVisibility(View.VISIBLE);
				attention.setVisibility(View.INVISIBLE);
			}
		});
	}
	/**
	 * 初始化页面的控件
	 */
	private void findView() {
		bottom = (LinearLayout) findViewById(R.id.bottom);
		total_price = (TextView) findViewById(R.id.total_price);
		pay = (TextView) findViewById(R.id.pay);
		shopping_main = (RelativeLayout) findViewById(R.id.shopping_main);
		pay.setOnClickListener(this);
		attention = (TextView) findViewById(R.id.attention);
		mListView = (PullToRefreshListView) findViewById(R.id.mylist);
		mListView.setVisibility(View.GONE);
		mListView.setMode(Mode.DISABLED);
		no_net = (LinearLayout) findViewById(R.id.no_net);
		adapter = new ShoppingCarPullListAdapter(data, this);
		mListView.setAdapter(adapter);
		reload = (TextView) findViewById(R.id.reload);
		shopping_main = (RelativeLayout) findViewById(R.id.shopping_main);
		reload.setOnClickListener(this);
		
		dataNoneLayout = new DataNoneLayout(getActivity(), shopping_main);
		dataNoneLayout.setNullImage(R.drawable.icon_gouwuche_none);
		dataNoneLayout.setText("您的购物车是空的");
		dataNoneLayout.setMode(Mode.DISABLED);
		dataNoneLayout.setGoHome();
		dataNoneLayout.setNoVisible();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay:
			//组织结算界面需要的数据，用于结算的跳转传递参数
			List<CustomsVo> customsList = new ArrayList<CustomsVo>();
			for (int i = 0; i < data.size(); i++) {
				List<ShoppingGoods> goodsList = new ArrayList<ShoppingGoods>();
				CustomsVo customs = new CustomsVo();
				for (int j = 0; j < data.get(i).getList().size(); j++) {
					if (data.get(i).getList().get(j).getOrCheck().equals("Y")) {
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
		case R.id.reload:
			loadData();
			break;
		default:
			break;
		}
	}
	/**
	 * 点击去结算
	 * @param shoppingCar
	 */
	private void doPay(ShoppingCar shoppingCar) {
		Intent intent = new Intent(getActivity(), GoodsBalanceActivity.class);
		intent.putExtra("car", shoppingCar);
		intent.putExtra("orderType", "item");
		getActivity().startActivity(intent);
	}
	
//	@SuppressWarnings("unused")
//	@Deprecated
//	private void doPrice() {
//		for (int i = 0; i < data.size(); i++) {
//			for (int j = 0; j < data.get(i).getList().size(); j++) {
//				if (!data.get(i).getList().get(j).getState().equals("S"))
//					data.get(i).getList().get(j).setState("G");
//			}
//		}
//		adapter.notifyDataSetChanged();
//		ShoppingCarMenager.getInstance().setBottom();
//	}
	
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



	private CarBroadCastReceiver netReceiver;

	// 广播接收者的注册
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
		//如果购车车发生改变，发送广播通知首页的购物车数据发生改变
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

	

}