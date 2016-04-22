package com.hanmimei.activity.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.adapter.MyPinTuanAdapter;
import com.hanmimei.entity.PinActivity;
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

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.my_pingou_list_layout, null);
		TextView notice = (TextView) view.findViewById(R.id.notice);
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
