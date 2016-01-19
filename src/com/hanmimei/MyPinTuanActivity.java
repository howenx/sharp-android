package com.hanmimei;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.activity.BaseActivity;

public class MyPinTuanActivity extends BaseActivity {

	private PullToRefreshListView mListView;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_pintuan_list_layout);
		mListView = (PullToRefreshListView) findViewById(R.id.mylist);
//		mListView.setAdapter(adapter);
	}

}
