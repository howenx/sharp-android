package com.hanmimei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hanmimei.data.UrlUtil;

public class DoJumpActivity extends BaseActivity {
	// hmmapp://data/vary/889544/241865
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String data = getIntent().getDataString();
//		ToastUtils.Toast(this, data);
		Intent intent = null;
		if (data.contains("pin/activity")) {
			intent = new Intent(this, PingouResultActivity.class);
			intent.putExtra("url",UrlUtil.SERVERY5 + "/promotion/pin/activity"
					+ data.split("activity")[1]);
			startActivity(new Intent(intent));
		} else {
			if (data.contains("pin")) {
				intent = new Intent(this, PingouDetailActivity.class);
			} else {
				intent = new Intent(this, GoodsDetailActivity.class);
			}

			intent.putExtra("url",
					UrlUtil.SERVERY3 + "/comm/detail" + data.split("data")[1]);
			intent.putExtra("from", "web");
			startActivity(new Intent(intent));
		}
		finish();

	}

}
