package com.hanmimei.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.Customs;
import com.hanmimei.manager.ShoppingCarMenager;
import com.hanmimei.utils.KeyWordUtil;
import com.hanmimei.view.CustomListView;

public class ShoppingCarPullListAdapter extends BaseAdapter {

	private List<Customs> data;
	private LayoutInflater inflater;
	private ShoppingCarAdapter adapter;
	private Activity activity;
	private Drawable check_Drawable;
	private Drawable uncheck_Drawable;
	
	public ShoppingCarPullListAdapter(List<Customs> data, Context mContext){
		this.data = ShoppingCarMenager.getInstance().getData();
		inflater = LayoutInflater.from(mContext);
		activity = (Activity) mContext;
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
		final Customs custom = data.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.shoppingcar_pull_item, null);
			holder = new ViewHolder();
			holder.area = (TextView) convertView.findViewById(R.id.area);
			holder.listView = (CustomListView) convertView.findViewById(R.id.my_listview);
			holder.tax = (TextView) convertView.findViewById(R.id.attention);
			holder.check = (ImageView) convertView.findViewById(R.id.check);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(custom.getState().equals("G")){
					custom.setState("N");
					notifyDataSetChanged();
					ShoppingCarMenager.getInstance().setCustomState();
				}else{
					custom.setState("G");
					notifyDataSetChanged();
					ShoppingCarMenager.getInstance().setCustomState();
				}
			}
		});
		if(custom.getState().equals("G")){
			holder.check.setImageDrawable(check_Drawable);
		}else{
			holder.check.setImageDrawable(uncheck_Drawable);
		}
		holder.area.setText(custom.getInvAreaNm());
		String tax = "";
		
		if(Double.valueOf(custom.getTax()) != 0){
			holder.tax.setVisibility(View.VISIBLE);
//			if(Double.valueOf(custom.getTax())-custom.getPostalStandard() > 0){
				tax = "行邮税:¥" + custom.getTax();
				KeyWordUtil.setDifrentFontColor(activity, holder.tax, tax, 4, tax.length());
//			}else{
//				tax = "行邮税:¥" + custom.getTax() + "（免）";
//				KeyWordUtil.setDifrentFontColor(activity, holder.tax, tax, 4, tax.length());
//			}
		}else{
			holder.tax.setVisibility(View.GONE);
		}
		adapter = new ShoppingCarAdapter(custom.getList(), activity);
		holder.listView.setAdapter(adapter);
		return convertView;
	}
	private class ViewHolder{
		private ImageView check;
		private TextView area;
		private CustomListView listView;
		private TextView tax;
	}

}
