package com.kakao.kakaogift.activity.mine.config;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.utils.ToastUtils;

/**
 * @author eric
 * 
 */
public class SuggestionActivity extends BaseActivity implements OnClickListener {

	private EditText editText;
	private String str;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.idea_layout);
		ActionBarUtil.setActionBarStyle(this, "意见反馈");
		editText = (EditText) findViewById(R.id.idea);
		findViewById(R.id.send).setOnClickListener(this);
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
		if (!str.equals("")) {
			doSend();
		} else {
			ToastUtils.Toast(this, "输入不能为空");
		}
	}

	private void doSend() {
		dialog = CommonUtils.dialog(this, "正在提交，请稍后...");
		dialog.show();
//		Map<String, String> map = new HashMap<String, String>();
		JSONObject object = new JSONObject();
		try {
			object.put("content", str);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		VolleyHttp.doPostRequestTask(getHeaders(), UrlUtil.SUGGESTION_URL, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				dialog.dismiss();
				finish();
			}
			
			@Override
			public void onError() {
				dialog.dismiss();
				ToastUtils.Toast(SuggestionActivity.this, "提交失败，请检查您的网络");
			}
		}, object.toString());
		
	}
}
