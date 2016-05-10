package com.hanmimei.activity.goods.pin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.balance.GoodsBalanceActivity;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.activity.login.LoginActivity;
import com.hanmimei.entity.CustomsVo;
import com.hanmimei.entity.PinTieredPrice;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.StockVo;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.GlideLoaderTools;
import com.hanmimei.utils.ToastUtils;
/**
 * 
 * @author vince
 *
 */
public class PingouDetailSelActivity extends BaseActivity {

	private StockVo stock;
	private List<PinTieredPrice> datas;
	private ListDialogAdapter adapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pingou_detail_sel_layout);
		ActionBarUtil.setActionBarStyle(this, "选择");

		stock = (StockVo) getIntent().getSerializableExtra("stock");
		TextView titleView = (TextView) findViewById(R.id.titleView);
		TextView btn_pin = (TextView) findViewById(R.id.btn_pin);
		ImageView iconView = (ImageView) findViewById(R.id.iconView);
		ListView mListView = (ListView) findViewById(R.id.mListView);
		GlideLoaderTools.loadSquareImage(getActivity(),stock.getInvImgForObj().getUrl(), iconView);
		adapter = new ListDialogAdapter(btn_pin);
		mListView.setAdapter(adapter);
		titleView.setText(stock.getPinTitle());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				for(PinTieredPrice p: datas)
					p.setSelected(false);
				datas.get(arg2).setSelected(true);
				adapter.notifyDataSetChanged();
			}
		});
	}

	private class ListDialogAdapter extends BaseAdapter {

		
		private TextView btn_pin;

		public ListDialogAdapter(TextView btn_pin) {
			datas = new ArrayList<PinTieredPrice>();
			datas.addAll(stock.getPinTieredPricesDatas());
			if(datas.size()>0)
				datas.get(0).setSelected(true);
			this.btn_pin = btn_pin;
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.pingou_detail_sel_item_layout, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final PinTieredPrice ppr = datas.get(position);
			if(position == 0){
				holder.tuijianView.setVisibility(View.VISIBLE);
			}else{
				holder.tuijianView.setVisibility(View.GONE);
			}
			holder.pin.setText("开团人数：" + ppr.getPeopleNum());
			holder.price.setText("¥" + ppr.getPrice());
			holder.manjianView.setText("团长特惠：开团立减"+ppr.getMasterMinPrice()+"元");
			holder.zengView.setText("团长特惠：赠"+ppr.getMasterCoupon()+"元优惠券");
			if (ppr.isSelected()) {
				if(ppr.getMasterMinPrice() != null &&ppr.getMasterMinPrice().compareTo(BigDecimal.ZERO) == 1 ){
					holder.manjianView.setVisibility(View.VISIBLE);
					holder.img.setVisibility(View.VISIBLE);
				}else{
					holder.manjianView.setVisibility(View.GONE);
				}
				
				if(ppr.getMasterCoupon() != null){
					holder.zengView.setVisibility(View.VISIBLE);
					holder.img.setVisibility(View.VISIBLE);
				}else{
					holder.zengView.setVisibility(View.GONE);
				}
				btn_pin.setText("立即开团("+ppr.getPrice()+"元 / "+ppr.getPeopleNum()+"人)");
				btn_pin.setBackgroundResource(R.drawable.btn_theme_radius_selector);
				btn_pin.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						goToGoodsBalance(stock);
					}
				});
				holder.pin.setTextColor(getResources().getColor(R.color.theme));
				holder.price.setTextColor(getResources().getColor(R.color.theme));
				holder.contentView.setBackgroundResource(R.drawable.bg_pingou_selected);
			} else {
				holder.manjianView.setVisibility(View.GONE);
				holder.zengView.setVisibility(View.GONE);
				holder.img.setVisibility(View.GONE);
				holder.pin.setTextColor(getResources().getColor(R.color.black));
				holder.price.setTextColor(getResources().getColor(R.color.black));
				holder.contentView.setBackgroundResource(R.drawable.bg_pingou_normal);
			}

			return convertView;
		}

		private class ViewHolder {
			TextView pin, price;
			TextView manjianView, zengView;
			View contentView,img,tuijianView;
			

			public ViewHolder(View convertView) {
				super();
				this.pin = (TextView) convertView.findViewById(R.id.pinView);
				this.price = (TextView) convertView.findViewById(R.id.pinPriceView);
				this.manjianView = (TextView) convertView.findViewById(R.id.manjianView);
				this.zengView = (TextView) convertView.findViewById(R.id.zengView);
				this.contentView = convertView.findViewById(R.id.contentView);
				this.img = convertView.findViewById(R.id.img);
				this.tuijianView = convertView.findViewById(R.id.tuijianView);
			}
		}
	}
	
	
	private void goToGoodsBalance(StockVo s){
		if(getUser() == null){
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		ShoppingCar car = new ShoppingCar();
		List<CustomsVo> list = new ArrayList<CustomsVo>();
		CustomsVo customs = new CustomsVo();
		ShoppingGoods sgoods;
		if (s.getStatus().equals("Y")) {
			sgoods = new ShoppingGoods();
			sgoods.setGoodsId(s.getId() + "");
			sgoods.setGoodsImg(s.getInvImgForObj().getUrl());
			sgoods.setGoodsName(s.getPinTitle());
			sgoods.setGoodsNums(1);
			for(PinTieredPrice p: datas){
				if(p.isSelected()){
					sgoods.setGoodsPrice(p.getPrice().doubleValue());
					sgoods.setPinTieredPriceId(p.getId());
				}
			}
			if(sgoods.getGoodsPrice() == null){
				ToastUtils.Toast(this, "请选择商品");
				return;
			}
			sgoods.setInvArea(s.getInvArea());
			sgoods.setInvAreaNm(s.getInvAreaNm());
			sgoods.setInvCustoms(s.getInvCustoms());
			sgoods.setPostalTaxRate(s.getPostalTaxRate());
			sgoods.setPostalStandard(s.getPostalStandard());
			sgoods.setSkuType(s.getSkuType());
			sgoods.setSkuTypeId(s.getSkuTypeId());
		} else if (s.getStatus().equals("P")) {
			ToastUtils.Toast(this, "尚未开售");
			return;
		} else {
			ToastUtils.Toast(this, "活动已结束");
			return;
		}
		customs.addShoppingGoods(sgoods);
		customs.setInvArea(sgoods.getInvArea());
		customs.setInvAreaNm(sgoods.getInvAreaNm());
		customs.setInvCustoms(sgoods.getInvCustoms());
		list.add(customs);
		car.setList(list);
		Intent intent = new Intent(this, GoodsBalanceActivity.class);
		intent.putExtra("car", car);
		intent.putExtra("orderType", "pin");
		startActivity(intent);
	}


}
