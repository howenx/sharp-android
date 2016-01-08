package com.hanmimei.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hanmimei.R;

public class AddGoodsSuccessDialog extends AlertDialog {

	
	public AddGoodsSuccessDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_add_to_shopcart_success_panel);
	}

	
}
