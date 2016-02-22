package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.hanmimei.R;
import com.hanmimei.adapter.MyMsgAdapter;
import com.hanmimei.entity.MessageInfo;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class MessageActivity extends BaseActivity {

	private SwipeMenuListView mListView;
	private List<MessageInfo> list;
	private MyMsgAdapter adapter;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_msg_layout);
		ActionBarUtil.setActionBarStyle(this, "消息盒子");
		mListView = (SwipeMenuListView) findViewById(R.id.mListView);
		list = new ArrayList<MessageInfo>();
		adapter = new MyMsgAdapter(list, this);
		mListView.setAdapter(adapter);
		mListView.setMenuCreator(creator);
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				list.remove(position);
				adapter.notifyDataSetChanged();
			}
		});
		loadData();
	}
	private void loadData() {
		for(int i = 0; i < 100; i ++){
			MessageInfo info = new MessageInfo();
			info.setTime("20分钟前");
			info.setTitle("这是消息的内容显示");
			list.add(info);
		}
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
//			deleteItem.setIcon(R.drawable.icon_delete);
			deleteItem.setTitle("删除消息");
			deleteItem.setTitleColor(getResources().getColor(R.color.white));
			deleteItem.setTitleSize(16);
			// 增加到menu中
			menu.addMenuItem(deleteItem);
		}
	};

}
