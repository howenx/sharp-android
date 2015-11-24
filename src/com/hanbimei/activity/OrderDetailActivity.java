package com.hanbimei.activity;

import com.hanbimei.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi") 
public class OrderDetailActivity extends BaseActivity implements OnClickListener{

	private TextView header;
	private ImageView back;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.order_detail_layout);
		getActionBar().hide();
		findView();
	}
	private void findView() {
		header = (TextView) findViewById(R.id.header);
		header.setText("订单详情");
		back = (ImageView) findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
	}

}
