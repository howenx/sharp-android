package com.kakao.kakaogift.activity.mine.collect;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.flyco.dialog.widget.NormalDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouDetailActivity;
import com.kakao.kakaogift.activity.mine.collect.adapter.MyCollectionAdapter;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.Collection;
import com.kakao.kakaogift.entity.CollectionVo;
import com.kakao.kakaogift.entity.HMessage;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.AlertDialogUtils;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.DataNoneLayout;

/**
 * @author eric
 *
 */
public class MyCollectionActivity extends BaseActivity implements
		OnClickListener {

	private ListView mListView;
	private List<Collection> datas;
	private MyCollectionAdapter adapter;
	private MyBroadCastReceiver netReceiver;
	private LinearLayout no_net;
	private RelativeLayout collection_main;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "我的收藏");
		setContentView(R.layout.my_collection_layout);
		mListView = (ListView) findViewById(R.id.mListView);
		no_net = (LinearLayout) findViewById(R.id.no_net);
		collection_main = (RelativeLayout) findViewById(R.id.collection_main);
		findViewById(R.id.reload).setOnClickListener(this);
		datas = new ArrayList<Collection>();
		adapter = new MyCollectionAdapter(datas, this);
		mListView.setAdapter(adapter);
		loadCollectionData();
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (datas.get(arg2).getSkuType().equals("pin")) {
					Intent intent = new Intent(MyCollectionActivity.this,
							PingouDetailActivity.class);
					intent.putExtra("url", datas.get(arg2).getSku().getInvUrl());
					startActivity(intent);
				}else{
					Intent intent = new Intent(MyCollectionActivity.this,
							GoodsDetailActivity.class);
					intent.putExtra("url", datas.get(arg2).getSku().getInvUrl());
					startActivity(intent);
				} 
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				showDelDialog(arg2);
				return true;
			}
		});
		registerReceivers();
		
	}


	private NormalDialog dialog;
	/**
	 * @param arg2
	 */
	private void showDelDialog(final int arg2) {
		dialog = AlertDialogUtils.showDialog(this, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				delCollect(arg2);
			}
		});
	}

	private void loadCollectionData() {
		getActivity().getLoading().show();
		VolleyHttp.doGetRequestTask( getHeaders(),
				UrlUtil.COLLECTION_LIST, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getActivity().getLoading().dismiss(); 
						no_net.setVisibility(View.GONE);
						CollectionVo collectionInfo = DataParser
								.parserCollect(result); 
						if (collectionInfo.getMessage().getCode() == 200) {
							if (collectionInfo.getList().size() > 0) {
								datas.clear();
								datas.addAll(collectionInfo.getList());
								adapter.notifyDataSetChanged();
								if(dataNoneLayout != null)
									dataNoneLayout.setNoVisible();
							} else {
								setDataNone();
							}
						} else {
							if(dataNoneLayout != null)
								dataNoneLayout.setNoVisible();
							no_net.setVisibility(View.VISIBLE);
							ToastUtils.Toast(MyCollectionActivity.this, "请求失败");
						}
					}

					@Override
					public void onError() {
						getActivity().getLoading().dismiss();
						if(dataNoneLayout != null)
							dataNoneLayout.setNoVisible();
						no_net.setVisibility(View.VISIBLE);
						ToastUtils.Toast(MyCollectionActivity.this,
								"请求失败，请检查您的网络");
					}
				});
	}
	/**
	 * 
	 */
	private DataNoneLayout dataNoneLayout;
	private void setDataNone() {
		if(dataNoneLayout == null){
			dataNoneLayout = new DataNoneLayout(MyCollectionActivity.this, collection_main);
			dataNoneLayout.setNullImage(R.drawable.icon_shoucang_none);
			dataNoneLayout.setText("您的收藏夹是空的");
			dataNoneLayout.setMode(Mode.DISABLED);
			dataNoneLayout.loadData(3);
		}else{
			dataNoneLayout.setVisible();
		}
	}

	//final SwipeMenuView index
	private void delCollect(final int position) {
		String collectId = datas.get(position).getCollectId();
		VolleyHttp.doGetRequestTask( getHeaders(), UrlUtil.DEL_COLLECTION
				+ collectId, new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) { 
				dialog.dismiss();
				HMessage hMessage = DataParser.paserResultMsg(result);
				if (hMessage.getCode() == 200) {
					datas.remove(position);
					adapter.notifyDataSetChanged();
					if (datas.size() <= 0) {
						setDataNone();
					}
				} else {
					ToastUtils.Toast(MyCollectionActivity.this, "取消收藏失败");
				}
			}

			@Override
			public void onError() {
				ToastUtils.Toast(MyCollectionActivity.this, "取消收藏失败");
			}
		});
	}
	// 广播接收者 注册
	private void registerReceivers() {
		netReceiver = new MyBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION);
		getActivity().registerReceiver(netReceiver, intentFilter);
	}

	private class MyBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION)) {
				loadCollectionData();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(netReceiver);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reload:
			loadCollectionData();
			break;

		default:
			break;
		}
	}

}
