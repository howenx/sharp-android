package com.hanmimei.activity;

import com.hanmimei.R;
import com.hanmimei.application.MyApplication;
import com.hanmimei.dao.UserDao;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi") 
public class SettingActivity extends BaseActivity implements OnClickListener{

	private TextView header;
	private ImageView back;
	private Drawable about_Drawable;
	private Drawable idea_Drawable;
	private Drawable comment_Drawable;
	private Drawable tel_Drawable;
	private Drawable del_Drawable;
	private TextView about;
	private TextView idea;
	private TextView comment;
	private TextView tel;
	private TextView del;
	private TextView exit;
	
	private ProgressDialog dialog;
	
	private MyApplication application;
	private UserDao userDao;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.setting_layout);
//		getActionBar().hide();
		application = (MyApplication) getApplication();
		userDao = getDaoSession().getUserDao();
		initDrawable();
		initView();
	}
	private void initDrawable() {
		about_Drawable = getResources().getDrawable(R.drawable.iconfont_guanyuwomen);
		about_Drawable.setBounds(0, 0, 40, 40);
		idea_Drawable = getResources().getDrawable(R.drawable.iconfont_yijianfankui);
		idea_Drawable.setBounds(0, 0, 40, 40);
		comment_Drawable = getResources().getDrawable(R.drawable.iconfont_comment);
		comment_Drawable.setBounds(0, 0, 45, 45);
		tel_Drawable = getResources().getDrawable(R.drawable.iconfont_lianxiwomen);
		tel_Drawable.setBounds(0, 0, 40, 40);
		del_Drawable = getResources().getDrawable(R.drawable.iconfont_qingchuhuancun);
		del_Drawable.setBounds(0, 0, 40, 40);
	}
	private void initView() {
		header = (TextView) findViewById(R.id.header);
		header.setText("设置");
		findViewById(R.id.back).setVisibility(View.VISIBLE);
		findViewById(R.id.back).setOnClickListener(this);
		about = (TextView) findViewById(R.id.about);
		idea = (TextView) findViewById(R.id.idea);
		comment = (TextView) findViewById(R.id.comment);
		tel = (TextView) findViewById(R.id.tel);
		del = (TextView) findViewById(R.id.del);
		exit = (TextView) findViewById(R.id.exit);
		about.setCompoundDrawables(about_Drawable, null, null, null);
		idea.setCompoundDrawables(idea_Drawable, null, null, null);
		comment.setCompoundDrawables(comment_Drawable, null, null, null);
		tel.setCompoundDrawables(tel_Drawable, null, null, null);
		del.setCompoundDrawables(del_Drawable, null, null, null);
		about.setOnClickListener(this);
		idea.setOnClickListener(this);
		comment.setOnClickListener(this);
		tel.setOnClickListener(this);
		del.setOnClickListener(this);
		exit.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about:
			
			break;
		case R.id.idea:
			break;
		case R.id.comment:
			break;
		case R.id.tel:
			break;
		case R.id.del:
			break;
		case R.id.exit:
			doExit();
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}
	private void doExit() {
		dialog = new ProgressDialog(this);
		dialog.setMessage("正在退出...");
		dialog.show();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = mHandler.obtainMessage(1);
				mHandler.sendMessage(msg);
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
