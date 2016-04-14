package com.hanmimei.activity;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuView;
import com.hanmimei.R;
import com.hanmimei.adapter.MyCollectionAdapter;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Collection;
import com.hanmimei.entity.CollectionInfo;
import com.hanmimei.entity.HMessage;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;

/**
 * @author eric
 *
 */
public class MyCollectionActivity extends BaseActivity implements
		OnClickListener {

	private SwipeMenuListView mListView;
	private List<Collection> datas;
	private MyCollectionAdapter adapter;
	private MyBroadCastReceiver netReceiver;
	private TextView no_data;
	private LinearLayout no_net;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "我的收藏");
		setContentView(R.layout.my_collection_layout);
		mListView = (SwipeMenuListView) findViewById(R.id.mListView);
		no_data = (TextView) findViewById(R.id.no_data);
		no_net = (LinearLayout) findViewById(R.id.no_net);
		findViewById(R.id.reload).setOnClickListener(this);
		datas = new ArrayList<Collection>();
		adapter = new MyCollectionAdapter(datas, this);
		mListView.setAdapter(adapter);
		loadCollectionData();
		mListView.setMenuCreator(creator);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (datas.get(arg2).getSkuType().equals("item")) {
					Intent intent = new Intent(MyCollectionActivity.this,
							GoodsDetailActivity.class);
					intent.putExtra("url", datas.get(arg2).getSku().getInvUrl());
					startActivity(intent);
				} else if (datas.get(arg2).getSkuType().equals("pin")) {
					Intent intent = new Intent(MyCollectionActivity.this,
							PingouDetailActivity.class);
					intent.putExtra("url", datas.get(arg2).getSku().getInvUrl());
					startActivity(intent);
				}
			}
		});
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void onMenuItemClick(int position, SwipeMenu menu,
					int index, SwipeMenuView view) {
				// TODO Auto-generated method stub
				delCollect(position, view);
			}

		});
		registerReceivers();
	}

	private SwipeMenuCreator creator = new SwipeMenuCreator() {

		@Override
		public void create(SwipeMenu menu) {
			SwipeMenuItem deleteItem = new SwipeMenuItem(
					getApplicationContext());
			// 设置背景颜色
			deleteItem.setBackground(R.drawable.btn_buy_selector);
			// 设置删除的宽度
			deleteItem.setWidth(CommonUtil.dip2px(90));
			// 设置图标
			deleteItem.setIcon(R.drawable.hmm_edit_delete);
			// 增加到menu中
			menu.addMenuItem(deleteItem);
		}
	};

	private void loadCollectionData() {
		Http2Utils.doGetRequestTask(this, getHeaders(),
				UrlUtil.COLLECTION_LIST, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						no_net.setVisibility(View.GONE);
						CollectionInfo collectionInfo = DataParser
								.parserCollect(result);
						if (collectionInfo.gethMessage().getCode() == 200) {
							if (collectionInfo.getList().size() > 0) {
								no_data.setVisibility(View.GONE);
								datas.clear();
								datas.addAll(collectionInfo.getList());
								adapter.notifyDataSetChanged();
							} else {
								no_data.setVisibility(View.VISIBLE);
							}
						} else {
							no_net.setVisibility(View.VISIBLE);
							ToastUtils.Toast(MyCollectionActivity.this, "请求失败");
						}
					}

					@Override
					public void onError() {
						no_net.setVisibility(View.VISIBLE);
						ToastUtils.Toast(MyCollectionActivity.this,
								"请求失败，请检查您的网络");
					}
				});
	}

	private void delCollect(final int position, final SwipeMenuView index) {
		String collectId = datas.get(position).getCollectId();
		Http2Utils.doGetRequestTask(this, getHeaders(), UrlUtil.DEL_COLLECTION
				+ collectId, new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				HMessage hMessage = DataParser.paserResultMsg(result);
				if (hMessage.getCode() == 200) {
					// removeListItem(mListView.getChildAt(position), position);
					datas.remove(position);
					mListView.deleteViewAt(index, true);
					if (datas.size() <= 0) {
						no_data.setVisibility(View.VISIBLE);
						mListView.setVisibility(View.GONE);
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
