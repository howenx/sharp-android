package com.kakao.kakaogift.activity.mine.config;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.flyco.dialog.widget.NormalDialog;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.manager.DataCleanManager;
import com.kakao.kakaogift.manager.HDownloadManager;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.AlertDialogUtils;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.utils.ToastUtils;

/**
 * @author eric
 * 
 */
@SuppressLint("NewApi")
public class SettingActivity extends BaseActivity implements OnClickListener {

	private TextView cur_version;
	private TextView versionName, cacheSize;
	private NormalDialog dialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.setting_layout);
		ActionBarUtil.setActionBarStyle(this, "设置");
		initView();
	}

	private void initView() {
		cur_version = (TextView) findViewById(R.id.cur_version);
		versionName = (TextView) findViewById(R.id.versionName);
		cacheSize = (TextView) findViewById(R.id.cache_size);
		cur_version.setText(getResources().getString(R.string.current_version,
				CommonUtils.getVersionName(this)));
		cacheSize.setText(DataCleanManager.getTotalCacheSize(this));
		if (getVersionInfo() != null) {
			versionName.setVisibility(View.VISIBLE);
		}
		findViewById(R.id.about).setOnClickListener(this);
		findViewById(R.id.idea).setOnClickListener(this);
		findViewById(R.id.tel).setOnClickListener(this);
		findViewById(R.id.clear).setOnClickListener(this);
		findViewById(R.id.upd).setOnClickListener(this);
		findViewById(R.id.push).setOnClickListener(this);
		findViewById(R.id.judge).setOnClickListener(this);

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
			if (getVersionInfo() == null) {
				ToastUtils.Toast(this, "已经是最新版本");
			} else {
				AlertDialogUtils.showUpdateDialog(getActivity(),
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								new HDownloadManager(getActivity())
										.download(getVersionInfo().getDownloadLink());
							}
						});
			}
			break;
		case R.id.judge:
			judge("com.kakao.kakaogift");
			break;
		default:
			break;
		}
	}

	private void judge(String appPkg) {
		if (TextUtils.isEmpty(appPkg))
			return;
		Uri uri = Uri.parse("market://details?id=" + appPkg);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private void showDialog() {
		dialog = AlertDialogUtils.showDialog(this, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Intent.ACTION_CALL, Uri
						.parse("tel:01053678808")));
				dialog.dismiss();
			}
		}, "拨打客服电话 010-53678808", "取消", "拨打");
	}
	

}
