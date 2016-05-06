package com.hanmimei.activity.mine.collect.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.Collection;
import com.hanmimei.utils.GlideLoaderUtils;

/**
 * @author eric
 *
 */
@SuppressLint("InflateParams")
public class MyCollectionAdapter extends BaseAdapter {

	private List<Collection> data;
	private LayoutInflater inflater;
	private Context mContext;

	public MyCollectionAdapter(List<Collection> list, Context mContext) {
		this.data = list;
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Collection collection = data.get(position);
		ViewHolder holder = null;	
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.my_collection_item_layout, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.size = (TextView) convertView.findViewById(R.id.size);
				holder.pinImg = (ImageView) convertView.findViewById(R.id.pin);
				holder.priceName = (TextView) convertView.findViewById(R.id.price_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			GlideLoaderUtils.loadGoodsImage(mContext,collection.getSku().getInvImg(), holder.img);
			holder.name.setText(collection.getSku().getSkuTitle());
			holder.price.setText("¥" + collection.getSku().getPrice());
			holder.size.setText(collection.getSku().getItemColor() + "  " + collection.getSku().getItemSize());
			if(collection.getSkuType().equals("pin")){
				holder.priceName.setText("最低至：");
				holder.pinImg.setVisibility(View.VISIBLE);
			}else{
				holder.priceName.setText("单价：");
				holder.pinImg.setVisibility(View.GONE);
			}
			return convertView;
	}

	private class ViewHolder {
		private ImageView img;
		private TextView name;
		private TextView price;
		private TextView size;
		private ImageView pinImg;
		private TextView priceName;
	}

}
