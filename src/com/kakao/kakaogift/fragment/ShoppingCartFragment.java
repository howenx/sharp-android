package com.kakao.kakaogift.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.balance.GoodsBalanceActivity;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.car.adapter.ShoppingCarPullListAdapter;
import com.kakao.kakaogift.activity.login.LoginActivity;
import com.kakao.kakaogift.dao.ShoppingGoodsDao;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.CustomsVo;
import com.kakao.kakaogift.entity.ShoppingCar;
import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.entity.User;
import com.kakao.kakaogift.event.ShoppingCarEvent;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.manager.BadgeViewManager;
import com.kakao.kakaogift.manager.ShoppingCarMenager;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.DataNoneLayout;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.BaseIconFragment;
import com.ypy.eventbus.EventBus;

/**
 * @author eric
 * 
 */
public class ShoppingCartFragment extends BaseIconFragment implements
		OnClickListener, OnRefreshListener2<ListView> {

	private PullToRefreshListView mListView;
	private LinearLayout bottom;
	private TextView total_price;
	private TextView pay;
	private TextView attention;
	private List<CustomsVo> data;
	private ShoppingCarPullListAdapter adapter;
	private BaseActivity activity;
	private User user;
	private ShoppingGoodsDao goodsDao;
	private LinearLayout no_net;
	private TextView reload;
	private RelativeLayout shopping_main;
	
	private BadgeViewManager mBadgeViewManager;
	private ShoppingCarMenager mShoppingCarMenager;
	

	public void setBadgeViewManager(BadgeViewManager mBadgeViewManager) {
		this.mBadgeViewManager = mBadgeViewManager;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		goodsDao = activity.getDaoSession().getShoppingGoodsDao();
		data = new ArrayList<CustomsVo>();
		shoppingCar = new ShoppingCar();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		activity = (BaseActivity) context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.shopping_car_list_layout, null);
		ActionBarUtil.initMainActionBarStyle(activity,view, 3);
		findView(view);
		registerReceivers();
		adapter = new ShoppingCarPullListAdapter(data, getActivity());
		mListView.setAdapter(adapter);
		loadData();
		EventBus.getDefault().register(this);
		return view;
	}

	private void loadData() {
		user = activity.getUser();
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
			bottom.setVisibility(View.VISIBLE);
			toJsonArray(list);
			getData();
		} else {
			mBadgeViewManager.setShopCartGoodsNum(0);
			attention.setVisibility(View.INVISIBLE);
			bottom.setVisibility(View.GONE);
			setDataNone();
			mListView.setVisibility(View.GONE);
			no_net.setVisibility(View.GONE);
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
				object.put("pinTieredPriceId", null);
				object.put("orCheck", goods.getOrCheck());
				object.put("cartSource", 1);
				array.put(object);
				
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void getData() {
		activity.getLoading().show();
		VolleyHttp.doPostRequestTask2( UrlUtil.CAR_LIST_URL,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						ShoppingCar car = DataParser.parserShoppingCar(result);
						afterLoadData(car);
					}

					@Override
					public void onError() {
						activity.getLoading().dismiss();
						attention.setVisibility(View.INVISIBLE);
						bottom.setVisibility(View.GONE);
						mListView.setVisibility(View.GONE);
						no_net.setVisibility(View.VISIBLE);
					}
				}, array.toString());
	}

	private int getShoppingCarNum(List<CustomsVo> list) {
		int nums = 0;
		for (int i = 0; i < list.size(); i++) {
			List<ShoppingGoods> goods = list.get(i).getList();
			for (int j = 0; j < goods.size(); j++) {
				nums = nums + goods.get(j).getGoodsNums();
			}
		}
		return nums;
	}

	private void afterLoadData(ShoppingCar car) {
		activity.getLoading().dismiss();
		mListView.onRefreshComplete();
		data.clear();
		mBadgeViewManager.setShopCartGoodsNum(0);
		if (car.getMessage() != null) {
			if (car.getList() != null && car.getList().size() > 0) {

				mBadgeViewManager.setShopCartGoodsNum(
						getShoppingCarNum(car.getList()));
				attention.setVisibility(View.VISIBLE);
				no_net.setVisibility(View.GONE);
				if(dataNoneLayout != null)
					dataNoneLayout.setNoVisible();
				bottom.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.VISIBLE);
				data.addAll(car.getList());
				//
				mShoppingCarMenager = new ShoppingCarMenager();
				mShoppingCarMenager.initShoppingCarMenager(
						activity, adapter, data, attention, total_price, pay, bottom, mListView, dataNoneLayout);
				clearPrice();
			} else {
				attention.setVisibility(View.INVISIBLE);
				bottom.setVisibility(View.GONE);
				if (car.getMessage().getCode() == 1015) {
					setDataNone();
					no_net.setVisibility(View.GONE);
				} else {
					if(dataNoneLayout != null)
						dataNoneLayout.setNoVisible();
					no_net.setVisibility(View.VISIBLE);
				}
			}
		} else {
			if(dataNoneLayout != null)
				dataNoneLayout.setNoVisible();
			attention.setVisibility(View.INVISIBLE);
			bottom.setVisibility(View.GONE);
			mListView.setVisibility(View.GONE);
			no_net.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
	}

	private DataNoneLayout dataNoneLayout;
	private void setDataNone() {
		dataNoneLayout.loadData(1);
		dataNoneLayout.setVisible();
	}
	
	private void getNetData() {
		activity.getLoading().show();
		VolleyHttp.doGetRequestTask( activity.getHeaders(),
				UrlUtil.GET_CAR_LIST, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						ShoppingCar car = DataParser.parserShoppingCar(result);
						afterLoadData(car);
					}

					@Override
					public void onError() {
						if(dataNoneLayout != null)
							dataNoneLayout.setNoVisible();
						activity.getLoading().dismiss();
						attention.setVisibility(View.INVISIBLE);
						bottom.setVisibility(View.GONE);
						mListView.setVisibility(View.GONE);
						no_net.setVisibility(View.VISIBLE);
					}
				});
	}

	private void findView(View view) {
		bottom = (LinearLayout) view.findViewById(R.id.bottom);
		total_price = (TextView) view.findViewById(R.id.total_price);
		pay = (TextView) view.findViewById(R.id.pay);
		attention = (TextView) view.findViewById(R.id.attention);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
		mListView.setOnRefreshListener(this);
		mListView.setMode(Mode.PULL_DOWN_TO_REFRESH);
		no_net = (LinearLayout) view.findViewById(R.id.no_net);
		reload = (TextView) view.findViewById(R.id.reload);
		shopping_main = (RelativeLayout) view.findViewById(R.id.shopping_main);
		reload.setOnClickListener(this);
		pay.setOnClickListener(this);
		
		dataNoneLayout = new DataNoneLayout(getActivity(), shopping_main);
		dataNoneLayout.setNullImage(R.drawable.icon_gouwuche_none);
		dataNoneLayout.setText("您的购物车是空的");
		dataNoneLayout.setMode(Mode.DISABLED);
		dataNoneLayout.setNoVisible();
		
	}

	

	private void clearPrice() {
//		for (int i = 0; i < data.size(); i++) {
//			for (int j = 0; j < data.get(i).getList().size(); j++) {
//				if (!data.get(i).getList().get(j).getState().equals("S"))
//					data.get(i).getList().get(j).setState("I");
//			}
//		}
		adapter.notifyDataSetChanged();
		mShoppingCarMenager.setBottom();
	}

	private ShoppingCar shoppingCar;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay:
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
				user = activity.getUser();
				if (user != null) {
					doPay(shoppingCar);
				} else {
					startActivity(new Intent(activity, LoginActivity.class));
				}

			} else {
				ToastUtils.Toast(getActivity(), "请选择商品");
			}
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

	private CarBroadCastReceiver netReceiver;

	// 广播接收者 注册
	private void registerReceivers() {
		netReceiver = new CarBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR);
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION);
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_QUIT_LOGIN_ACTION);
		getActivity().registerReceiver(netReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		getActivity().unregisterReceiver(netReceiver);
	}

	private class CarBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR)) {
				loadData();
			} else if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)) {
				// clearPrice();
				loadData();
			} else if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_QUIT_LOGIN_ACTION)) {
				loadData();
			}
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// clearPrice();
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// clearPrice();
		loadData();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ShoppingCartFragment"); // 统计页面，"MainScreen"为页面名称，可自定义
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ShoppingCartFragment");
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return " 购物车";
	}

	@Override
	public int getIconId() {
		// TODO Auto-generated method stub
		return R.drawable.tab_shopping;
	}
	
	public void onEventMainThread(ShoppingCarEvent event){
		if(event.getHandleCode() == ShoppingCarEvent.ADD){
			mBadgeViewManager.addShoppingCarNum(event.getNums());
		}else if(event.getHandleCode() == ShoppingCarEvent.SETBOTTOM){
			mShoppingCarMenager.setBottom();
		}else if(event.getHandleCode() == ShoppingCarEvent.SETCUSTOMSTATE){
			mShoppingCarMenager.setCustomState();
		}
	}
	

}
