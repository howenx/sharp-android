package com.hanmimei.activity;

import com.hanmimei.R;
import com.hanmimei.utils.SharedPreferencesUtil;
import com.hanmimei.view.viewflow.CircleFlowIndicator;
import com.hanmimei.view.viewflow.ViewFlow;
import com.umeng.analytics.MobclickAgent;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi") 
public class IndroductionActivity extends AppCompatActivity {

	private ViewFlow viewFlow;
	private CircleFlowIndicator indicator;
	public int[] images = { R.drawable.first_show, R.drawable.first_show,
			R.drawable.first_show, R.drawable.first_show,
			R.drawable.first_show, };
	private static final String FIRST = "first";
	private static final String FIRST_LOG_FLAG = "first_log_flag";
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.viewpager_panel);
		getSupportActionBar().hide();
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
			inflater = LayoutInflater.from(IndroductionActivity.this);
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
			holder.experience.setVisibility(View.GONE);
			holder.img.setImageResource(images[position % images.length]);
			if(position == 4){
				holder.experience.setVisibility(View.VISIBLE);
				holder.experience.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						SharedPreferencesUtil util = new SharedPreferencesUtil(
								IndroductionActivity.this, FIRST);
						util.putString(FIRST_LOG_FLAG, "not_first");
						IndroductionActivity.this.startActivity(new Intent(IndroductionActivity.this,MainActivity.class));
						finish();
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
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("IndroductionActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
	    MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("IndroductionActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
	    MobclickAgent.onPause(this);
	}

}
