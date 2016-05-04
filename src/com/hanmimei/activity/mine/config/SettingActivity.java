package com.hanmimei.activity.mine.config;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.AboutWeActivity;
import com.hanmimei.activity.SuggestionActivity;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.application.HMMApplication;
import com.hanmimei.dao.UserDao;
import com.hanmimei.data.AppConstant;
import com.hanmimei.entity.User;
import com.hanmimei.manager.DataCleanManager;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.DownloadTools;
import com.hanmimei.utils.ToastUtils;

/**
 * @author eric
 *
 */
@SuppressLint("NewApi")
public class SettingActivity extends BaseActivity implements OnClickListener {

	
	private TextView exit,cur_version;
	private TextView versionName,cacheSize;
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
		initView();
	}


	private void initView() {
		exit = (TextView) findViewById(R.id.exit);
		cur_version = (TextView) findViewById(R.id.cur_version);
		versionName = (TextView) findViewById(R.id.versionName);
		cacheSize = (TextView) findViewById(R.id.cache_size);
		if (getUser() == null) {
			exit.setVisibility(View.GONE);
		} else {
			exit.setVisibility(View.VISIBLE);
		}
		cur_version.setText(getResources().getString(R.string.current_version, CommonUtil.getVersionName(this)));
		cacheSize.setText(DataCleanManager.getTotalCacheSize(this));
		if(getVersionInfo() !=null){
			versionName.setVisibility(View.VISIBLE);
		}
		findViewById(R.id.about).setOnClickListener(this);
		 findViewById(R.id.idea).setOnClickListener(this);
		 findViewById(R.id.tel).setOnClickListener(this);
		 findViewById(R.id.clear).setOnClickListener(this);
		 findViewById(R.id.upd).setOnClickListener(this);
		 findViewById(R.id.push).setOnClickListener(this);
		findViewById(R.id.exit).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about:
			startActivity(new Intent(this, AboutWeActivity.class));
			break;
		case R.id.idea:
			startActivity(new Intent(this, SuggestionActivity.class));
			break;
		case R.id.tel:
			showDialog();
			break;
		case R.id.clear:
			DataCleanManager.cleanApplicationData(this);
			cacheSize.setText(DataCleanManager.getTotalCacheSize(this));
			break;
		case R.id.upd:
			if(getVersionInfo() == null){
				ToastUtils.Toast(this, "已经是最新版本");
			}else{
				AlertDialogUtils.showUpdate2Dialog(getActivity(), new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						new DownloadTools(getActivity()).download(getVersionInfo().getDownloadLink());
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
		dialog = AlertDialogUtils.showDialog(this, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Intent.ACTION_CALL, Uri
						.parse("tel:4006640808")));
				dialog.dismiss();
			}
		},"拨打客服电话 400-664-0808","取消","拨打");
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

}
