package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.hanmimei.R;
import com.hanmimei.adapter.MyMsgAdapter;
import com.hanmimei.data.DataParser;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.MessageInfo;
import com.hanmimei.entity.MsgResult;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;

public class MessageActivity extends BaseActivity {
	private SwipeMenuListView mListView;
	private List<MessageInfo> list;
	private MyMsgAdapter adapter;
	private TextView no_msg;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_msg_layout);
		String title = "";
		if(getIntent().getStringExtra("type").equals("system")){
			title = "系统消息";
		}else if(getIntent().getStringExtra("type").equals("coupon")){
			title = "我的资产";
		}else if(getIntent().getStringExtra("type").equals("discount")){
			title = "优惠促销";
		}else if(getIntent().getStringExtra("type").equals("logistics")){
			title = "物流通知";
		}else if(getIntent().getStringExtra("type").equals("goods")){
			title = "商品提醒";
		}
		ActionBarUtil.setActionBarStyle(this, title);
		mListView = (SwipeMenuListView) findViewById(R.id.mListView);
		no_msg = (TextView) findViewById(R.id.no_msg);
		list = new ArrayList<MessageInfo>();
		adapter = new MyMsgAdapter(list, this);
		mListView.setAdapter(adapter);
		mListView.setMenuCreator(creator);
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				delMsg(list.get(position));
			}
		});
		loadData();
	}

	private void delMsg(final MessageInfo messageInfo) {
		Http2Utils.doGetRequestTask(this, getHeaders(), "http://172.28.3.78:9003/client/msg/delMsg/" + messageInfo.getMsgId(), new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				HMessage hMessage = DataParser.paserResultMsg(result);
				if(hMessage.getCode() == 200){
					ToastUtils.Toast(MessageActivity.this, "删除成功");
					list.remove(messageInfo);
					adapter.notifyDataSetChanged();
				}else{
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
		Http2Utils.doGetRequestTask(this, getHeaders(), "http://172.28.3.78:9003/client/msg/list/" + getIntent().getStringExtra("type"), new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				MsgResult msgResult = DataParser.parseMsgInfo(result);
				if(msgResult.getMessage().getCode() == 200){
					list.addAll(msgResult.getList());
					adapter.notifyDataSetChanged();
					if(list.size() <= 0){
						no_msg.setVisibility(View.VISIBLE);
						mListView.setVisibility(View.GONE);
					}
				}else{
//					no_msg.setVisibility(View.VISIBLE);
//					mListView.setVisibility(View.GONE);
					ToastUtils.Toast(MessageActivity.this, msgResult.getMessage().getMessage());
				}
			}
			
			@Override
			public void onError() {
//				no_msg.setVisibility(View.VISIBLE);
//				mListView.setVisibility(View.GONE);
				ToastUtils.Toast(MessageActivity.this, "请求失败，请检查您的网络");
			}
		});
		adapter.notifyDataSetChanged();
	}

	private SwipeMenuCreator creator = new SwipeMenuCreator() {

		@Override
		public void create(SwipeMenu menu) {

			SwipeMenuItem deleteItem = new SwipeMenuItem(
					getApplicationContext());
			// 设置背景颜色
			deleteItem.setBackground(new ColorDrawable(Color
					.parseColor("#e56254")));
			// 设置删除的宽度
			deleteItem.setWidth(CommonUtil.dip2px(120));
			// 设置图标
			// deleteItem.setIcon(R.drawable.icon_delete);
			deleteItem.setTitle("删除消息");
			deleteItem.setTitleColor(getResources().getColor(R.color.white));
			deleteItem.setTitleSize(16);
			// 增加到menu中
			menu.addMenuItem(deleteItem);
		}
	};

}