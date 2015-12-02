package com.hanmimei.activity;

import com.hanmimei.R;
import com.hanmimei.view.viewflow.CircleFlowIndicator;
import com.hanmimei.view.viewflow.ViewFlow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi") 
public class ViewPagerActivity extends BaseActivity {

	private ViewFlow viewFlow;
	private CircleFlowIndicator indicator;
	public int[] images = { R.drawable.first_show, R.drawable.first_show,
			R.drawable.first_show, R.drawable.first_show,
			R.drawable.first_show, };
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.viewpager_panel);
		getActionBar().hide();
		viewFlow = (ViewFlow) findViewById(R.id.my_viewflow);
		indicator = (CircleFlowIndicator) findViewById(R.id.my_indicator);
		viewFlow.setmSideBuffer(images.length);
		viewFlow.setAdapter(new ViewFlowAdapter());
		viewFlow.setFlowIndicator(indicator);
		viewFlow.setSelection(0);
	}
	class ViewFlowAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		
		public ViewFlowAdapter(){
			inflater = LayoutInflater.from(ViewPagerActivity.this);
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object getItem(int position) {
			return images[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.viewpager_panel_item, null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.experience = (TextView) convertView.findViewById(R.id.experience);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			holder.img.setImageResource(images[position % images.length]);
			if(position == 4){
				holder.experience.setVisibility(View.VISIBLE);
				holder.experience.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						ViewPagerActivity.this.startActivity(new Intent(ViewPagerActivity.this,MainActivity.class));
					}
				});
			}
			return convertView;
		}
		private class ViewHolder{
			private ImageView img;
			private TextView experience;
		}
		
	}

}
