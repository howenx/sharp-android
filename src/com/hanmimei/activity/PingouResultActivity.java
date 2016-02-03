package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.listener.TimeEndListner;
import com.hanmimei.entity.Member;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.view.TimerTextView;

public class PingouResultActivity extends BaseActivity implements
		TimeEndListner {

	private TimerTextView timer;
	private ListView mListView;
	private GridLayout gridlayout;
	private List<Member> members;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "组团详情");
		setContentView(R.layout.pingou_result_layout);

		timer = (TimerTextView) findViewById(R.id.timer);
		mListView = (ListView) findViewById(R.id.mListView);
		gridlayout = (GridLayout) findViewById(R.id.gridlayout);
		timer.setTimeEndListner(this);
		long[] time = { 24, 0, 0 };
		timer.setTimes(time, "拼购已经结束，请等待下次");

		timer.beginRun();
		initmembers();

		mListView.setAdapter(new PinTuanListAdapter());
//		mGridView.setAdapter(new PinTuanGridAdapter());
	}

	private void initmembers() {
		members = new ArrayList<Member>();
		for (int i = 0; i < 9; i++) {
			if (i == 0) {
				members.add(new Member(0));
			} else {
				members.add(new Member());
			}
		}
	}

	@Override
	public void isTimeEnd() {

	}

	private class PinTuanListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return members.size();
		}

		@Override
		public Object getItem(int arg0) {
			return members.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder holder = null;
			if (arg1 == null) {
				arg1 = getLayoutInflater().inflate(
						R.layout.pingou_result_list_item_layout, null);
				holder = new ViewHolder(arg1);

				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			return arg1;
		}

		private class ViewHolder {
			TextView nameView, timeView;
			ImageView faceView;
			View xian, contentView;

			public ViewHolder(View view) {
				super();
				this.contentView = view.findViewById(R.id.contentView);
				this.xian = view.findViewById(R.id.xian);
				this.nameView = (TextView) view.findViewById(R.id.nameView);
				this.timeView = (TextView) view.findViewById(R.id.timeView);
				this.faceView = (ImageView) view.findViewById(R.id.faceView);
			}
		}
	}

	private class PinTuanGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return members.size();
		}

		@Override
		public Object getItem(int arg0) {
			return members.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder holder = null;
			if (arg1 == null) {
				arg1 = getLayoutInflater().inflate(
						R.layout.pingou_result_grid_item_layout, null);
				holder = new ViewHolder(arg1);

				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			if (members.get(arg0).getRole() == 0) {
				holder.roleView.setVisibility(View.VISIBLE);
			} else {
				holder.roleView.setVisibility(View.INVISIBLE);
			}

			return arg1;
		}

		private class ViewHolder {
			TextView roleView;
			ImageView faceView;

			public ViewHolder(View view) {
				super();
				this.roleView = (TextView) view.findViewById(R.id.roleView);
				this.faceView = (ImageView) view.findViewById(R.id.faceView);
			}
		}
	}

}
