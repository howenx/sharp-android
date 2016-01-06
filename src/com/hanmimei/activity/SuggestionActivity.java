package com.hanmimei.activity;

import com.hanmimei.R;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class SuggestionActivity extends BaseActivity implements OnClickListener{

	private TextView send;
	private EditText editText;
	private String str;
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.idea_layout);
		ActionBarUtil.setActionBarStyle(this, "意见反馈");
		editText = (EditText) findViewById(R.id.idea);
		send = (TextView) findViewById(R.id.send);
		send.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send:
			checkInput();
			break;
		default:
			break;
		}
	}
	private void checkInput() {
		str = editText.getText().toString();
		if(!str.equals("")){
			doSend();
		}else{
			editText.setError("输入不能为空");
		}
	}
	private void doSend() {
		dialog = CommonUtil.dialog(this, "正在提交，请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					Message msg = mHandler.obtainMessage(1);
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				finish();
				break;

			default:
				break;
			}
		}
		
	};
}
