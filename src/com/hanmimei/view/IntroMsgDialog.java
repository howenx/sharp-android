package com.hanmimei.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.hanmimei.R;
import com.hanmimei.utils.PreferenceUtil.IntroConfig;

public class IntroMsgDialog extends AlertDialog {

	
	private Context mContext;
	
	
	public IntroMsgDialog(Context context) {
		super(context,R.style.CustomDialog);
		this.mContext = context;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_intro_msg);
		setCanceledOnTouchOutside(false);
		findViewById(R.id.page_indro_message).setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				IntroConfig.putIntroMsgCfg(mContext, IntroConfig.INTRO_CONFIG_VALUE_NO);
				findViewById(R.id.page_indro_message).setVisibility(View.GONE);
			}
		});
	}

	
	
}
