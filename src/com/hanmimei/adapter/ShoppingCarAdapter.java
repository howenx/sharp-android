package com.hanmimei.adapter;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.activity.GoodsDetailActivity;
import com.hanmimei.dao.ShoppingGoodsDao;
import com.hanmimei.dao.ShoppingGoodsDao.Properties;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.User;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.InitImageLoader;
import com.hanmimei.utils.ShoppingCarMenager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShoppingCarAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<ShoppingGoods> data;
	private ImageLoader imageLoader;
	private DisplayImageOptions imageOptions;
	private BaseActivity activity;
	private User user;
	private Drawable check_Drawable;
	private Drawable uncheck_Drawable;
	private ShoppingGoodsDao goodsDao;
	private int check_nums;

	public ShoppingCarAdapter(List<ShoppingGoods> data, Context mContext) {
		inflater = LayoutInflater.from(mContext);
		imageLoader = InitImageLoader.initLoader(mContext);
		imageOptions = InitImageLoader.initOptions();
		this.data = data;
		activity = (BaseActivity) mContext;
		goodsDao = activity.getDaoSession().getShoppingGoodsDao();
		user = activity.getUser();
		check_Drawable = activity.getResources()
				.getDrawable(R.drawable.checked);
		uncheck_Drawable = activity.getResources().getDrawable(
				R.drawable.check_un);
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
			holder.del = (ImageView) convertView.findViewById(R.id.delete);
			holder.jian = (TextView) convertView.findViewById(R.id.jian);
			holder.plus = (TextView) convertView.findViewById(R.id.plus);
			holder.size = (TextView) convertView.findViewById(R.id.size);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (goods.getState().equals("G")) {
			check_nums = check_nums + 1;
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.checkBox.setImageDrawable(check_Drawable);
		} else if (goods.getState().equals("S")) {
			holder.checkBox.setVisibility(View.INVISIBLE);
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
		// }
		imageLoader.displayImage(goods.getGoodsImg(), holder.img, imageOptions);
		holder.name.setText(goods.getGoodsName());
		holder.size.setText(goods.getItemColor() + "  " + goods.getItemSize());
		holder.price.setText("¥" + goods.getGoodsPrice());
		holder.nums.setText(goods.getGoodsNums() + "");
		holder.name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(activity, GoodsDetailActivity.class);
				intent.putExtra("id", goods.getGoodsId());
				activity.startActivity(intent);
			}
		});
		holder.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(activity, GoodsDetailActivity.class);
				intent.putExtra("url", goods.getGoodsUrl());
				activity.startActivity(intent);
			}
		});
		holder.jian.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				// 登录状态减少到服务器，未登录状态增减少本地数据库
				if (user != null) {
					if (goods.getGoodsNums() > 1)
						goods.setGoodsNums(goods.getGoodsNums() - 1);
					if(!goods.getState().equals("S"))
						upGoods(goods);
				} else {
					if (goods.getGoodsNums() > 1) {
						goodsDao.deleteAll();
						if(!goods.getState().equals("S"))
							goods.setGoodsNums(goods.getGoodsNums() - 1);
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
				if (goods.getGoodsNums() < goods.getRestrictAmount()
						|| goods.getRestrictAmount() == 0) {
					// 登录状态增加到服务器，未登录状态增加到本地数据库
					if (user != null) {
						goods.setGoodsNums(goods.getGoodsNums() + 1);
						if(!goods.getState().equals("S"))
							upGoods(goods);
					} else {
						goods.setGoodsNums(goods.getGoodsNums() + 1);
						if(!goods.getState().equals("S"))
							upGoodsN(goods);
					}
				} else {
					Toast.makeText(activity,
							"本商品限购" + goods.getRestrictAmount() + "件",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		holder.del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 登录状态删除服务器数据，未登录状态删除本地数据
				if (user != null) {
					delGoods(goods);
				} else {
					if (!goods.getState().equals("S")) {
						goodsDao.delete(goodsDao
								.queryBuilder()
								.where(Properties.GoodsId.eq(goods.getGoodsId()))
								.build().unique());
					}
					data.remove(goods);
					notifyDataSetChanged();
					ShoppingCarMenager.getInstance().setBottom();
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

	private ShoppingGoods delGoods;

	// 删除购物车商品
	private void delGoods(final ShoppingGoods goods) {
		delGoods = goods;
		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = HttpUtils.get(goods.getDelUrl(),
						user.getToken());
				HMessage hm = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = hm;
				mHandler.sendMessage(msg);
			}
		}).start();

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
			array.put(object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.post(UrlUtil.GET_CAR_LIST_URL, array,
						"id-token", user.getToken());
				ShoppingCar car = DataParser.parserShoppingCar(result);
				Message msg = mHandler.obtainMessage(3);
				msg.obj = car;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private void upGoodsN(final ShoppingGoods goods) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = HttpUtils.get(UrlUtil.SEND_CAR_TO_SERVER_UN
						+ goods.getGoodsId() + "/" + goods.getGoodsNums());
				HMessage hm = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(2);
				// msg.obj = car;
				msg.obj = hm;
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
				HMessage hm = (HMessage) msg.obj;
				if (hm != null) {
					if (hm.getCode() == 200) {
						data.remove(delGoods);
						notifyDataSetChanged();
						ShoppingCarMenager.getInstance().setBottom();
					} else {
						Toast.makeText(activity, hm.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(activity, "删除失败！", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case 2:
				HMessage hmm = (HMessage) msg.obj;
				if (hmm != null) {
					if (hmm.getCode() == 200) {
						notifyDataSetChanged();
						ShoppingCarMenager.getInstance().setBottom();
						goodsDao.deleteAll();
						goodsDao.insertInTx(data);
					} else {
						Toast.makeText(activity, hmm.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(activity, "操作失败！", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case 3:
				ShoppingCar car = (ShoppingCar) msg.obj;
				HMessage m = car.getMessage();
				if (m != null) {
					if (m.getCode() == 200) {
						notifyDataSetChanged();
						ShoppingCarMenager.getInstance().setBottom();
					} else {
						Toast.makeText(activity, m.getMessage(),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(activity, "操作失败！", Toast.LENGTH_SHORT)
							.show();
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
		private ImageView del;
		private TextView jian;
		private TextView plus;
		private TextView size;
	}

}
