package com.hanmimei.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.ShoppingGoods;
import com.squareup.picasso.Picasso;

public class GoodsBalanceCustomAdapter extends BaseAdapter {
	
	private List<Customs> customs;
	private Context context;
	
	public GoodsBalanceCustomAdapter(List<Customs> customs, Context context) {
		super();
		this.customs = customs;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return customs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return customs.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(arg1 == null){
			arg1 = LayoutInflater.from(context).inflate(R.layout.goods_balance_custom_item_layout, null);
			holder = new ViewHolder(arg1);
			
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		Customs c = customs.get(arg0);
		holder.area_name.setText(c.getInvArea());
		holder.mListView.setAdapter(new GoodsBalanceAdapter(c.getList(), context));
		return arg1;
	}
	
	private class ViewHolder{
		private TextView area_name;
		private ListView mListView;
		public ViewHolder(View view) {
			super();
			this.area_name = (TextView) view.findViewById(R.id.area_name);
			this.mListView = (ListView) view.findViewById(R.id.mListView);
		}
	}
}
