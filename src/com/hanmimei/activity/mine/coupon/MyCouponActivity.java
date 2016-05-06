package com.hanmimei.activity.mine.coupon;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.activity.mine.coupon.fragment.CouponFragment;
import com.hanmimei.adapter.MyPagerAdapter;
import com.hanmimei.entity.Category;
import com.hanmimei.manager.CouponMenager;
import com.hanmimei.utils.ActionBarUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * @author eric
 *
 */
@SuppressLint("NewApi") 
public class MyCouponActivity extends BaseActivity implements OnClickListener, OnPageChangeListener{
	
	private static final String TAG_ID_01 = "tag01";
	private static final String TAG_ID_02 = "tag02";
	private static final String TAG_ID_03 = "tag03";
	private static final String TAG_01 = "未使用（0）";
	private static final String TAG_02 = "已使用（0）";
	private static final String TAG_03 = "已过期（0）";
	private ViewPager viewPager;
	private TextView t1;
	private TextView t2;
	private TextView t3;
	private TextView c1;
	private TextView c2;
	private TextView c3;
	private List<Category> data;
	private List<Fragment> fragmentList;
	private MyPagerAdapter adapter;
//	private AlertDialog dialog;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_coupon_layout_main);
		ActionBarUtil.setActionBarStyle(this, "优惠券");
		findView();
		initCategory();
		initFragment();
		CouponMenager.getInstance().initCouponMenager(t1, t2, t3);
	}
	
	/**
	 * 
	 */
	private void initFragment() {
		fragmentList = new ArrayList<Fragment>();
		for (int i = 0; i < data.size(); i++) {
			Category category = data.get(i);
			CouponFragment fragment = new CouponFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("category", category);
			fragment.setArguments(bundle);
			fragmentList.add(fragment);
		}
		adapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList,
				data);
		viewPager.setAdapter(adapter);
	}

	/**
	 * 
	 */
	private void findView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setOffscreenPageLimit(3);
		viewPager.addOnPageChangeListener(this);
		t1 = (TextView) findViewById(R.id.t1);
		t2 = (TextView) findViewById(R.id.t2);
		t3 = (TextView) findViewById(R.id.t3);
		c1 = (TextView) findViewById(R.id.cursor1);
		c2 = (TextView) findViewById(R.id.cursor2);
		c3 = (TextView) findViewById(R.id.cursor3);
		findViewById(R.id.tv_guid1).setOnClickListener(this);
		findViewById(R.id.tv_guid2).setOnClickListener(this);
		findViewById(R.id.tv_guid3).setOnClickListener(this);
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
		case R.id.tv_guid1:
			viewPager.setCurrentItem(0);
			break;
		case R.id.tv_guid2:
			viewPager.setCurrentItem(1);
			break;
		case R.id.tv_guid3:
			viewPager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}


	
//	private ProgressDialog progressDialog;
//	private void getCoupon() {
//		dialog.dismiss();
//		progressDialog = CommonUtil.dialog(this, "...");
//		progressDialog.show();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(1000);
//					Message msg = mHandler.obtainMessage(1);
//					mHandler.sendMessage(msg);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}

//	private Handler mHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case 1:
//				progressDialog.dismiss();
//				ToastUtils.Toast(MyNewCouponActivity.this, "兑换失败，无效兑换码");
//				break;
//
//			default:
//				break;
//			}
//		}
//		
//	};
//	private EditText input;
//	private void showDialog() {
//		View view = LayoutInflater.from(this).inflate(R.layout.img_save_layout, null);
//		view.findViewById(R.id.refresh).setVisibility(View.GONE);
//		TextView title = (TextView) view.findViewById(R.id.title);
//		title.setText("兑换优惠券");
//		input = (EditText) view.findViewById(R.id.code);
//		input.setHint("请输入兑换码");
//		view.findViewById(R.id.cancle).setOnClickListener(this);
//		view.findViewById(R.id.besure).setOnClickListener(this);
//		dialog = new AlertDialog.Builder(this).create();
//		dialog.setView(view);
//		dialog.show();
//		
//	}
	@Override
	public void onPageScrollStateChanged(int arg0) {}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}
	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
	case 0:
		setTopSelected(t1, c1);
		setTopUnSelected(t2, c2);
		setTopUnSelected(t3, c3);
		break;
	case 1:
		setTopSelected(t2, c2);
		setTopUnSelected(t1, c1);
		setTopUnSelected(t3, c3);
		break;
	case 2:
		setTopSelected(t3, c3);
		setTopUnSelected(t2, c2);
		setTopUnSelected(t1, c1);
		break;
		default:
			break;
	}
	}
	private void setTopSelected(TextView textView, TextView cusor) {
		textView.setTextColor(getResources().getColor(R.color.theme));
		cusor.setVisibility(View.VISIBLE);
	}

	private void setTopUnSelected(TextView textView, TextView cusor) {
		textView.setTextColor(getResources().getColor(R.color.fontcolor));
		cusor.setVisibility(View.INVISIBLE);
	}

}
