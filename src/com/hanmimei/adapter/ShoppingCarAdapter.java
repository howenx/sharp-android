package com.hanmimei.adapter;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.dao.ShoppingGoodsDao;
import com.hanmimei.dao.ShoppingGoodsDao.Properties;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.User;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.manager.BadgeViewManager;
import com.hanmimei.manager.ShoppingCarMenager;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.GlideLoaderUtils;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ToastUtils;

/**
 * @author eric
 *
 */
public class ShoppingCarAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<ShoppingGoods> data;
	private BaseActivity activity;
	private User user;
	private Drawable check_Drawable;
	private Drawable uncheck_Drawable;
	private ShoppingGoodsDao goodsDao;
	private int check_nums;
//	private AlertDialog dialog;
	private boolean isAdd;
	private BaseActivity baseActivity;

	public ShoppingCarAdapter(List<ShoppingGoods> data, Context mContext) {
		inflater = LayoutInflater.from(mContext);
		this.data = data;
		activity = (BaseActivity) mContext;
		baseActivity = (BaseActivity) mContext;
		goodsDao = activity.getDaoSession().getShoppingGoodsDao();
		user = activity.getUser();
		check_Drawable = activity.getResources()
				.getDrawable(R.drawable.hmm_radio_select);
		uncheck_Drawable = activity.getResources().getDrawable(
				R.drawable.hmm_radio_normal);
	}	

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final ShoppingGoods goods = data.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.shopping_car_item_layout,
					null);
			holder = new ViewHolder();
			holder.checkBox = (ImageView) convertView.findViewById(R.id.check);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.nums = (TextView) convertView.findViewById(R.id.nums);
			holder.shixiao = (ImageView) convertView.findViewById(R.id.shixiao);
			holder.jian = (TextView) convertView.findViewById(R.id.jian);
			holder.plus = (TextView) convertView.findViewById(R.id.plus);
			holder.size = (TextView) convertView.findViewById(R.id.size);
			holder.shopping_main = (RelativeLayout) convertView.findViewById(R.id.shopping_main);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//根据状态判断是否选中，s状态显示一次，未登录状态显示之后删除本地数据，登录状态直接显示后台做处理
		if (goods.getState().equals("G")) {
			check_nums = check_nums + 1;
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.checkBox.setImageDrawable(check_Drawable);
		} else if (goods.getState().equals("S")) {
			holder.shixiao.setVisibility(View.VISIBLE);
			holder.shopping_main.setBackgroundColor(activity.getResources().getColor(R.color.shixiao_bg));
			holder.plus.setTextColor(activity.getResources().getColor(R.color.qianhui));
			holder.jian.setTextColor(activity.getResources().getColor(R.color.qianhui));
			holder.checkBox.setVisibility(View.INVISIBLE);
			//购物车商品失效，增减按钮置灰，选择按钮消失
			if (user == null) {
				ShoppingGoodsDao goodsDao = activity.getDaoSession()
						.getShoppingGoodsDao();
				if (goodsDao.queryBuilder()
						.where(Properties.GoodsId.eq(goods.getGoodsId()))
						.build().list() != null) {
					goodsDao.deleteInTx(goodsDao.queryBuilder()
							.where(Properties.GoodsId.eq(goods.getGoodsId()))
							.build().list());
				}
			}
		} else {
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.checkBox.setImageDrawable(uncheck_Drawable);
		}
		// 根据限购数量，判断是否可以继续增减
		if(!goods.getState().equals("S")){
				holder.plus.setTextColor(activity.getResources().getColor(R.color.fontcolor));
				holder.jian.setTextColor(activity.getResources().getColor(R.color.fontcolor));
				holder.shixiao.setVisibility(View.GONE);
			holder.shopping_main.setBackgroundColor(activity.getResources().getColor(R.color.white));
			if(goods.getGoodsNums() >= goods.getRestrictAmount()){
				holder.plus.setTextColor(activity.getResources().getColor(R.color.qianhui));
				holder.plus.setClickable(false);
			}else{
				holder.plus.setTextColor(activity.getResources().getColor(R.color.fontcolor));
				holder.plus.setClickable(true);
			}
		}
		GlideLoaderUtils.loadGoodsImage(activity,goods.getGoodsImg(), holder.img);
		holder.name.setText(goods.getGoodsName());
		holder.size.setText(goods.getItemColor() + "  " + goods.getItemSize());
		holder.price.setText("¥" + CommonUtil.doubleTrans(goods.getGoodsPrice()));
		holder.nums.setText(goods.getGoodsNums() + "");
		holder.jian.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				baseActivity.setShoppingcarChanged(true);
				isAdd = false;
				// 登录状态减少到服务器，未登录状态增减少本地数据库
				if (user != null) {
					//数量大于1的时候才可以进行减一操作
					if (goods.getGoodsNums() > 1){
						goods.setGoodsNums(goods.getGoodsNums() - 1);
					//非失效商品，调用接口 更新服务器商品数量
					if(!goods.getState().equals("S"))
						upGoods(goods);
					}
				} else {
					//数量大于1可以执行减一，s状态直接删除本地数据库数据
					if (goods.getGoodsNums() > 1) {
						goodsDao.deleteAll();
						if(!goods.getState().equals("S")){
							goods.setGoodsNums(goods.getGoodsNums() - 1);
							BadgeViewManager.getInstance().addShoppingCarNum(-1);
						}
						notifyDataSetChanged();
						ShoppingCarMenager.getInstance().setBottom();
						goodsDao.insertInTx(data);
					}
				}

			}
		});
		holder.plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				baseActivity.setShoppingcarChanged(true);
				isAdd = true;
				if (goods.getGoodsNums() < goods.getRestrictAmount()
						|| goods.getRestrictAmount() == 0) {
					// 登录状态增加到服务器，未登录状态增加到本地数据库
					if (user != null) {
						if(!goods.getState().equals("S")){
							goods.setGoodsNums(goods.getGoodsNums() + 1);
							upGoods(goods);
						}
					} else {
						if(!goods.getState().equals("S")){
							goods.setGoodsNums(goods.getGoodsNums() + 1);
							upGoodsN(goods);
					}
					}
				} 
				else {
					ToastUtils.Toast(activity, "本商品限购" + goods.getRestrictAmount() + "件");
				}
			}
		});
		holder.checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (goods.getState().equals("G")) {
					check_nums = check_nums - 1;
					goods.setState("I");
					notifyDataSetChanged();
					ShoppingCarMenager.getInstance().setBottom();
				} else {
					goods.setState("G");
					check_nums = check_nums + 1;
					notifyDataSetChanged();
					ShoppingCarMenager.getInstance().setBottom();
				}
			}
		});
		return convertView;
	}

	// 更新购物车 商品状态
	private void upGoods(final ShoppingGoods goods) {
		final JSONArray array = new JSONArray();
		JSONObject object = new JSONObject();
		try {
			object.put("cartId", goods.getCartId());
			object.put("skuId", goods.getGoodsId());
			object.put("amount", goods.getGoodsNums());
			object.put("state", "I");
			object.put("skuTypeId", goods.getSkuTypeId());
			object.put("skuType", goods.getSkuType());
			array.put(object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		activity.getLoading().show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.post(UrlUtil.GET_CAR_LIST_URL, array,
						"id-token", user.getToken());
				ShoppingCar car = DataParser.parserShoppingCar(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = car;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private void upGoodsN(final ShoppingGoods goods) {
		final JSONArray array = new JSONArray();
		JSONObject object = new JSONObject();
		try {
			object.put("skuId", goods.getGoodsId());
			object.put("amount", goods.getGoodsNums());
			object.put("skuTypeId", goods.getSkuTypeId());
			object.put("skuType", goods.getSkuType());
			array.put(object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		activity.getLoading().show();
		VolleyHttp.doPostRequestTask2( UrlUtil.POST_ADD_CART, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				activity.getLoading().dismiss();
				HMessage hmm = DataParser.paserResultMsg(result);
				if (hmm.getCode() != null) {
					if (hmm.getCode() == 200) {
						notifyDataSetChanged();
						ShoppingCarMenager.getInstance().setBottom();
						goodsDao.deleteAll();
						goodsDao.insertInTx(data);
						if(isAdd){
							BadgeViewManager.getInstance().addShoppingCarNum(1);
						}else{
							BadgeViewManager.getInstance().addShoppingCarNum(-1);
						}
					} else {
						ToastUtils.Toast(activity, hmm.getMessage());
					}
				} else {
					ToastUtils.Toast(activity, "操作失败！");
				}
			}
			
			@Override
			public void onError() {
				activity.getLoading().dismiss();
				ToastUtils.Toast(activity, "操作失败！请检查您的网络");
			}
		},object.toString());

	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				activity.getLoading().dismiss();
				ShoppingCar car = (ShoppingCar) msg.obj;
				HMessage m = car.getMessage();
				if (m != null) {
					if (m.getCode() == 200) {
						notifyDataSetChanged();
						ShoppingCarMenager.getInstance().setBottom();
						if(isAdd){
							BadgeViewManager.getInstance().addShoppingCarNum(1);
						}else{
							BadgeViewManager.getInstance().addShoppingCarNum(-1);
						}
					} else {
						ToastUtils.Toast(activity, m.getMessage());
					}
				} else {
					ToastUtils.Toast(activity, "操作失败！");
				}
				break;
			default:
				break;
			}
		}
	};

	private class ViewHolder {
		private ImageView checkBox;
		private ImageView img;
		private TextView name;
		private TextView price;
		private TextView nums;
		private ImageView shixiao;
		private TextView jian;
		private TextView plus;
		private TextView size;
		private RelativeLayout shopping_main;
	}

}
