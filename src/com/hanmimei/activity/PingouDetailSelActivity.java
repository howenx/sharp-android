package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
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
import com.hanmimei.entity.PinTieredPrice;
import com.hanmimei.entity.StockVo;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.ImageLoaderUtils;

public class PingouDetailSelActivity extends BaseActivity {

	private StockVo stock;
	private List<PinTieredPrice> datas;
	private ListDialogAdapter adapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_list_layout);
		ActionBarUtil.setActionBarStyle(this, "选择");

		stock = (StockVo) getIntent().getSerializableExtra("stock");
		TextView titleView = (TextView) findViewById(R.id.titleView);
		TextView btn_pin = (TextView) findViewById(R.id.btn_pin);
		ImageView iconView = (ImageView) findViewById(R.id.iconView);
		ListView mListView = (ListView) findViewById(R.id.mListView);
		ImageLoaderUtils.loadImage(stock.getInvImgForObj().getUrl(), iconView);
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
						R.layout.dialog_list_item_layout, null);
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
			holder.manjianView.setText("团长特惠：开团立减"+ppr.getMasterCoupon()+"元");
			holder.zengView.setText("团长特惠：赠"+ppr.getMasterCoupon()+"元优惠券");
			if (ppr.isSelected()) {
				if(ppr.getMasterCoupon() != null){
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
				btn_pin.setBackgroundResource(R.color.theme);
				btn_pin.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						
					}
				});
				holder.contentView.setBackgroundResource(R.drawable.bg_red_boarder);
			} else {
				holder.manjianView.setVisibility(View.GONE);
				holder.zengView.setVisibility(View.GONE);
				holder.img.setVisibility(View.GONE);
				holder.contentView.setBackgroundResource(R.color.white);
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

}
