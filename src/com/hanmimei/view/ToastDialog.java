package com.hanmimei.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hanmimei.R;

public class ToastDialog extends AlertDialog {

	private String massage;
	private TextView msgView;
	
	public ToastDialog(Context context,String massage) {
		super(context,R.style.CustomDialog);
		this.massage =massage;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_toast_massage);
		setCanceledOnTouchOutside(true);
		
		 msgView = (TextView) findViewById(R.id.message);
		msgView.setText(massage);
	}
	
	public void setMessage(String  msg){
		msgView.setText(msg);
	}


	@Override
	public void show() {
		super.show();
	}
	
	

}
