package com.kakao.kakaogift.activity.mine.coupon.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.mine.coupon.adapter.TicketAdapter;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.Category;
import com.kakao.kakaogift.entity.CouponVo;
import com.kakao.kakaogift.entity.Ticket;
import com.kakao.kakaogift.event.CouponEvent;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.manager.CouponMenager;
import com.kakao.kakaogift.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.ypy.eventbus.EventBus;

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
	private int state;
	private View no_data;
	private LinearLayout no_net;
	private CouponMenager mCouponMenager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (BaseActivity) getActivity();
		data = new ArrayList<CouponVo>();
		adapter = new TicketAdapter(data, activity);
		Bundle bundle = getArguments();
		category = (Category) bundle.getSerializable("category");
		mCouponMenager = (CouponMenager) bundle.getSerializable("CouponMenager");
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
		VolleyHttp.doGetRequestTask(activity.getHeaders(), UrlUtil.GET_COUPON_LIST_URL, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				activity.getLoading().dismiss();
				Ticket ticket = new Gson().fromJson(result, Ticket.class);
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
			}
			
			@Override
			public void onError() {
				activity.getLoading().dismiss();
				no_net.setVisibility(View.VISIBLE);
				ToastUtils.Toast(activity, "请求失败，请检查您的网络！");
				
			}
		});
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
		mCouponMenager.setTitle("未使用（" + num1 + "）", "已使用（" + num2 + "）", "已过期（" + num3 + "）");
		EventBus.getDefault().post(new CouponEvent(num1));
	}
	
	
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
