package com.hanmimei.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hanmimei.R;

public class UpdateNewDialog extends AlertDialog {

	
	private android.view.View.OnClickListener l;
	
	
	public UpdateNewDialog(Context context,android.view.View.OnClickListener l) {
		super(context,R.style.CustomDialog);
		this.l = l;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_update_new_layout);
		setCanceledOnTouchOutside(false);
		


		findViewById(R.id.btn_close).setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		 findViewById(R.id.btn_cancel).setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		 findViewById(R.id.btn_confirm).setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (l != null)
					l.onClick(v);
				dismiss();
			}
		});
	}

	
	
}
