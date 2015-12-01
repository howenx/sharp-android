package com.hanmimei.activity;

import com.hanmimei.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi") 
public class AboutIdCardActivity extends BaseActivity implements OnClickListener{

	private TextView header;
	private ImageView back;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.about_id_card_layout);
		getActionBar().hide();
		back = (ImageView) findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		header = (TextView) findViewById(R.id.header);
		header.setText("关于身份证");
		
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
