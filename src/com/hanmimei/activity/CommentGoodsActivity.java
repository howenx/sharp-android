/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-26 上午9:36:03 
**/
package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.hanmimei.R;
import com.hanmimei.adapter.CommentGoodsAdapter;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.CommentCenter;
import com.hanmimei.entity.OrderRemark;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.ToastUtils;

/**
 * @author eric
 *
 */
public class CommentGoodsActivity extends BaseActivity{

	private ListView mListView;
	private CommentGoodsAdapter adapter;
	private List<OrderRemark> list;
	private String orderId;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_goods_layout);
		ActionBarUtil.setActionBarStyle(this, "评价中心");
//		list = ((Order)getIntent().getSerializableExtra("order")).getList();
		list = new ArrayList<OrderRemark>();
		orderId = getIntent().getStringExtra("orderId");
		mListView = (ListView) findViewById(R.id.mylist);
		adapter = new CommentGoodsAdapter(this, list);
		mListView.setAdapter(adapter);
		loadData();
		registerReceivers();
	}

	/**
	 * 
	 */
	private void loadData() {
		getLoading().show();
		VolleyHttp.doGetRequestTask(getHeaders(), UrlUtil.COMMENT_CENTER + orderId, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				getLoading().dismiss();
				CommentCenter center = DataParser.parserCommentCenter(result);
				if(center.getMessage() != null){
					if(center.getMessage().getCode() == 200){
						list.clear();
						list.addAll(center.getList());
						adapter.notifyDataSetChanged();
					}else{
						ToastUtils.Toast(CommentGoodsActivity.this, center.getMessage().getMessage());
					}
				}else{
					ToastUtils.Toast(CommentGoodsActivity.this, R.string.error);
				}
			}
			
			@Override
			public void onError() {
				getLoading().dismiss();
				ToastUtils.Toast(CommentGoodsActivity.this, R.string.error);
			}
		});
	}

	private NetBroadCastReceiver netReceiver;

	// 广播接收者 注册
	private void registerReceivers() {
		netReceiver = new NetBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_UPDATE_COMMENT);
		getActivity().registerReceiver(netReceiver, intentFilter);
	}
	private class NetBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_UPDATE_COMMENT)) {
				loadData();
			} 
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(netReceiver);
	}
}
