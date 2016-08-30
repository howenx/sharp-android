package com.kakao.kakaogift.activity.mine.pin.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.mine.pin.adapter.MyPinTuanAdapter;
import com.kakao.kakaogift.entity.PinActivity;
/**
 * 
 * @author vince
 *
 */
public class MyPinFragment extends Fragment {

	private MyPinTuanAdapter adapter;
	private PullToRefreshListView mListView;
	private List<PinActivity> data;

	public void setData(List<PinActivity> data) {
		this.data = data;
	}

	/**
	 * 添加标识,返回带标识的对象。
	 * 
	 * @param tag
	 *            标识
	 * @return 带标识的Fragment
	 */
	public static MyPinFragment newInstance(List<PinActivity> data) {
		MyPinFragment fragment = new MyPinFragment();
		fragment.setData(data);
		return fragment;
	}

	@SuppressLint("InflateParams")
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.my_pingou_list_layout, null);
		View notice =  view.findViewById(R.id.notice);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
		mListView.setMode(Mode.DISABLED);
		if (data != null && data.size()>0) {
			notice.setVisibility(View.GONE);
			adapter = new MyPinTuanAdapter(getActivity(), data);
			mListView.setAdapter(adapter);
		}else{
			notice.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
		return view;
	}

}
