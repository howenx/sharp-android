package com.hanmimei.activity.car.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.activity.goods.detail.GoodsDetailActivity;
import com.hanmimei.dao.ShoppingGoodsDao;
import com.hanmimei.dao.ShoppingGoodsDao.Properties;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.CustomsVo;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.User;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.manager.BadgeViewManager;
import com.hanmimei.manager.ShoppingCarMenager;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.KeyWordUtil;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.CustomListView;

/**
 * @author eric
 * 
 */
public class ShoppingCarPullListAdapter extends BaseAdapter {

	private List<CustomsVo> data;
	private LayoutInflater inflater;
	private ShoppingCarAdapter adapter;
	private BaseActivity activity;
	private Drawable check_Drawable;
	private Drawable uncheck_Drawable;
	private AlertDialog dialog;
	private ShoppingGoodsDao goodsDao;
	private User user;
	private List<ShoppingGoods> goodsList;
	private ShoppingGoods delGoods;

	public ShoppingCarPullListAdapter(List<CustomsVo> data, Context mContext) {
		this.data = ShoppingCarMenager.getInstance().getData();
		inflater = LayoutInflater.from(mContext);
		activity = (BaseActivity) mContext;
		check_Drawable = activity.getResources().getDrawable(
				R.drawable.hmm_radio_select);
		uncheck_Drawable = activity.getResources().getDrawable(
				R.drawable.hmm_radio_normal);
		goodsDao = activity.getDaoSession().getShoppingGoodsDao();
		user = activity.getUser();
		goodsList = new ArrayList<ShoppingGoods>();
		delGoods = new ShoppingGoods();
		
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
		final CustomsVo custom = data.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.shoppingcar_pull_item, null);
			holder = new ViewHolder();
			holder.area = (TextView) convertView.findViewById(R.id.area);
			holder.listView = (CustomListView) convertView
					.findViewById(R.id.my_listview);
			holder.tax = (TextView) convertView.findViewById(R.id.attention);
			holder.check = (ImageView) convertView.findViewById(R.id.check);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (custom.getState().equals("G")) {
					custom.setState("N");
					updataAllGoodsState(custom,"N");
				} else {
					custom.setState("G");
					updataAllGoodsState(custom,"Y");
				}
			}
		});
		if (custom.getState().equals("G")) {
			holder.check.setImageDrawable(check_Drawable);
		} else {
			holder.check.setImageDrawable(uncheck_Drawable);
		}
		if(custom.getInvAreaNm() != null)
			holder.area.setText(custom.getInvAreaNm());
		String tax = "";

		if (Double.valueOf(custom.getTax()) != 0) {
			holder.tax.setVisibility(View.VISIBLE);
			if (Double.valueOf(custom.getTax()) - custom.getPostalStandard() > 0) {
				tax = "行邮税:¥" + custom.getTax();
				KeyWordUtil.setDifrentFontColor(activity, holder.tax, tax, 4,
						tax.length());
			} else {
				tax = "行邮税:¥" + custom.getTax() + "(免)";
				KeyWordUtil.setDifrentFontColor(activity, holder.tax, tax, 4,
						tax.length());
			}
			
		} else {
			holder.tax.setVisibility(View.GONE);
		}
		holder.listView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						goodsList = custom.getList();
						delGoods = custom.getList().get(arg2);
						showDelDialog();
						return true;
					}
				});
		holder.listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(activity, GoodsDetailActivity.class);
				intent.putExtra("url", custom.getList().get(arg2).getGoodsUrl());
				activity.startActivity(intent);
			}
		});
		adapter = new ShoppingCarAdapter(custom.getList(), activity);
		holder.listView.setAdapter(adapter);
		return convertView;
	}

	/**
	 * @param custom
	 * @param b 
	 */
	private void updataAllGoodsState(final CustomsVo custom, final String  selected) {
		final JSONArray array = new JSONArray();
		for(int i =0 ; i < custom.getList().size(); i ++){
			ShoppingGoods goods = custom.getList().get(i);
			JSONObject object = new JSONObject();
			try {
				object.put("cartId", goods.getCartId());
				object.put("skuId", goods.getGoodsId());
				object.put("amount", goods.getGoodsNums());
				object.put("skuTypeId", goods.getSkuTypeId());
				object.put("skuType", goods.getSkuType());
				object.put("state", goods.getState());
				object.put("orCheck", selected);
				object.put("cartSource", 1);
				object.put("pinTieredPriceId", null);
				array.put(object);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		activity.getLoading().show();
		VolleyHttp.doPostRequestTask2(activity.getHeaders(),UrlUtil.UPDATA_SHOPPING_STATE, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				activity.getLoading().dismiss();
				HMessage hmm = DataParser.paserResultMsg(result);
				if (hmm.getCode() != null) {
					if (hmm.getCode() == 200) {
						notifyDataSetChanged();
						ShoppingCarMenager.getInstance().setCustomState();
					} else {
						if(selected.equals("Y")){
							custom.setState("N");
						}else{
							custom.setState("G");
						}
						ToastUtils.Toast(activity, hmm.getMessage());
					}
				} else {
					if(selected.equals("Y")){
						custom.setState("N");
					}else{
						custom.setState("G");
					}
					ToastUtils.Toast(activity, "操作失败！");
				}
			}
			
			@Override
			public void onError() {
				activity.getLoading().dismiss();
				ToastUtils.Toast(activity, "操作失败！请检查您的网络");
			}
		},array.toString());
	}

	/**
	 * 弹出确认删除窗口
	 */
	private void showDelDialog() {
		dialog = AlertDialogUtils.showDialog(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.setShoppingcarChanged(true);
				if (user != null) {
					delGoods(delGoods);
				} else {
					dialog.dismiss();
					if (!delGoods.getState().equals("S")) {
						goodsDao.delete(goodsDao
								.queryBuilder()
								.where(Properties.GoodsId.eq(delGoods.getGoodsId()))
								.build().unique());
					}
					goodsList.remove(delGoods);
					notifyDataSetChanged();
					ShoppingCarMenager.getInstance().setBottom();
				}

			}
		});
	}

	/**
	 * @param 删除购物车商品
	 */
	protected void delGoods(final ShoppingGoods goods) {
		dialog.dismiss();
		activity.getLoading().show();
		VolleyHttp.doGetRequestTask( activity.getHeaders(), goods.getDelUrl(), new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				activity.getLoading().dismiss();
				HMessage hm = DataParser.paserResultMsg(result);
				if (hm.getCode() != null) {
					if (hm.getCode() == 200) {
						goodsList.remove(delGoods);
						BadgeViewManager.getInstance().addShoppingCarNum(-delGoods.getGoodsNums());
						notifyDataSetChanged();
						ShoppingCarMenager.getInstance().setBottom();
					} else {
						ToastUtils.Toast(activity, hm.getMessage());
					}
				} else {
					ToastUtils.Toast(activity, "删除失败！");
				}
				
			}
			@Override
			public void onError() {
				activity.getLoading().dismiss();
				ToastUtils.Toast(activity, "删除失败，请检查您的网络！");
			}
		});
	}

	private class ViewHolder {
		private ImageView check;
		private TextView area;
		private CustomListView listView;
		private TextView tax;
	}

}
