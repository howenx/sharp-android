package com.hanmimei.activity.mine.user;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMessage;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ToastUtils;

/**
 * @author eric
 *
 */
public class EditUserNameActivity extends BaseActivity implements OnClickListener{

	private EditText nameText;
	private TextView save;
	private String name;
	private JSONObject object;
	private ProgressDialog dialog;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_username_layout);
		ActionBarUtil.setActionBarStyle(this, "修改昵称");
		nameText = (EditText) findViewById(R.id.name);
		nameText.setText(getIntent().getStringExtra("name"));
		Selection.setSelection((Spannable)nameText.getText(), nameText.getText().toString().length());
		save = (TextView) findViewById(R.id.send);
		save.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send:
			checkName();
			break;

		default:
			break;
		}
	}
	private void checkName() {
		name = nameText.getText().toString();
		if(!CommonUtil.inputIsName(name,2,15).equals("")){
			ToastUtils.Toast(this, "姓名"+ CommonUtil.inputIsName(name,2,15));
			return;
		} else {
			toObject();
			UpUserInfo();
		}
	}
	private void UpUserInfo() {
		dialog = CommonUtil.dialog(this, "正在修改，请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.post(UrlUtil.UPDATE_USERINFO, object,
						"id-token", EditUserNameActivity.this.getUser().getToken());
				HMessage hm = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = hm;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	// 封装json，作为请求参数
		private void toObject() {
			try {
				object = new JSONObject();
				object.put("nickname", name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@SuppressLint("HandlerLeak") 
		private Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					dialog.dismiss();
					HMessage hm = (HMessage) msg.obj;
					if (hm.getCode() != null) {
						if (hm.getCode() == 200) {
							sendBroadcast(new Intent(
									AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION));
							Intent intent = new Intent();
							intent.putExtra("name", name);
							setResult(AppConstant.UP_USER_NAME_CODE,intent);
							finish();
						} else {
							ToastUtils.Toast(EditUserNameActivity.this,"修改失败,请检查您的网络");
						}
					} else {
						ToastUtils.Toast(EditUserNameActivity.this,"修改失败,请检查您的网络");
					}
					break;

				default:
					break;
				}
			}

		};

}
