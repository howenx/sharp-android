package com.hanmimei.activity;

import com.hanmimei.R;
import com.hanmimei.utils.ActionBarUtil;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class HmmServiceActivity extends BaseActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hhm_service_layout);
		ActionBarUtil.setActionBarStyle(this, "韩秘美服务条款");
	}

}
