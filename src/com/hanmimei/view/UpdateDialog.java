package com.hanmimei.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hanmimei.R;

public class UpdateDialog extends AlertDialog {

	
	String[] tb ;
	private android.view.View.OnClickListener l;
	
	
	public UpdateDialog(Context context,String[] tb,android.view.View.OnClickListener l) {
		super(context,R.style.CustomDialog);
		this.tb = tb;
		this.l = l;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_update_layout);
		setCanceledOnTouchOutside(false);
		
		TextView content = (TextView) findViewById(R.id.content);
		TextView btn_cancel = (TextView) findViewById(R.id.btn_cancel);
		TextView btn_queding = (TextView) findViewById(R.id.btn_confirm);

		if (tb[0] != null) {
			content.setVisibility(View.VISIBLE);
			content.setText(tb[0]);
		}
		if (tb[1] != null) {
			btn_cancel.setVisibility(View.VISIBLE);
			btn_cancel.setText(tb[1]);
		}
		if (tb[2] != null) {
			btn_queding.setVisibility(View.VISIBLE);
			btn_queding.setText(tb[2]);
		}

		btn_cancel.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		btn_queding.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (l != null)
					l.onClick(v);
				dismiss();
			}
		});
	}

	
	
}
