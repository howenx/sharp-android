package com.hanmimei.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.adapter.ShoppingCarAdapter;
import com.hanmimei.dao.ShoppingGoodsDao;
import com.hanmimei.data.DataParser;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.User;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ShoppingCarMenager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShoppingCartFragment extends Fragment implements
		OnClickListener {

	private PullToRefreshListView mListView;
	private RelativeLayout bottom;
	private TextView goods_nums;
	private TextView total_price;
	private ImageView check_all;
	private TextView pay_price;
	private TextView cut_price;
	private TextView pay;
	private LinearLayout no_data;
	private List<ShoppingGoods> data;
	private ShoppingCarAdapter adapter;
	private BaseActivity activity;
	private User user;
	private ShoppingGoodsDao goodsDao;
	//
	private int nums = 0;
	private int totalPrice = 0;
	private int cutPrice = 0;
	
	private boolean isSelected = false;
	private Drawable check_Drawable;
	private Drawable uncheck_Drawable;
	
//	private ShoppingCarMenager menager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
		goodsDao = activity.getDaoSession().getShoppingGoodsDao();
		data = new ArrayList<ShoppingGoods>();
		check_Drawable = activity.getResources()
				.getDrawable(R.drawable.checked);
		uncheck_Drawable = activity.getResources().getDrawable(
				R.drawable.check_un);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.shopping_car_list_layout, null);
		findView(view);
//		menager = ShoppingCarMenager.getInstance();
		ShoppingCarMenager.getInstance().initShoppingCarMenager(check_all, goods_nums, total_price, pay_price, cut_price, no_data, bottom);
		ShoppingCarMenager.getInstance().initDrawable(getActivity());
		adapter = new ShoppingCarAdapter(data, getActivity());
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
		List<ShoppingGoods> list = goodsDao.queryBuilder().build().list();
		if (list != null && list.size() > 0) {
//			no_data.setVisibility(View.GONE);
//			bottom.setVisibility(View.VISIBLE);
//			data.addAll(list);
//			adapter.notifyDataSetChanged();
			toJsonArray(list);
			getData();
		}else{
			bottom.setVisibility(View.GONE);
			no_data.setVisibility(View.VISIBLE);
		}
	}

	private JSONArray array;
	
	private void toJsonArray(List<ShoppingGoods> list) {
		try {
			array = new JSONArray();
			for(int i = 0; i < list.size(); i ++){
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
		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = HttpUtils.post(
						"http://172.28.3.18:9003/client/cart/get/sku/list",
						array, "null", "");
				ShoppingCar car = DataParser.parserShoppingCar(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = car;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private void getNetData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = HttpUtils.post(
						"http://172.28.3.18:9003/client/cart",
						new JSONObject(), "id-token", user.getToken());
				ShoppingCar car = DataParser.parserShoppingCar(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = car;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				ShoppingCar car = (ShoppingCar) msg.obj;
				if(car.getList() != null && car.getList().size() > 0){
					no_data.setVisibility(View.GONE);
					bottom.setVisibility(View.VISIBLE);
					data.clear();
					data.addAll(car.getList());
					adapter.notifyDataSetChanged();
				}else{
					bottom.setVisibility(View.GONE);
					no_data.setVisibility(View.VISIBLE);
				}
				break;

			default:
				break;
			}
		}
		
	};
	private void findView(View view) {
		bottom = (RelativeLayout) view.findViewById(R.id.bottom);
		goods_nums = (TextView) view.findViewById(R.id.goods_nums);
		total_price = (TextView) view.findViewById(R.id.total_price);
		check_all = (ImageView) view.findViewById(R.id.all);
		pay_price = (TextView) view.findViewById(R.id.end_price);
		cut_price = (TextView) view.findViewById(R.id.cost_price);
		pay = (TextView) view.findViewById(R.id.pay);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
		no_data = (LinearLayout) view.findViewById(R.id.data_null);
		check_all.setOnClickListener(this);
	}

	private void doPrice() {
		for (int i = 0; i < data.size(); i++) {
			data.get(i).setState("G");
			nums = nums + data.get(i).getGoodsNums();
			totalPrice = totalPrice + data.get(i).getGoodsNums()
					* data.get(i).getGoodsPrice();
		}
		adapter.notifyDataSetChanged();
//		goods_nums.setText("商品数量：" + nums);
//		total_price.setText("总金额：" + totalPrice);
//		pay_price.setText("应付金额：" + payPrice);
//		cut_price.setText("已节省：" + cutPrice);
		ShoppingCarMenager.getInstance().ClearAll();
		ShoppingCarMenager.getInstance().setAll(nums, totalPrice, cutPrice);
	}

	private void clearPrice() {
		for (int i = 0; i < data.size(); i++) {
			data.get(i).setState("I");
		}
		adapter.notifyDataSetChanged();
//		goods_nums.setText("商品数量：" + 0);
//		total_price.setText("总金额：" + 0);
//		pay_price.setText("应付金额：" + 0);
//		cut_price.setText("已节省：" + 0);
//		nums = 0;
//		totalPrice = 0;
//		payPrice = 0;
//		cutPrice = 0;
		ShoppingCarMenager.getInstance().ClearAll();
		nums = 0;
		totalPrice = 0;
		cutPrice = 0;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.all:
			if(isSelected){
				check_all.setImageDrawable(uncheck_Drawable);
				clearPrice();
				isSelected = false;
			}else{
				check_all.setImageDrawable(check_Drawable);
				doPrice();
				isSelected = true;
			}
			break;

		default:
			break;
		}
	}

	

}
