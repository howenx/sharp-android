package com.hanmimei.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.application.HMMApplication;
import com.hanmimei.dao.UserDao;
import com.hanmimei.data.AppConstant;
import com.hanmimei.entity.User;
import com.hanmimei.service.DownloadService;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.DoJumpUtils;
import com.hanmimei.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class SettingActivity extends BaseActivity implements OnClickListener {

	
	private final String TAG = this.getClass().getName();
	private Drawable about_Drawable;
	private Drawable idea_Drawable;
	private Drawable comment_Drawable;
	private Drawable tel_Drawable;
	private Drawable del_Drawable;
	private Drawable push_Drawable;
	private TextView about;
	private TextView idea;
	private TextView comment;
	private TextView tel;
	private TextView del;
	private TextView upd;
	private TextView exit;
	private TextView versionName;
	private TextView push;
	private ProgressDialog pdialog;
	private AlertDialog dialog;
	private HMMApplication application;
	private UserDao userDao;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.setting_layout);
		ActionBarUtil.setActionBarStyle(this, "设置");
		application = (HMMApplication) getApplication();
		userDao = getDaoSession().getUserDao();
		initDrawable();
		initView();
	}

	private void initDrawable() {
		about_Drawable = getResources().getDrawable(
				R.drawable.iconfont_guanyuwomen);
		about_Drawable.setBounds(0, 0, 40, 40);
		idea_Drawable = getResources().getDrawable(
				R.drawable.iconfont_yijianfankui);
		idea_Drawable.setBounds(0, 0, 40, 40);
		comment_Drawable = getResources().getDrawable(
				R.drawable.iconfont_comment);
		comment_Drawable.setBounds(0, 0, 45, 45);
		tel_Drawable = getResources().getDrawable(
				R.drawable.iconfont_lianxiwomen);
		tel_Drawable.setBounds(0, 0, 45, 45);
		del_Drawable = getResources().getDrawable(
				R.drawable.iconfont_qingchuhuancun);
		del_Drawable.setBounds(0, 0, 40, 40);
		push_Drawable = getResources().getDrawable(
				R.drawable.icon_tuisong);
		push_Drawable.setBounds(0, 0, 40, 40);
	}

	private void initView() {
		about = (TextView) findViewById(R.id.about);
		idea = (TextView) findViewById(R.id.idea);
		comment = (TextView) findViewById(R.id.comment);
		tel = (TextView) findViewById(R.id.tel);
		del = (TextView) findViewById(R.id.del);
		exit = (TextView) findViewById(R.id.exit);
		upd = (TextView) findViewById(R.id.upd);
		versionName = (TextView) findViewById(R.id.versionName);
		if (getUser() == null) {
			exit.setVisibility(View.GONE);
		} else {
			exit.setVisibility(View.VISIBLE);
		}
		if(getVersionInfo() !=null){
			versionName.setText(getVersionInfo().getReleaseNumber());
		}
		about.setCompoundDrawables(about_Drawable, null, null, null);
		idea.setCompoundDrawables(idea_Drawable, null, null, null);
		comment.setCompoundDrawables(comment_Drawable, null, null, null);
		tel.setCompoundDrawables(tel_Drawable, null, null, null);
		del.setCompoundDrawables(del_Drawable, null, null, null);
		upd.setCompoundDrawables(del_Drawable, null, null, null);
		push.setCompoundDrawables(push_Drawable, null, null, null);
		about.setOnClickListener(this);
		idea.setOnClickListener(this);
		comment.setOnClickListener(this);
		tel.setOnClickListener(this);
		del.setOnClickListener(this);
		upd.setOnClickListener(this);
		push.setOnClickListener(this);
		exit.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about:
			DoJumpUtils.doJump(this, AboutWeActivity.class);
			break;
		case R.id.idea:
			DoJumpUtils.doJump(this, SuggestionActivity.class);
			break;
		case R.id.comment:
			break;
		case R.id.tel:
			showDialog();
			break;
		case R.id.del:
			break;
		case R.id.upd:
			if(getVersionInfo() == null){
				ToastUtils.Toast(this, "已经是最新版本");
			}else{
				AlertDialogUtils.showUpdateDialog(getActivity(), getVersionInfo(), new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),DownloadService.class);
						intent.putExtra("url", getVersionInfo().getDownloadLink());
						startService(intent);
					}
				});
			}
			break;
		case R.id.exit:
			doExit();
			break;
		default:
			break;
		}
	}

	private void showDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout,
				null);
		dialog = new AlertDialog.Builder(this).create();
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText("拨打客服电话 400100108888");
		dialog.setView(view);
		dialog.show();
		view.findViewById(R.id.cancle).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
					}
				});
		view.findViewById(R.id.besure).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(Intent.ACTION_CALL, Uri
								.parse("tel:18612433707")));
						dialog.dismiss();
					}
				});
	}

	private void doExit() {
		pdialog = new ProgressDialog(this);
		pdialog.setMessage("正在退出...");
		pdialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					User user = new User();
					user.setPhone(getUser().getPhone());
					application.clearLoginUser();
					userDao.deleteAll();
					userDao.insert(user);
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = mHandler.obtainMessage(1);
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				pdialog.dismiss();
				sendBroadcast(new Intent( AppConstant.MESSAGE_BROADCAST_QUIT_LOGIN_ACTION));
				finish();
				break;

			default:
				break;
			}
		}

	};
	
	

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SettingActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SettingActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
													// onPageEnd 在onPause
													// 之前调用,因为 onPause
													// 中会保存信息。"SplashScreen"为页面名称，可自定义
		MobclickAgent.onPause(this);
	}

}
