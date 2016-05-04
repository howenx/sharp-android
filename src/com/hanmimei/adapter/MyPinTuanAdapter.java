package com.hanmimei.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.MyPingouDetailActivity;
import com.hanmimei.activity.PingouResultActivity;
import com.hanmimei.entity.PinActivity;
import com.hanmimei.utils.GlideLoaderUtils;

/**
 * @author eric
 *
 */
public class MyPinTuanAdapter extends BaseAdapter {

	private List<PinActivity> data;
	private LayoutInflater inflater;
	private Context mContext;

	public MyPinTuanAdapter(Context mContext, List<PinActivity> data) {
		this.data = data;
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
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
		final PinActivity goods = data.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.my_pingou_item_layout,null);
			holder = new ViewHolder(convertView);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GlideLoaderUtils.loadGoodsImage(mContext,goods.getPinImg().getUrl(), holder.img);
		holder.title.setText(goods.getPinTitle());
		holder.price.setText("成团价：" + goods.getPinPrice());
		if(goods.getStatus().equals("Y")){
			holder.state.setText("拼团中");
			holder.see_order.setVisibility(View.GONE);
			holder.see_tuan.setVisibility(View.VISIBLE);
		}else if(goods.getStatus().equals("F")){
			holder.state.setText("拼团失败");
			holder.see_order.setVisibility(View.GONE);
			holder.see_tuan.setVisibility(View.VISIBLE);
		}else if(goods.getStatus().equals("C")){
			holder.state.setText("拼团成功");
			holder.see_order.setVisibility(View.VISIBLE);
			holder.see_tuan.setVisibility(View.VISIBLE);
		}else if(goods.getStatus().equals("N")){
			holder.state.setText("拼团取消");
			holder.see_order.setVisibility(View.GONE);
			holder.see_tuan.setVisibility(View.VISIBLE);
		}
		
		holder.see_tuan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, PingouResultActivity.class);
				intent.putExtra("url", goods.getPinUrl());
				mContext.startActivity(intent);
			}
		});
		
		holder.see_order.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				doJumpDetail(goods);
			}
		});
		
		return convertView;
	}

	private class ViewHolder {
		private ImageView img;
		private TextView title;
		private TextView price;
		private TextView state;
		private TextView see_tuan;
		private TextView see_order;
		public ViewHolder(View convertView) {
			this.img = (ImageView) convertView.findViewById(R.id.img);
			this.title = (TextView) convertView.findViewById(R.id.title);
			this.price = (TextView) convertView.findViewById(R.id.price);
			this.state = (TextView) convertView.findViewById(R.id.state);
			this.see_tuan = (TextView) convertView.findViewById(R.id.see_tuan);
			this.see_order = (TextView) convertView.findViewById(R.id.see_order);
		}
		
		
	}
	
	//跳转到订单详情界面的方法
		private void doJumpDetail(PinActivity goods) {
			Intent intent = new Intent(mContext, MyPingouDetailActivity.class);
			intent.putExtra("orderId", goods.getOrderId());
			mContext.startActivity(intent);
		}

}
