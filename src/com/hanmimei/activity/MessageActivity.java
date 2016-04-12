package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.adapter.MyMsgAdapter;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.MessageInfo;
import com.hanmimei.entity.MsgResult;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;

/**
 * @author eric
 *
 */
public class MessageActivity extends BaseActivity implements OnClickListener {
	private ListView mListView;
	private List<MessageInfo> list;
	private MyMsgAdapter adapter;
	private TextView no_msg;
//	private boolean doDel = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_msg_layout);
		String title = "";
		list = new ArrayList<MessageInfo>();
		if (getIntent().getStringExtra("type").equals("system")) {
			title = "系统消息";
			adapter = new MyMsgAdapter(list, this, "system");
		} else if (getIntent().getStringExtra("type").equals("coupon")) {
			title = "我的资产";
			adapter = new MyMsgAdapter(list, this, "coupon");
		} else if (getIntent().getStringExtra("type").equals("discount")) {
			adapter = new MyMsgAdapter(list, this, "discount");
			title = "优惠促销";
		} else if (getIntent().getStringExtra("type").equals("logistics")) {
			adapter = new MyMsgAdapter(list, this, "logistics");
			title = "物流通知";
		} else if (getIntent().getStringExtra("type").equals("goods")) {
			adapter = new MyMsgAdapter(list, this, "goods");
			title = "商品提醒";
		}
		ActionBarUtil.setActionBarStyle(this, title, R.drawable.hmm_edit_delete,
				true, this, this);
		mListView = (ListView) findViewById(R.id.mListView);
		no_msg = (TextView) findViewById(R.id.no_msg);
		mListView.setAdapter(adapter);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				showDelLog(list.get(arg2));	
				return false;
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = null;
				if (getIntent().getStringExtra("type").equals("goods")) {
					if(list.get(arg2).getTargetType().equals("D")){
						intent = new Intent(MessageActivity.this,GoodsDetailActivity.class);
						intent.putExtra("url", list.get(arg2).getMsgUrl());
						startActivity(intent);
					}else if(list.get(arg2).getTargetType().equals("P")){
						intent = new Intent(MessageActivity.this,PingouDetailActivity.class);
						intent.putExtra("url", list.get(arg2).getMsgUrl());
						startActivity(intent);
					}else if(list.get(arg2).getTargetType().equals("V")){
						intent = new Intent(MessageActivity.this,PingouResultActivity.class);
						intent.putExtra("url", list.get(arg2).getMsgUrl());
						startActivity(intent);
					}
				}else if(getIntent().getStringExtra("type").equals("logistics")){
					
				}else if(getIntent().getStringExtra("type").equals("coupon")){
					startActivity(new Intent(MessageActivity.this, MyCouponActivity.class));
				}else if(getIntent().getStringExtra("type").equals("discount")){
					
				}else{
					
				}
			}
		});
		loadData();
	}

	private void delMsg(final MessageInfo messageInfo) {
		Http2Utils.doGetRequestTask(
				this,
				getHeaders(),
				UrlUtil.DEL_MSG
						+ messageInfo.getMsgId(), new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						HMessage hMessage = DataParser.paserResultMsg(result);
						if (hMessage.getCode() == 200) {
							ToastUtils.Toast(MessageActivity.this, "删除成功");
							list.remove(messageInfo);
							adapter.notifyDataSetChanged();
						} else {
							ToastUtils.Toast(MessageActivity.this, "删除失败");
						}
					}

					@Override
					public void onError() {
						ToastUtils.Toast(MessageActivity.this, "删除失败，请检查您的网络");
					}
				});
	}

	private void loadData() {
		getLoading().show();
		Http2Utils.doGetRequestTask(this, getHeaders(),UrlUtil.GET_MSG_LIST
						+ getIntent().getStringExtra("type"),
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getLoading().dismiss();
						MsgResult msgResult = DataParser.parseMsgInfo(result);
						if (msgResult.getMessage().getCode() == 200) {
							list.addAll(msgResult.getList());
							adapter.notifyDataSetChanged();
							if (list.size() <= 0) {
								no_msg.setVisibility(View.VISIBLE);
								mListView.setVisibility(View.GONE);
							}
						} else {
							// no_msg.setVisibility(View.VISIBLE);
							// mListView.setVisibility(View.GONE);
							ToastUtils.Toast(MessageActivity.this, msgResult
									.getMessage().getMessage());
						}
					}

					@Override
					public void onError() {
						getLoading().dismiss();
						// no_msg.setVisibility(View.VISIBLE);
						// mListView.setVisibility(View.GONE);
						ToastUtils.Toast(MessageActivity.this, "请求失败，请检查您的网络");
					}
				});
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting:
			showDelDialog();
			break;
		case R.id.back:
			finish();
			sendMsgUpBroadcast();
			break;
		default:
			break;
		}
	}
	private AlertDialog dialog;
	private void showDelDialog(){
		dialog = AlertDialogUtils.showDialog(this, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				delAll();
			}
		});
	}
	private AlertDialog delDialog;
	private void showDelLog(final MessageInfo messageInfo){
		delDialog = AlertDialogUtils.showDialog(this, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				delDialog.dismiss();
				delMsg(messageInfo);
			}
		});
	}

	private void delAll() {
		Http2Utils.doGetRequestTask(
				this,
				getHeaders(),UrlUtil.DEL_MSG_TYPE
						+ getIntent().getStringExtra("type"), new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						HMessage hMessage = DataParser.paserResultMsg(result);
						if (hMessage.getCode() == 200) {
							list.clear();
//							sendMsgUpBroadcast();
//							doDel = true;
							adapter.notifyDataSetChanged();
							mListView.setVisibility(View.GONE);
							no_msg.setVisibility(View.VISIBLE);
							ToastUtils.Toast(MessageActivity.this, "清空消息成功");
						} else {
							ToastUtils.Toast(MessageActivity.this, "清空消息失败");
						}
					}

					@Override
					public void onError() {
						ToastUtils.Toast(MessageActivity.this, "清空消息失败，请检查您的网络");
					}
				});
	}
	private void sendMsgUpBroadcast(){
		if(list.size() <= 0){
			sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_UPDATE_MSG));
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			sendMsgUpBroadcast();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
