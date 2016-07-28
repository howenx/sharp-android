package com.kakao.kakaogift.activity.balance.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.utils.GlideLoaderTools;
/**
 * 
 * @author vince
 *
 */
public class GoodsBalanceAdapter extends BaseAdapter {
	
	private List<ShoppingGoods> goods;
	private Context context;
	
	public GoodsBalanceAdapter(List<ShoppingGoods> goods, Context context) {
		super();
		this.goods = goods;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return goods.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return goods.get(arg0);
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
			arg1 = LayoutInflater.from(context).inflate(R.layout.goods_balance_item_layout, null);
			holder = new ViewHolder(arg1);
			
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		ShoppingGoods sg = goods.get(arg0);
		holder.pro_name.setText(sg.getGoodsName());
		holder.pro_num.setText(context.getResources().getString(R.string.buyNum, sg.getGoodsNums()));
		holder.pro_price.setText(context.getResources().getString(R.string.price, sg.getGoodsPrice().toString()));
		GlideLoaderTools.loadCirlceImage(context,sg.getGoodsImg(), holder.pro_img);
		return arg1;
	}
	
	private class ViewHolder{
		private TextView pro_name,pro_price,pro_num;
		private ImageView pro_img;
		public ViewHolder(View view) {
			super();
			this.pro_name = (TextView) view.findViewById(R.id.pro_name);
			this.pro_price = (TextView) view.findViewById(R.id.pro_price);
			this.pro_num = (TextView) view.findViewById(R.id.pro_num);
			this.pro_img = (ImageView) view.findViewById(R.id.pro_img);
		}
	}
}
