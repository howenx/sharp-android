package com.hanmimei.fragment;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.adapter.TicketAdapter;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Category;
import com.hanmimei.entity.Coupon;
import com.hanmimei.entity.Ticket;
import com.hanmimei.entity.User;
import com.hanmimei.utils.HttpUtils;

public class CouponFragment extends Fragment {

	
	private PullToRefreshListView mListView;
	private TicketAdapter adapter;
	private List<Coupon> data;
	private BaseActivity activity;
	private Category category;
	private User user;
	private int state;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
		user = activity.getUser();
		data = new ArrayList<Coupon>();
		adapter = new TicketAdapter(data, activity);
		Bundle bundle = getArguments();
		category = (Category) bundle.getSerializable("category");
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_coupon_layout, null);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
		mListView.setAdapter(adapter);
		if (category.getId().equals("tag00")) {
			state = 0;
		}else if(category.getId().equals("tag01")){
			state = 1;
		}else{
			state = 2;
		}
		loadData();
		return view;
	}

	private void loadData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.get(UrlUtil.GET_COUPON_LIST_URL, user.getToken());
				Ticket ticket = new Gson().fromJson(result, Ticket.class);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = ticket;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	
	private void mCoupno(List<Coupon> list){
		String mState = "N";
		if(state == 0){
			mState = "N";
			
		}else if(state == 1){
			mState = "Y";
		}else{
			mState = "S";
		}
		for(int i = 0; i < list.size(); i ++){
			if(list.get(i).getState().equals(mState)){
				data.add(list.get(i));
			}
		}
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Ticket ticket = (Ticket) msg.obj;
				if(ticket != null){
					if(ticket.getMessage().getCode() == 200){
						data.clear();
						mCoupno(ticket.getCoupons());
						adapter.notifyDataSetChanged();
					}
				}
				break;

			default:
				break;
			}
		}
		
	};
}
