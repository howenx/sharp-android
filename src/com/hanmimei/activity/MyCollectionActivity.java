package com.hanmimei.activity;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class MyCollectionActivity  extends BaseActivity{
	
	PullToRefreshListView mListView;
	

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_collection_layout);
		mListView = (PullToRefreshListView) findViewById(R.id.mListView);
	}

}
