package com.hanmimei.fragment;

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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.activity.GoodsBalanceActivity;
import com.hanmimei.activity.LoginActivity;
import com.hanmimei.adapter.ShoppingCarPullListAdapter;
import com.hanmimei.dao.ShoppingGoodsDao;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.User;
import com.hanmimei.manager.BadgeViewManager;
import com.hanmimei.manager.ShoppingCarMenager;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.umeng.analytics.MobclickAgent;

public class ShoppingCartFragment extends Fragment implements OnClickListener,
		OnRefreshListener2<ListView> {

	private PullToRefreshListView mListView;
	private LinearLayout bottom;
	private TextView total_price;
	private ImageView check_all;
	private TextView pay;
	private TextView attention;
	private LinearLayout no_data;
	private TextView go_home;
	private List<Customs> data;
	private ShoppingCarPullListAdapter adapter;
	private BaseActivity activity;
	private User user;
	private ShoppingGoodsDao goodsDao;
	private LinearLayout no_net;
	private TextView reload;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
		goodsDao = activity.getDaoSession().getShoppingGoodsDao();
		data = new ArrayList<Customs>();
		shoppingCar = new ShoppingCar();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.shopping_car_list_layout, null);
		findView(view);
		registerReceivers();
		adapter = new ShoppingCarPullListAdapter(data, getActivity());
		mListView.setAdapter(adapter);
		loadData();
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
		List<ShoppingGoods> list = new ArrayList<ShoppingGoods>();
		list = goodsDao.queryBuilder().build().list();
		if (list != null && list.size() > 0) {
			mListView.setVisibility(View.VISIBLE);
			no_data.setVisibility(View.GONE);
			bottom.setVisibility(View.VISIBLE);
			toJsonArray(list);
			getData();
		} else {
			BadgeViewManager.getInstance().setShopCartGoodsNum(0);
			attention.setVisibility(View.INVISIBLE);
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
		activity.getLoading().show();
		Http2Utils.doPostRequestTask2(activity,activity.getNullHeaders(),UrlUtil.CAR_LIST_URL,new VolleyJsonCallback() {
			
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
				no_data.setVisibility(View.GONE);
				no_net.setVisibility(View.VISIBLE);
			}
		},array.toString());
	}
	
	private int getShoppingCarNum(List<Customs> list){
		int nums = 0;
		for(int i = 0; i < list.size(); i ++){
			List<ShoppingGoods> goods = list.get(i).getList();
			for(int j = 0; j < goods.size(); j ++){
				nums = nums + goods.get(j).getGoodsNums();
			}
		}
		return nums ;
	}

	private void afterLoadData(ShoppingCar car){
		activity.getLoading().dismiss();
		mListView.onRefreshComplete();
		data.clear();
		BadgeViewManager.getInstance().setShopCartGoodsNum(0);
		if (car.getMessage() != null) {
			if (car.getList() != null && car.getList().size() > 0) {
				
				BadgeViewManager.getInstance().setShopCartGoodsNum(getShoppingCarNum(car.getList()));
				attention.setVisibility(View.VISIBLE);
				no_data.setVisibility(View.GONE);
				no_net.setVisibility(View.GONE);
				bottom.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.VISIBLE);
				data.addAll(car.getList());
				//
				ShoppingCarMenager.getInstance()
						.initShoppingCarMenager(activity, adapter,
								data, false, attention, check_all,
								total_price, pay, no_data, bottom,mListView);
				ShoppingCarMenager.getInstance().initDrawable(
						getActivity());
				clearPrice();
				//
			} else {
				attention.setVisibility(View.INVISIBLE);
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
			attention.setVisibility(View.INVISIBLE);
			bottom.setVisibility(View.GONE);
			mListView.setVisibility(View.GONE);
			no_data.setVisibility(View.GONE);
			no_net.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
	}
	private void getNetData() {
		activity.getLoading().show();
		Http2Utils.doPostRequestTask2(activity,activity.getHeaders(),UrlUtil.GET_CAR_LIST_URL,new VolleyJsonCallback() {
			
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
				no_data.setVisibility(View.GONE);
				no_net.setVisibility(View.VISIBLE);
			}
		},new JSONObject().toString());
	}


	private void findView(View view) {
		bottom = (LinearLayout) view.findViewById(R.id.bottom);
		total_price = (TextView) view.findViewById(R.id.total_price);
		check_all = (ImageView) view.findViewById(R.id.all);
		if (ShoppingCarMenager.getInstance().getChecked()) {
			check_all.setImageResource(R.drawable.checked);
		} else {
			check_all.setImageResource(R.drawable.check_un);
		}
		pay = (TextView) view.findViewById(R.id.pay);
		attention = (TextView) view.findViewById(R.id.attention);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
		go_home = (TextView) view.findViewById(R.id.go_home);
		mListView.setOnRefreshListener(this);
		mListView.setMode(Mode.PULL_DOWN_TO_REFRESH);
		no_data = (LinearLayout) view.findViewById(R.id.data_null);
		no_net = (LinearLayout) view.findViewById(R.id.no_net);
		reload = (TextView) view.findViewById(R.id.reload);
		reload.setOnClickListener(this);
		check_all.setOnClickListener(this);
		pay.setOnClickListener(this);
		go_home.setOnClickListener(this);
		reload.setOnClickListener(this);
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

	private ShoppingCar shoppingCar;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.all:
			if (ShoppingCarMenager.getInstance().getChecked()) {
				check_all.setImageResource(R.drawable.checked);
				clearPrice();
			} else {
				check_all.setImageResource(R.drawable.check_un);
				doPrice();
			}
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
				Toast.makeText(getActivity(), "请选择商品", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.go_home:
			getActivity().sendBroadcast(
					new Intent(AppConstant.MESSAGE_BROADCAST_GO_HOME));
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
	    MobclickAgent.onPageStart("ShoppingCartFragment"); //统计页面，"MainScreen"为页面名称，可自定义
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("ShoppingCartFragment"); 
	}

}
