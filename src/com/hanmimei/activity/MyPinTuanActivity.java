package com.hanmimei.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.R.id;
import com.hanmimei.R.layout;

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
