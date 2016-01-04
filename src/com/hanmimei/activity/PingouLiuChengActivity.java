package com.hanmimei.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.hanmimei.R;
import com.hanmimei.utils.ActionBarUtil;

public class PingouLiuChengActivity extends BaseActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pingou_liucheng_layout);
		ActionBarUtil.setActionBarStyle(this, "拼团流程");
		ImageView my_image_view = (ImageView) findViewById(R.id.my_image_view);
		my_image_view.setImageResource(R.drawable.pingou_liucheng);
	}

}
