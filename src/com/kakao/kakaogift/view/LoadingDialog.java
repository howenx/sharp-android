package com.kakao.kakaogift.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kakao.kakaogift.R
;

public class LoadingDialog extends AlertDialog {
	
	private Context context;
	public LoadingDialog(Context context) {
		super(context,R.style.CustomDimDialog);
		this.context = context;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_dialog);
		setCanceledOnTouchOutside(false);
		
		ImageView imageView = (ImageView) findViewById(R.id.loading);
		Glide.with(context).load(R.raw.loading).into(imageView);
	}

	
	
	
}
