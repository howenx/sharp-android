package com.kakao.kakaogift.activity.car.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.flyco.dialog.widget.NormalDialog;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.dao.ShoppingGoodsDao;
import com.kakao.kakaogift.dao.ShoppingGoodsDao.Properties;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.CustomsVo;
import com.kakao.kakaogift.entity.HMessage;
import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.event.ShoppingCarEvent;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.manager.ShoppingCarMenager;
import com.kakao.kakaogift.utils.AlertDialogUtils;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.CustomListView;
import com.ypy.eventbus.EventBus;

/**
 * @author eric
 * 购物车外层 仓库的adapter
 * 
 */
public class ShoppingCarPullListAdapter extends BaseAdapter {

	private List<CustomsVo> data;
	private LayoutInflater inflater;
	private ShoppingCarAdapter adapter;
	private BaseActivity activity;
	private NormalDialog dialog;
	private ShoppingGoodsDao goodsDao;
	private List<ShoppingGoods> goodsList;
	private ShoppingGoods delGoods;

	public ShoppingCarPullListAdapter(List<CustomsVo> data, Context mContext) {
		this.data = data;
		inflater = LayoutInflater.from(mContext);
		activity = (BaseActivity) mContext;
		goodsDao = activity.getDaoSession().getShoppingGoodsDao();
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

	@SuppressLint("InflateParams")
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
//			holder.tax = (TextView) convertView.findViewById(R.id.attention);
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
					if(activity.getHeaders() != null){
						updataAllGoodsState(custom,"N");
					}else{
						updataAllLocalState(custom,"N");
					}
				} else {
					custom.setState("G");
					if(activity.getHeaders() != null){
						updataAllGoodsState(custom,"Y");
					}else{
						updataAllLocalState(custom,"Y");
					}
				}
			}
		});
		//根据仓库状态  显示是否选中
		if (custom.getState().equals("G")) {
			holder.check.setImageResource(R.drawable.hmm_radio_select);
		} else {
			holder.check.setImageResource(R.drawable.hmm_radio_normal);
		}
		if(custom.getInvAreaNm() != null)
			holder.area.setText(custom.getInvAreaNm());
		/*
		行邮税的显示方式
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
		*/
		//长按删除商品
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
	 * @param string
	 * 未登陆状态下更新整个仓库的商品选中状态
	 */
	private void updataAllLocalState(CustomsVo custom, String state) {
		List<ShoppingGoods> list = new ArrayList<ShoppingGoods>();
		for(int i = 0; i < custom.getList().size(); i ++){
			//查找本地该购物车数据，修改选中状态
			ShoppingGoods shoppingGoods = activity.getDaoSession().getShoppingGoodsDao().queryBuilder()
			.where(Properties.GoodsId.eq(custom.getList().get(i).getGoodsId())).unique();
			shoppingGoods.setOrCheck(state);
			list.add(shoppingGoods);
		}
		//更新本地购物车数据
		activity.getDaoSession().getShoppingGoodsDao().updateInTx(list);
		//界面显示信息的更新
		notifyDataSetChanged();
		EventBus.getDefault().post(new ShoppingCarEvent(ShoppingCarEvent.SETCUSTOMSTATE));
		activity.setShoppingcarChanged(true);
	}

	/**
	 * @param custom
	 * @param b 
	 * 登陆状态下更新整个仓库的商品选中状态
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
						activity.setShoppingcarChanged(true);
						notifyDataSetChanged();
						EventBus.getDefault().post(new ShoppingCarEvent(ShoppingCarEvent.SETCUSTOMSTATE));
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
				if(selected.equals("Y")){
					custom.setState("N");
				}else{
					custom.setState("G");
				}
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
				//根据是否登陆，来执行删除操作
				if (activity.getHeaders() != null) {
					delGoods(delGoods);
				} else {//未登陆状态下，删除本地数据库相应数据，并更新界面
					dialog.dismiss();
					if (!delGoods.getState().equals("S")) {
						goodsDao.delete(goodsDao
								.queryBuilder()
								.where(Properties.GoodsId.eq(delGoods.getGoodsId()))
								.build().unique());
					}
					goodsList.remove(delGoods);
					notifyDataSetChanged();
					EventBus.getDefault().post(new ShoppingCarEvent(ShoppingCarEvent.SETBOTTOM));
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
						EventBus.getDefault().post(new ShoppingCarEvent(ShoppingCarEvent.ADD,-delGoods.getGoodsNums()));
						notifyDataSetChanged();
						EventBus.getDefault().post(new ShoppingCarEvent(ShoppingCarEvent.SETBOTTOM));
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
//		private TextView tax;
	}

}
