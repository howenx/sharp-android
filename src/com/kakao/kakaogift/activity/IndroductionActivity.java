package com.kakao.kakaogift.activity;

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

import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.main.HMainActivity;
import com.kakao.kakaogift.utils.PreferenceUtil.IntroConfig;
import com.kakao.kakaogift.view.viewflow.CircleFlowIndicator;
import com.kakao.kakaogift.view.viewflow.ViewFlow;

/**
 * @author eric
 * 
 */
@SuppressLint("NewApi")
public class IndroductionActivity extends AppCompatActivity {

	private ViewFlow viewFlow;
//	private CircleFlowIndicator indicator;
	public int[] images = { R.drawable.intro_1, R.drawable.intro_2, R.drawable.intro_3 };

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.viewpager_panel);
		getSupportActionBar().hide();
		viewFlow = (ViewFlow) findViewById(R.id.my_viewflow);
//		indicator = (CircleFlowIndicator) findViewById(R.id.my_indicator);
		viewFlow.setmSideBuffer(images.length);
		viewFlow.setAdapter(new ViewFlowAdapter());
//		viewFlow.setFlowIndicator(indicator);
		viewFlow.setSelection(0);
		findViewById(R.id.tiaoguo).setOnClickListener(TurnListener);
	}
	
	private OnClickListener TurnListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			startActivity(new Intent(IndroductionActivity.this,HMainActivity.class));
			IntroConfig.putIntroCfg(getApplicationContext(),IntroConfig.INTRO_CONFIG_VALUE_NO);
			finish();
		}
	};
	
	
	
	

	class ViewFlowAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public ViewFlowAdapter() {
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
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.viewpager_panel_item, null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.experience = convertView.findViewById(R.id.experience);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.img.setImageResource(images[position % images.length]);
			if (position == images.length - 1) {
				holder.experience.setVisibility(View.VISIBLE);
				holder.experience.setOnClickListener(TurnListener);
			} else {
				holder.experience.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}

		private class ViewHolder {
			private ImageView img;
			private View experience;
		}

	}

}
