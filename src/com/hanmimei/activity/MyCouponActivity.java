package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.adapter.MyPagerAdapter;
import com.hanmimei.entity.Category;
import com.hanmimei.fragment.CouponFragment;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;

@SuppressLint("NewApi") 
public class MyCouponActivity extends BaseActivity implements OnClickListener{

	
	private static final String TAG_ID_01 = "tag01";
	private static final String TAG_ID_02 = "tag02";
	private static final String TAG_ID_03 = "tag03";
	private static final String TAG_01 = "未使用";
	private static final String TAG_02 = "已使用";
	private static final String TAG_03 = "已过期";
	private TabPageIndicator indicator;
	private ViewPager viewPager;
	private List<Category> data;
	private List<Fragment> fragmentList;
	private MyPagerAdapter adapter;
	
	private AlertDialog dialog;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_order_layout);
		ActionBarUtil.setActionBarStyle(this, "优惠券", R.drawable.icon_duihuan, true, this);
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		viewPager = (ViewPager) findViewById(R.id.pager);
		initCategory();
		initFragment();
	}

	private void initFragment() {
		fragmentList = new ArrayList<Fragment>();
		for(int i = 0; i < data.size(); i ++){
			Category category = data.get(i);
			CouponFragment fragment = new CouponFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("category", category);
			fragment.setArguments(bundle);
			fragmentList.add(fragment);
		}
		adapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList, data);
		viewPager.setAdapter(adapter);
		indicator.setViewPager(viewPager);
	}

	private void initCategory() {
		data = new ArrayList<Category>();
		data.add(new Category(TAG_ID_01, TAG_01));
		data.add(new Category(TAG_ID_02, TAG_02));
		data.add(new Category(TAG_ID_03, TAG_03));
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting:
			showDialog();
			break;
		case R.id.cancle:
			dialog.dismiss();
			break;
		case R.id.besure:
			checkInput();
			break;
		default:
			break;
		}
	}

	private void checkInput() {
		if(input.getText().toString().equals("")){
			ToastUtils.Toast(this, "无效兑换码，兑换失败");
			return;
		}else{
			CommonUtil.closeBoard(this);
			getCoupon();
		}
	}
	
	private ProgressDialog progressDialog;
	private void getCoupon() {
		dialog.dismiss();
		progressDialog = CommonUtil.dialog(this, "...");
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					Message msg = mHandler.obtainMessage(1);
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				progressDialog.dismiss();
				ToastUtils.Toast(MyCouponActivity.this, "兑换失败，无效兑换码");
				break;

			default:
				break;
			}
		}
		
	};
	private EditText input;
	private void showDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.img_save_layout, null);
		view.findViewById(R.id.refresh).setVisibility(View.GONE);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText("兑换优惠券");
		input = (EditText) view.findViewById(R.id.code);
		input.setHint("请输入兑换码");
		view.findViewById(R.id.cancle).setOnClickListener(this);
		view.findViewById(R.id.besure).setOnClickListener(this);
		dialog = new AlertDialog.Builder(this).create();
		dialog.setView(view);
		dialog.show();
		
	}

}
