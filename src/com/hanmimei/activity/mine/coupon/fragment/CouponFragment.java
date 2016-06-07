package com.hanmimei.activity.mine.coupon.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.activity.mine.coupon.adapter.TicketAdapter;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Category;
import com.hanmimei.entity.CouponVo;
import com.hanmimei.entity.Ticket;
import com.hanmimei.entity.User;
import com.hanmimei.manager.CouponMenager;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @author eric
 *
 */
public class CouponFragment extends Fragment implements OnClickListener{

	
	private ListView mListView;
	private TicketAdapter adapter;
	private List<CouponVo> data;
	private BaseActivity activity;
	private Category category;
	private User user;
	private int state;
	private View no_data;
	private LinearLayout no_net;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
		user = activity.getUser();
		data = new ArrayList<CouponVo>();
		adapter = new TicketAdapter(data, activity);
		Bundle bundle = getArguments();
		category = (Category) bundle.getSerializable("category");
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_coupon_layout, null);
		mListView = (ListView) view.findViewById(R.id.mylist);
		no_data =  view.findViewById(R.id.no_data);
		no_net = (LinearLayout) view.findViewById(R.id.no_net);
		view.findViewById(R.id.reload).setOnClickListener(this);
		mListView.setAdapter(adapter);
		if (category.getId().equals("tag01")) {
			state = 0;
		}else if(category.getId().equals("tag02")){
			state = 1;
		}else{
			state = 2;
		}
		loadData();
		return view;
	}

	private void loadData() {
		activity.getLoading().show();
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
	
	private void mCoupno(List<CouponVo> list){
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
		adapter.notifyDataSetChanged();
	}
	private int num1 = 0;
	private int num2 = 0;
	private int num3 = 0;
	private void getNums(List<CouponVo> list){
		for(int i = 0; i < list.size(); i ++){
			if(list.get(i).getState().equals("N")){
				num1 ++;
			}else if(list.get(i).getState().equals("Y")){
				num2 ++;
			}else{
				num3 ++;
			}
		}
		CouponMenager.getInstance().setTitle("未使用（" + num1 + "）", "已使用（" + num2 + "）", "已过期（" + num3 + "）");
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				activity.getLoading().dismiss();
				Ticket ticket = (Ticket) msg.obj;
				if(ticket != null){
					no_net.setVisibility(View.GONE);
					data.clear();
					getNums(ticket.getCoupons());
					mCoupno(ticket.getCoupons());
					if(ticket.getMessage().getCode() == 200){
						if(data.size() > 0){
							mListView.setVisibility(View.VISIBLE);
							no_data.setVisibility(View.GONE);
						}else{
							mListView.setVisibility(View.GONE);
							no_data.setVisibility(View.VISIBLE);
						}
					}
				}else{
					no_net.setVisibility(View.VISIBLE);
					ToastUtils.Toast(activity, "请求失败，请检查您的网络！");
				}
				break;
			default:
				break;
			}
		}
	};
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("CouponFragment"); //统计页面，"MainScreen"为页面名称，可自定义
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("CouponFragment"); 
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.reload:
			loadData();
			break;

		default:
			break;
		}
	}
}
