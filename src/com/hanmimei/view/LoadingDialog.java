package com.hanmimei.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.hanmimei.R;

public class LoadingDialog extends AlertDialog {
	
	
	public LoadingDialog(Context context) {
		super(context,R.style.CustomDimDialog);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_dialog);
		setCanceledOnTouchOutside(false);
	}

	
	
}
