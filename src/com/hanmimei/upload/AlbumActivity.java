package com.hanmimei.upload;

import java.io.Serializable;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.data.AppConstant;
import com.hanmimei.utils.ActionBarUtil;

public class AlbumActivity extends BaseActivity {
	// ArrayList<Entity> dataList;//用来装载数据源的列表
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;// 自定义的适配器
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "相册");
		setContentView(R.layout.upload_activity_image_bucket);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		initData();
		initView();
		registerReceivers();
	}
	
	

	/**
	 * 初始化数据
	 */
	private void initData() {
		dataList = helper.getImagesBucketList(false);	
		bimap=BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(AlbumActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(AlbumActivity.this,
						ImageGridActivity.class);
				intent.putExtra(AlbumActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				startActivity(intent);
			}

		});
	}
	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mSelCastReceiver);
	}



	private SelCastReceiver mSelCastReceiver;
	
	// 广播接收者 注册
		private void registerReceivers() {
			mSelCastReceiver = new SelCastReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_IMG_SEL_OK_ACTION);
			getActivity().registerReceiver(mSelCastReceiver, intentFilter);
		}
	
	private class SelCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(AppConstant.MESSAGE_BROADCAST_IMG_SEL_OK_ACTION)){
				finish();
			}
		}
	}
}
