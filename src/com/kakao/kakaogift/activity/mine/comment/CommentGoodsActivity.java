/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-26 上午9:36:03 
**/
package com.kakao.kakaogift.activity.mine.comment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.mine.comment.adapter.CommentGoodsAdapter;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.CommentCenterVo;
import com.kakao.kakaogift.entity.OrderRemark;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.ToastUtils;

/**
 * @author eric
 *
 */
public class CommentGoodsActivity extends BaseActivity implements OnClickListener{

	private ListView mListView;
	private CommentGoodsAdapter adapter;
	private List<OrderRemark> list;
	private String orderId;
	private boolean isRemarkOk = false;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_goods_layout);
		ActionBarUtil.setActionBarStyle(this, "评价中心",this);
		list = new ArrayList<OrderRemark>();
		orderId = getIntent().getStringExtra("orderId");
		mListView = (ListView) findViewById(R.id.mylist);
		adapter = new CommentGoodsAdapter(this, list);
		mListView.setAdapter(adapter);
		loadData();
		registerReceivers();
	}

	/**
	 * 获取评价中心数据
	 */
	private void loadData() {
		getLoading().show();
		VolleyHttp.doGetRequestTask(getHeaders(), UrlUtil.COMMENT_CENTER + orderId, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				getLoading().dismiss();
//				CommentCenter center = DataParser.parserCommentCenter(result);
				CommentCenterVo center = new Gson().fromJson(result, CommentCenterVo.class);
				if(center.getMessage() != null){
					if(center.getMessage().getCode() == 200){
						list.clear();
						list.addAll(center.getOrderRemark());
						isRemarkOk(center.getOrderRemark());
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

	private void isRemarkOk(List<OrderRemark> list){
		int remarkNum = 0;
		for(int i = 0; i < list.size(); i ++){
			if(list.get(i).getComment() != null){
				remarkNum ++;
			}
		}
		if(remarkNum == list.size()){
			isRemarkOk = true;
		}
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

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			exitClick();
			break;

		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitClick();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void exitClick() {
		if(isRemarkOk){
			sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_CANCLE_ORDER));
		}
		finish();
	}
}
